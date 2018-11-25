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

package colesico.framework.teleapi;

/**
 * Tele-invocations driver.
 * Controls invocation process concerning tele driver environment (protocol etc), performs error handling.
 *
 * @author Vladlen Larionov
 */
public interface TeleDriver<R, W, I, P extends DataPort<R, W>> {

    String INVOKE_METHOD = "invoke";
    String BINDER_PARAM = "binder";
    String TARGET_PARAM = "target";
    String RESULT_PARAM = "result";

    /**
     * Performs target method tele-invocation
     * @param target
     * @param binder
     * @param context
     * @param <T>
     */
    <T> void invoke(T target, Binder<T, P> binder, I context);

    /**
     * Is used to retrieve target method parameters values from tele data port and puts back a result.
     * @param <T>
     * @param <P>
     */
    @FunctionalInterface
    interface Binder<T, P extends DataPort> {
        String TARGET_PARAM = "target";
        String DATAPORT_PARAM = "dataPort";

        void invoke(T target, P dataPort);
    }
}