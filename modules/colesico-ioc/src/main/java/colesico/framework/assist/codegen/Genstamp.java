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

package colesico.framework.assist.codegen;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Genstamp annotations is used to add to the generated class the meta information regarding generation
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Genstamp {

    /**
     * Code generator class name
     * @return
     */
    String generator();

    /**
     * When the code was generated
     * @return
     */
    String timestamp();

    /**
     * The "hash" of the generated code to control changes
     * @return
     */
    String hashId();

    /**
     * Any comments string
     * @return
     */
    String comments() default "";
}
