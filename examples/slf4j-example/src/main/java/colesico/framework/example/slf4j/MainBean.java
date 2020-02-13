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

package colesico.framework.example.slf4j;

import colesico.framework.ioc.annotation.Contextual;
import colesico.framework.service.Service;
import org.slf4j.Logger;

/**
 * Logger will be injected to this service bean
 */
@Service
public class MainBean {

    private final Logger logger;

    /**
     * Contextual annotation is used to pass injection point information to logger factory
     * to produce specific logger
     * @param logger
     */
    public MainBean(@Contextual Logger logger) {
        this.logger = logger;
    }

    public void logMessage(String message) {
        logger.info(message);
    }
}
