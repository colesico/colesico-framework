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

package colesico.framework.example.jdbi;

import colesico.framework.config.Config;
import colesico.framework.ioc.production.Classed;
import colesico.framework.jdbi.JdbiOptionsPrototype;
import org.jdbi.v3.core.Jdbi;

/**
 * This config allow to set up jdbi extra configuration options.
 */
@Config
@Classed(JdbiConfig.class)
public class JdbiOptions extends JdbiOptionsPrototype {

    @Override
    public void applyOptions(Jdbi jdbi) {
        System.out.println("JDBI internal config: "+jdbi.getConfig());
    }
}
