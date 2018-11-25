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

package colesico.framework.service;

/**
 * @author Vladlen Larionov
 */
public final class InvocationContext<T, R> {

    public static final String GET_SERVICE_METHOD = "getTarget";
    public static final String GET_METHOD_NAME_METHOD = "getMethodName";
    public static final String GET_PARAMETERS_METHOD = "getParameters";
    public static final String PROCEED_METHOD = "proceed";

    private final T target;
    private final String methodName;
    private final Object[] parameters;
    private final InterceptorsChain<T,R> interceptors;

    private InterceptorsChain.Interception<T,R,Object> interception;

    public InvocationContext(T target, String methodName, Object[] parameters, InterceptorsChain<T,R> interceptors) {
        this.target = target;
        this.methodName = methodName;
        this.parameters = parameters;
        this.interceptors = interceptors;
    }

    /**
     * The target object whose method is executed
     *
     * @return
     */
    public T getTarget() {
        return target;
    }

    /**
     * Executing method name
     *
     * @return
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Method parameters values
     *
     * @return
     */
    public Object[] getParameters() {
        return parameters;
    }

    public InterceptorsChain.Interception getInterception() {
        return interception;
    }

    public R proceed() {
        interception = interceptors.next();
        return (R) interception.getInterceptor().intercept(this);
    }


}
