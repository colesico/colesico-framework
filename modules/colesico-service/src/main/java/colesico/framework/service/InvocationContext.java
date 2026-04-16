/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
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

package colesico.framework.service;

/**
 * Service method invocation interception context
 */
public final class InvocationContext<T, R> {

    public static final String SERVICE_METHOD = "target";
    public static final String METHOD_NAME_METHOD = "methodName";
    public static final String PARAMETERS_METHOD = "parameters";
    public static final String PROCEED_METHOD = "proceed";

    /**
     * Target service
     */
    private final T target;

    /**
     * Target service method name
     */
    private final String methodName;

    /**
     * Method parameters
     */
    private final Object[] parameters;

    /**
     * Interceptors to be invoked to intercept method invocation
     */
    private final InterceptorsChain<T, R> interceptors;

    private InterceptorsChain.Interception<T, R, ?> interception;

    public InvocationContext(T target, String methodName, Object[] parameters, InterceptorsChain<T, R> interceptors) {
        this.target = target;
        this.methodName = methodName;
        this.parameters = parameters;
        this.interceptors = interceptors;
    }

    /**
     * The target object whose method is executed
     */
    public T target() {
        return target;
    }

    /**
     * Executing method name
     */
    public String methodName() {
        return methodName;
    }

    /**
     * Method parameters values
     */
    public Object[] parameters() {
        return parameters;
    }

    public InterceptorsChain.Interception<T, R, ?> interception() {
        return interception;
    }

    public R proceed() {
        interception = interceptors.next();
        return interception.interceptor().intercept(this);
    }


}
