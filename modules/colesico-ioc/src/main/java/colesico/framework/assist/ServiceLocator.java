/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.assist;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.ServiceConfigurationError;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * ServiceLoader alternative
 */
public class ServiceLocator<S> {

    static final String SERVICE_CATALOG_PREFIX = "META-INF/services/";

    private final Class<S> service;
    private final ClassLoader classLoader;
    private final Predicate<Class<? extends S>> classFilter;
    private final AccessControlContext acc;

    protected ServiceLocator(Class<?> caller, Class<S> service, ClassLoader classLoader, Predicate<Class<? extends S>> classFilter) {
        if (caller == null) {
            throw new ServiceConfigurationError("Caller class is null");
        }

        if (service == null) {
            throw new ServiceConfigurationError("Service class is null");
        }
        this.service = service;

        Module callerModule = caller.getModule();
        if (!callerModule.canUse(service)) {
            throw new ServiceConfigurationError("Module '" + callerModule + "' does not declare 'uses' for class " + service.getName());
        }

        if (classLoader != null) {
            this.classLoader = classLoader;
        } else {
            this.classLoader = ClassLoader.getSystemClassLoader();
        }
        this.classFilter = classFilter;

        this.acc = (System.getSecurityManager() != null)
            ? AccessController.getContext()
            : null;
    }

    protected Set<String> getServiceProviderClassNames(URL serviceProvidersCatalogueUrl) {
        try {
            URLConnection uc = serviceProvidersCatalogueUrl.openConnection();
            uc.setUseCaches(false);
            try (InputStream in = uc.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                Predicate<String> filter = (str) -> !((str == null) || str.equals("") || str.startsWith("#"));
                Set<String> result = reader.lines().map(String::trim).filter(filter).collect(Collectors.toSet());
                return result;
            }
        } catch (IOException x) {
            throw new ServiceConfigurationError("Error accessing service configuration file", x);
        }
    }

    protected Enumeration<URL> findServiceProviderCatalogues(String serviceClassName, ClassLoader classLoader) {
        try {
            String fullName = SERVICE_CATALOG_PREFIX + serviceClassName;
            if (classLoader == null) {
                return ClassLoader.getSystemResources(fullName);
            } else {
                return classLoader.getResources(fullName);
            }
        } catch (IOException x) {
            throw new ServiceConfigurationError("Error locating service configuration files", x);
        }
    }

    @SuppressWarnings("unchecked")
    protected Set<Class<? extends S>> getServiceProviderClasses(String serviceClassName, ClassLoader classLoader) {
        Set<Class<? extends S>> result = new HashSet<>();
        Enumeration<URL> serviceProviderCatalogues = findServiceProviderCatalogues(serviceClassName, classLoader);
        while (serviceProviderCatalogues.hasMoreElements()) {
            URL serviceProvidersCatalogueUrl = serviceProviderCatalogues.nextElement();
            Set<String> serviceProviderClassNames = getServiceProviderClassNames(serviceProvidersCatalogueUrl);
            for (String serviceProviderClassName : serviceProviderClassNames) {
                try {
                    Class<? extends S> clazz = (Class<? extends S>) Class.forName(serviceProviderClassName, false, classLoader);
                    result.add(clazz);
                } catch (ClassCastException cce) {
                    throw new ServiceConfigurationError("Service provider " + serviceProviderClassName + " class must be subclass of " + serviceClassName);
                } catch (ClassNotFoundException x) {
                    throw new ServiceConfigurationError("Service provider " + serviceProviderClassName + " class not found");
                }
            }
        }
        return result;
    }

    protected Set<S> getProvidersImpl(String serviceClassName, ClassLoader classLoader, Predicate<Class<? extends S>> classFilter) {
        Set<S> result = new HashSet<>();
        Set<Class<? extends S>> srvProvClasses = getServiceProviderClasses(serviceClassName, classLoader);
        for (Class<? extends S> clazz : srvProvClasses) {
            if ((classFilter != null) && !classFilter.test(clazz)) {
                continue;
            }
            try {
                S service = clazz.getDeclaredConstructor().newInstance();
                result.add(service);
            } catch (Exception e) {
                throw new ServiceConfigurationError("Service '" + clazz.getName() + "' instantiation error: " + e);
            }
        }
        return result;
    }

    public Set<? extends S> getProviders() {
        if (acc == null) {
            return getProvidersImpl(service.getName(), classLoader, classFilter);
        } else {
            PrivilegedAction<Set<? extends S>> action = () -> getProvidersImpl(service.getName(), classLoader, classFilter);
            return AccessController.doPrivileged(action, acc);
        }
    }

    public static <S> ServiceLocator<S> of(Class<?> caller, Class<S> service, ClassLoader classLoader, Predicate<Class<? extends S>> classFilter) {
        return new ServiceLocator<>(caller, service, classLoader, classFilter);
    }

    public static <S> ServiceLocator<S> of(Class<?> caller, Class<S> service, ClassLoader classLoader) {
        return new ServiceLocator<>(caller, service, classLoader, null);
    }

    public static <S> ServiceLocator<S> of(Class<?> caller, Class<S> service, Predicate<Class<? extends S>> classFilter) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return new ServiceLocator<>(caller, service, classLoader, classFilter);
    }

    public static <S> ServiceLocator<S> of(Class<?> caller, Class<S> service) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return new ServiceLocator<>(caller, service, classLoader, null);
    }

}
