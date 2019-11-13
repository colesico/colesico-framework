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

package colesico.framework.ioc.internal;

import colesico.framework.ioc.*;
import colesico.framework.ioc.ioclet.AdvancedIoc;
import colesico.framework.ioc.ioclet.DefaultPolysupplier;
import colesico.framework.ioc.ioclet.DefaultProvider;
import colesico.framework.ioc.ioclet.Factory;

import javax.inject.Provider;
import java.util.Map;

/**
 * Ioc container implementation to use in develop mode / for testing
 *
 * @author Vladlen Larionov
 */
public final class LazyIocContainer implements AdvancedIoc {

    public static final String CIRCULAR_DEP_ERR_MSG = "Circular dependence for key: %s";

    private final Map<Key<?>, Factory<?>> factories;

    public LazyIocContainer(Map<Key<?>, Factory<?>> factories) {
        this.factories = factories;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T instance(Key<T> key, Object message) {
        Factory factory = factories.get(key);
        if (factory == null) {
            throw new UnsatisfiedInjectionException(key);
        }
        activate(factory, key);
        return (T) factory.get(message);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T instanceOrNull(Key<T> key, Object message) {
        Factory factory = factories.get(key);
        if (factory == null) {
            return null;
        }
        activate(factory, key);
        return (T) factory.get(message);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Provider<T> provider(Key<T> key, Object message) {
        Factory factory = factories.get(key);
        if (factory == null) {
            throw new UnsatisfiedInjectionException(key);
        }
        activate(factory, key);
        return new DefaultProvider<>(factory, message);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Provider<T> providerOrNull(Key<T> key, Object message) {
        Factory factory = factories.get(key);
        if (factory == null) {
            return null;
        }
        activate(factory, key);
        return new DefaultProvider<>(factory, message);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Supplier<T> supplier(Key<T> key) throws UnsatisfiedInjectionException {
        return factory(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Supplier<T> supplierOrNull(Key<T> key) {
        return factoryOrNull(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Polysupplier<T> polysupplier(Key<T> key) {
        Factory factory = factories.get(key);
        activate(factory, key);
        return new DefaultPolysupplier<>(factory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Factory<T> factory(Key<T> key) throws UnsatisfiedInjectionException {
        Factory factory = factories.get(key);
        if (factory == null) {
            throw new UnsatisfiedInjectionException(key);
        }
        activate(factory, key);
        return factory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Factory<T> factoryOrNull(Key<T> key) {
        Factory factory = factories.get(key);
        if (factory == null) {
            return null;
        }
        activate(factory, key);
        return factory;
    }

    protected void activate(Factory factory, Key key) {
        try {
            Factory s = factory;
            while (s != null) {
                s.activate(this);
                s = s.next();
            }
        } catch (StackOverflowError soe) {
            throw new IocException(String.format(CIRCULAR_DEP_ERR_MSG, key.toString()));
        }
    }
}
