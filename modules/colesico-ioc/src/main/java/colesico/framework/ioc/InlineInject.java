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

package colesico.framework.ioc;

import javax.inject.Qualifier;
import java.lang.annotation.*;


/**
 * This annotation should be used in case of cyclic dependencies. Inline injection
 * has slightly lower performance and does not participate in circular dependencies checking.
 * The  dependencies of the instance factory will be obtained from the IoC container
 * at the instance production and not at the factory activation.
 */
@Qualifier
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface InlineInject {
}
