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

package colesico.framework.rpc;

import java.lang.annotation.*;

/**
 * Remote procedure call interface definition for services
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface RpcApi {

    String DEFAULT_NAMESPACE = "rpc";

    /**
     * RPC name
     * If not defined assumed as rpc api interface canonical name
     */
    String name() default "";

    /**
     * RPC rpcNamespace
     */
    String namespace() default DEFAULT_NAMESPACE;
}
