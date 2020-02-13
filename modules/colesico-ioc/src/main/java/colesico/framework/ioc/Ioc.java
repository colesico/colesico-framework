/*
 * Copyright © 2014-2020 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.ioc;

import colesico.framework.ioc.exception.UnsatisfiedInjectionException;
import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;

import javax.inject.Provider;

/**
 * Inversion of control container  (IoC container)
 * Used to create instances with the injected dependencies.
 * <p>
 *
 * @author Vladlen Larionov
 */
public interface Ioc {

    String INSTANCE_METHOD = "instance";
    String PROVIDER_METHOD = "provider";
    String SUPPLIER_METHOD = "supplier";
    String POLYSUPPLIER_METHOD = "polysupplier";

    /**
     * Returns instance of T by its class.
     * <p>
     *
     * @param message the message that can be used by factory that creates the instance
     * @return the instance if the appropriate factory is found, exception otherwise
     * @see TypeKey
     */
    <T> T instance(Key<T> key, Object message) throws UnsatisfiedInjectionException;

    /**
     * Returns instance of T by its class or null if the appropriate factory is not found.
     */
    <T> T instanceOrNull(Key<T> key, Object message);

    /**
     * Returns provider for instance of T by key
     *
     * @return the provider if the appropriate factory is found, exception otherwise
     */
    <T> Provider<T> provider(Key<T> key, Object message) throws UnsatisfiedInjectionException;

    /**
     * Returns provider if exists or null if the appropriate factory is not found.
     *
     * @param key
     * @param message
     * @param <T>
     * @return
     */
    <T> Provider<T> providerOrNull(Key<T> key, Object message);

    /**
     * Returns supplier by key
     *
     * @param key
     * @param <T>
     * @return
     * @see Supplier
     */
    <T> Supplier<T> supplier(Key<T> key) throws UnsatisfiedInjectionException;

    /**
     * Returns supplier if exists or null if the appropriate factory is not found.
     *
     * @param key
     * @param <T>
     * @return
     */
    <T> Supplier<T> supplierOrNull(Key<T> key);


    /**
     * Returns polysupplier by key.
     * If there is no instance providers returns empty polysupplier.
     *
     * @param key
     * @param <T>
     * @return polysupplier
     */
    <T> Polysupplier<T> polysupplier(Key<T> key);

    /**
     * Shortcut method
     * The method uses a TypeKey and null injection message to obtain an instance.
     *
     * @param type
     * @param <T>
     * @return
     */
    default <T> T instance(Class<T> type) throws UnsatisfiedInjectionException {
        return instance(new TypeKey<>(type), null);
    }

    /**
     * Shortcut method
     * The method uses a TypeKey and null injection message to obtain a provider.
     *
     * @param type
     * @param <T>
     * @return
     */
    default <T> Provider<T> provider(Class<T> type) throws UnsatisfiedInjectionException {
        return provider(new TypeKey<>(type), null);
    }

    /**
     * Shortcut method
     * @param type
     * @param <T>
     * @return
     * @throws UnsatisfiedInjectionException
     */
    default <T> Supplier<T> supplier(Class<T> type) throws UnsatisfiedInjectionException {
        return supplier(new TypeKey<>(type));
    }

    /**
     * Shortcut method
     * The method uses a TypeKey and null injection message to obtain a plyprovider.
     *
     * @return
     */
    default <T> Polysupplier<T> polysupplier(Class<T> type) {
        return polysupplier(new TypeKey<>(type));
    }
}
