/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.example.ioc.named;

import javax.inject.Named;

public class MyBeanHolder {

    private final MyBean defaultBean;
    private final MyBean customBean;

    public MyBeanHolder(@Named("default") MyBean defaultBean,@Named("custom") MyBean customBean) {
        this.defaultBean = defaultBean;
        this.customBean = customBean;
    }

    public void print(){
        System.out.print("Default bean: ");
        defaultBean.printHello();

        System.out.print("Custom bean: ");
        customBean.printHello();
    }
}
