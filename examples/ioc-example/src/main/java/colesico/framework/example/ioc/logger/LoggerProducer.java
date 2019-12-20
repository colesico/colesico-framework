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

package colesico.framework.example.ioc.logger;


import colesico.framework.ioc.InjectionPoint;
import colesico.framework.ioc.Message;
import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;

@Producer
@Produce(MainBeanLOG.class)
public class LoggerProducer {

    public Logger getLogger(@Message InjectionPoint ip) {
        if (ip == null) {
            return new Logger("NonameLogger");
        }
        return new Logger(ip.getTargetClass().getName() + "Logger");
    }
}
