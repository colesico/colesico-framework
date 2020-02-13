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

package colesico.framework.jdbi.internal;

import colesico.framework.ioc.annotation.Classed;
import colesico.framework.ioc.annotation.Message;
import colesico.framework.ioc.annotation.Producer;
import colesico.framework.ioc.annotation.Unscoped;
import colesico.framework.jdbi.JdbiConfigPrototype;
import org.jdbi.v3.core.Jdbi;



@Producer(MinorTag.class)
public class JdbiProducer {

    /**
     * Jdbi factory
     * Creates Jdbi instance configured with settings
     *
     * @param config
     * @return
     */
    @Unscoped
    @Classed(JdbiConfigPrototype.class)
    public Jdbi jdbiFactory(@Message JdbiConfigPrototype config) {
        final Jdbi jdbi = Jdbi.create(config.getDataSource());
        jdbi.installPlugins();
        if (config.getOptions() != null) {
            config.getOptions().forEach(o -> o.applyOptions(jdbi), null);
        }
        return jdbi;
    }

}
