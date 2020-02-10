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

package colesico.framework.test.example.eventbus;

import colesico.framework.example.eventbus.Receiver;
import colesico.framework.example.eventbus.Sender;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class EventBusExampleTest {
    private Ioc ioc;

    @BeforeClass
    public void init() {
        ioc = IocBuilder.forTests().build();
     }

     @Test
     public void testEvents(){
         Sender sender = ioc.instance(Sender.class);
         sender.sendEvent();

         Receiver receiver = ioc.instance(Receiver.class);

         assertEquals(receiver.getEvent1().message,"Hello1");
         assertEquals(receiver.getEvent2().message,"Hello2");
     }

}
