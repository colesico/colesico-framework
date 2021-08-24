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

package colesico.framework.teleapi;

/**
 * Controls invocation process considering tele-driver environment (protocol etc.), performs error handling, etc.
 *
 * @param <R> Data reading context
 * @param <W> Data writing context
 * @param <I> Target (service) invocation context
 * @param <P> Data port
 */
public interface TeleDriver<R extends TRContext, W extends TWContext, I, P extends DataPort<R, W>> {

    String INVOKE_METHOD = "invoke";
    String INVOKER_PARAM = "invoker";
    String TARGET_PARAM = "target";
    String RESULT_PARAM = "result";

    /**
     * Performs target method tele-invocation
     *
     * @param target
     * @param invoker
     * @param context
     * @param <T>
     */
    <T> void invoke(T target, MethodInvoker<T, P> invoker, I context);

}