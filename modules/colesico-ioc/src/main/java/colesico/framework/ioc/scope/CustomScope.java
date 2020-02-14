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

package colesico.framework.ioc.scope;

import java.lang.annotation.*;

/**
 * Defines actual scope implementation class for scope definition annotation.
 * Used to define the custom scope annotation and specify the Scope implementation class that
 * is associated with the annotation.
 * For example, see @ThreadScoped predefined custom scope
 *
 * @see Scope
 * @see ThreadScoped
 * @author Vladlen Larionov
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
@Inherited
@Documented
public @interface CustomScope {
    /**
     * Scope intrface or implementation class
     * @return
     */
    Class<? extends Scope> value();
}
