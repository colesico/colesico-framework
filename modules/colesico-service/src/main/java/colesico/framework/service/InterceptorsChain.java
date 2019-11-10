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

package colesico.framework.service;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 *
 */
public final class InterceptorsChain<T, R> {

    private Queue<Interception> queue = new ArrayDeque();

    public <P> void add(Interceptor<T, R> interceptor, P parameters) {
        queue.add(new Interception(interceptor, parameters));
    }

    public Interception next() {
        return queue.poll();
    }

    public static final class Interception<T, R, P> {
        private final Interceptor<T, R> interceptor;
        private final P parameters;

        public Interception(Interceptor<T, R> interceptor, P parameters) {
            this.interceptor = interceptor;
            this.parameters = parameters;
        }

        public Interceptor getInterceptor() {
            return interceptor;
        }

        public P getParameters() {
            return parameters;
        }
    }
}
