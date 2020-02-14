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

package colesico.framework.example.ioc;

import colesico.framework.example.ioc.message.ReceiverBean;
import colesico.framework.example.ioc.message.TextMessage;
import colesico.framework.example.ioc.multiplugin.PluginInterface;
import colesico.framework.example.ioc.named.NamedBean;
import colesico.framework.example.ioc.singleton.Singleton1;
import colesico.framework.example.ioc.singleton.Singleton2;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.ioc.key.NamedKey;
import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.ioc.production.Supplier;

public class Main {
    public static void main(String[] args) {
        Ioc ioc = IocBuilder.forProduction().build();

        Singleton1 s1 = ioc.instance(Singleton1.class);
        System.out.println(s1.getMessage());

        Singleton2 s2 = ioc.instance(Singleton2.class);
        System.out.println(s2.getMessage());

        NamedBean nb = ioc.instance(new NamedKey<>(NamedBean.class, "custom"), null);
        System.out.println(nb.getName());

        Supplier<ReceiverBean> rbs = ioc.supplier(ReceiverBean.class);
        TextMessage msg = new TextMessage("TextMessage!");
        System.out.println(rbs.get(msg).getMessage());

        Polysupplier<PluginInterface> pps = ioc.polysupplier(PluginInterface.class);
        pps.forEach(p->System.out.println(p.getInfo()),null);


    }
}
