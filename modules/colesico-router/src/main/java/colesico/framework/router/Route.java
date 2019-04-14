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

package colesico.framework.router;

import java.lang.annotation.*;

/**
 * Route customization annotation.
 *
 * This annotation can be used on:
 *  - package (package-info)
 *  - controller class  (implemented by weblet,restlet, etc)
 *  - request handler method
 *
 *  Package based rout should always be an absolute path ans starts with "/".
 *
 *  Controller based route can be absolute or relative.
 *  Controller  absolute route starts with "/".
 *  Relative route  starts with dot (.)
 *
 *  Handler method route considered always to be a relative path.
 *  The slash symbol at the beginning of the route is ignored.
 *
 *  The relative path is added to the parent path.
 *  The local relative path for the handler method  is added to the path specified for the
 *  controller class. And further, respectively, to the path for the package.
 *
 * @author Vladlen Larionov
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE,ElementType.PACKAGE})
@Inherited
@Documented
public @interface Route {
    String value();
}
