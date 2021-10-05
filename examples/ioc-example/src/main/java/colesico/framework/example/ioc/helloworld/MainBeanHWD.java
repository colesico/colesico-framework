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

package colesico.framework.example.ioc.helloworld;

/**
 * This bin can be obtained from the IoC container:
 * <code>
 * MainBeanHWD mainBean = ioc.instance(MainBeanHWD.class);
 * </code>
 */
public class MainBeanHWD {

    private final HelloBean helloService;

    /**
     * Annotation {@link javax.inject.Inject}
     * is not needed because the constructor is the only one
     */
    public MainBeanHWD(HelloBean helloService) {
        this.helloService = helloService;
    }

    public String sayHello() {
        return helloService.sayHello();
    }
}
