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

package colesico.framework.ioc.ioclet;

import colesico.framework.ioc.Polyproduce;
import colesico.framework.ioc.Polysupplier;
import colesico.framework.ioc.Supplier;

import javax.inject.Provider;

/**
 * Factory superclass. Should be expanded by all the factories.
 * Factory "supplies" the instance of class T for IoC container. T
 * he factories are provided for the IoC container with IoC container modules - ioclets
 * <p>
 *
 * @author Vladlen Larionov
 * @see Ioclet
 */
abstract public class Factory<T> implements Provider<T>, Supplier<T> {

    public static final String GET_METHOD = "get";
    public static final String SETUP_METHOD = "setup";
    public static final String MESSAGE_PARAM = "message";
    public static final String IOC_PARAM = "ioc";

    private volatile boolean inactive = true;

    private Factory<T> next = null;

    /**
     * Returns instance to be injected
     */
    abstract public T get(Object message);

    @Override
    public T get() {
        return get(null);
    }

    /**
     * Returns next factory in the chain
     *
     * @see Polysupplier
     * @see Polyproduce
     */
    public final Factory<T> next() {
        return next;
    }

    /**
     * Called by Ioc container to initialize factory
     * (perform obtaining dependencies from IoC container)
     */
    public void setup(final AdvancedIoc ioc) {
    }

    public final void setNext(Factory<T> next) {
        this.next = next;
    }

    public final void activate(final AdvancedIoc ioc) {
        boolean i = inactive;
        if (i) {
            synchronized (this) {
                i = inactive;
                if (i) {
                    inactive = false;
                    setup(ioc);
                }
            }
        }
    }
}
