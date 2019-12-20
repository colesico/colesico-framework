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

package colesico.framework.ioc;

import javax.inject.Qualifier;
import java.lang.annotation.*;

/**
 * Use this annotation to pass InjectionPoint to instance factory.
 * This annotation is used to mark the constructor parameter to pass the information about the class in which this parameter is injected.
 * This information is used while the parameter instance been created.
 * For example this annotation should be used to context dependent logger injection.
 *
 *
 * @see InjectionPoint
 */
@Qualifier
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Contextual {
}
