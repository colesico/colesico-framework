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

package colesico.framework.slf4j;

import colesico.framework.ioc.message.InjectionPoint;
import colesico.framework.ioc.message.Message;
import colesico.framework.ioc.production.Producer;
import colesico.framework.service.ServiceOrigin;
import colesico.framework.service.ServiceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Producer
public class Slf4jProducer {

    public Logger getLogger(@Message InjectionPoint ip) {
        if (ip==null){
            throw new RuntimeException("Undetermined target class for Logger injection");
        }
        if (ServiceProxy.class.isAssignableFrom(ip.getTargetClass())) {
            ServiceOrigin so = ip.getTargetClass().getAnnotation(ServiceOrigin.class);
            return LoggerFactory.getLogger(so.value());
        }
        return LoggerFactory.getLogger(ip.getTargetClass());
    }

}
