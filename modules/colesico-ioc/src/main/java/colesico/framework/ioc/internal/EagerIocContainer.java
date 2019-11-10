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

import colesico.framework.ioc.Key;
import colesico.framework.ioc.Polysupplier;
import colesico.framework.ioc.Supplier;
import colesico.framework.ioc.UnsatisfiedInjectionException;
import colesico.framework.ioc.ioclet.*;

import javax.inject.Provider;
import java.util.Map;

/**
 * Ioc containter implementation for production.
 * Requires factories pre-activation after container creation.
 *
 * @author Vladlen Larionov
 */
public final class EagerIocContainer implements AdvancedIoc {

    private final Map<Key<?>, Factory<?>> factories;

    public EagerIocContainer(Map<Key<?>, Factory<?>> factories) {
        this.factories = factories;
    }

    @Override
    public <T> T instance(Key<T> key, Object message) throws UnsatisfiedInjectionException {
        Factory<T> factory = (Factory<T>) factories.get(key);
        if (factory == null) {
            throw new UnsatisfiedInjectionException(key);
        }
        return factory.get(message);
    }

    @Override
    public <T> T instanceOrNull(Key<T> key, Object message) {
        Factory<T> factory = (Factory<T>) factories.get(key);
        if (factory == null) {
            return null;
        }
        return factory.get(message);
    }

    @Override
    public <T> Provider<T> provider(Key<T> key, Object message) throws UnsatisfiedInjectionException {
        Factory<T> factory = (Factory<T>) factories.get(key);
        if (factory == null) {
            throw new UnsatisfiedInjectionException(key);
        }
        return new DefaultProvider<>(factory, message);
    }

    @Override
    public <T> Provider<T> providerOrNull(Key<T> key, Object message) {
        Factory<T> factory = (Factory<T>) factories.get(key);
        if (factory == null) {
            return null;
        }
        return new DefaultProvider<>(factory, message);
    }

    @Override
    public <T> Supplier<T> supplier(Key<T> key) throws UnsatisfiedInjectionException {
        Factory<T> factory = (Factory<T>) factories.get(key);
        if (factory == null) {
            throw new UnsatisfiedInjectionException(key);
        }
        return factory;
    }

    @Override
    public <T> Supplier<T> supplierOrNull(Key<T> key) {
        return (Factory<T>) factories.get(key);
    }

    @Override
    public <T> Polysupplier<T> polysupplier(Key<T> key) {
        Factory<T> factory = (Factory<T>) factories.get(key);
        return new DefaultPolysupplier<>(factory);
    }

    @Override
    public <T> Factory<T> factory(Key<T> key) throws UnsatisfiedInjectionException {
        Factory<T> factory = (Factory<T>) factories.get(key);
        if (factory == null) {
            throw new UnsatisfiedInjectionException(key);
        }
        return factory;
    }

    @Override
    public <T> Factory<T> factoryOrNull(Key<T> key) {
        return (Factory<T>) factories.get(key);
    }

}
