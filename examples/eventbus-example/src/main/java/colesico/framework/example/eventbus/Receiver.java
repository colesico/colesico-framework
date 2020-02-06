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

package colesico.framework.example.eventbus;

import colesico.framework.eventbus.OnEvent;
import colesico.framework.service.Service;

@Service
public class Receiver {

    private MyEvent1 event1;
    private MyEvent2 event2;

    @OnEvent
    public void onEvent1(MyEvent1 event) {
        System.out.println("Receiver on MyEvent1: " + event.message);
        this.event1 = event;
    }

    @OnEvent
    public void onEvent2(MyEvent2 event) {
        System.out.println("Receiver on MyEvent2: " + event.message);
        this.event2 = event;
    }

    public MyEvent1 getEvent1() {
        return event1;
    }

    public MyEvent2 getEvent2() {
        return event2;
    }
}
