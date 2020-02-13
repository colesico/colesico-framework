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

package colesico.framework.ioc.tag;

/**
 * Depending on various conditions determines the possibility of using an IoC factory in the IoC container.
 */
public interface Tag {

    /**
     * Determines that the corresponding IoC factory can be used in the IoC container to produce instances.
     */
    boolean isEnabled();

    /**
     * Decides whether a given ioclet can replace other
     */
    boolean canReplace(Tag other);

}
