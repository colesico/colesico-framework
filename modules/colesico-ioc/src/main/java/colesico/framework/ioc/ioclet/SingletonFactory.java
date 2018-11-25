/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */

package colesico.framework.ioc.ioclet;


/**
 * Factory that  provides an 'local' lazy singleton of T
 * <p>
 *
 * @author Vladlen Larionov
 */
abstract public class SingletonFactory<T> extends Factory<T> {

    public static final String INSTANCE_FIELD = "instance";
    public static final String CREATE_METHOD = "create";

    private volatile T instance = null;

    abstract protected T create(Object message);

    // Injection context ignored for singletons
    // since these instances can be injected to multiple instances
    @Override
    public final T get(Object message) {
        T i = instance;
        if (i == null) {
            synchronized (this) {
                i = instance;
                if (i == null) {
                    instance = i = create(message);
                }
            }
        }
        return i;
    }
}
