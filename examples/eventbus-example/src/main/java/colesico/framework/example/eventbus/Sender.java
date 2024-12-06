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

import colesico.framework.eventbus.TaskDispatcher;
import colesico.framework.taskhub.OnEvent;
import colesico.framework.service.Service;

import javax.inject.Inject;

@Service
public class Sender {

    final TaskDispatcher taskDispatcher;

    @Inject
    public Sender(TaskDispatcher taskDispatcher) {
        this.taskDispatcher = taskDispatcher;
    }

    public void sendEvent() {
        taskDispatcher.send(new MyEvent1("Hello1"));
        taskDispatcher.send(new MyEvent2("Hello2"));
    }

    @OnEvent
    public void onEvent2(MyEvent2 event) {
        System.out.println("Sender on MyEvent2: " + event.message);
    }
}
