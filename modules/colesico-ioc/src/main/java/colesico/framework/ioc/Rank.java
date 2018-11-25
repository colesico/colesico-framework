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

package colesico.framework.ioc;

/**
 * Standard ranks of ioclets. Ranks are used to prioritize/disabling the using of the ioclets in the IoC container.
 * By default, the ranks are constructed by IocBuilder in the following order:
 * test-extension-default-minor. 'minor' is the least priority, ' test' - the highest.
 * IocBuilder can provide any ranks order, including any custom ranks.
 * <p>
 *
 * @author Vladlen Larionov
 * @see IocBuilder
 */
public interface Rank {
    String RANK_MINOR = "minor";           // for low priority producers
    String RANK_DEFAULT = "default";       // for default producers
    String RANK_EXTENSION = "extension";   // for extension producers  (plugins, etc)
    String RANK_TEST = "test";             // for test producers
}
