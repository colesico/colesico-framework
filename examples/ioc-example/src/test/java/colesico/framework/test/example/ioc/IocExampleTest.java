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

package colesico.framework.test.example.ioc;


import colesico.framework.example.ioc.helloworld.MainBeanHWD;
import colesico.framework.example.ioc.implement.MainBeanIMP;
import colesico.framework.example.ioc.lifecycle.MainBeanLFC;
import colesico.framework.example.ioc.logger.MainBeanLOG;
import colesico.framework.example.ioc.message.MainBeanMSG;
import colesico.framework.example.ioc.multiplugin.MainBeanMLP;
import colesico.framework.example.ioc.named.MainBeanNMD;
import colesico.framework.example.ioc.scope.*;
import colesico.framework.example.ioc.substitute.MainBeanREP;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.ioc.scope.RefreshScope;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class IocExampleTest {
    private Ioc ioc;

    @BeforeClass
    public void init() {
        ioc = IocBuilder.create().build();
    }

    @Test
    public void testHelloWorld() {
        MainBeanHWD mainBean = ioc.instance(MainBeanHWD.class);
        assertEquals(mainBean.sayHello(), "Hello");
    }

    @Test
    public void testImplement() {
        MainBeanIMP mainBean = ioc.instance(MainBeanIMP.class);
        assertEquals(mainBean.getBeanValue(), "BeanImpl");
        assertEquals(mainBean.getServiceValue(), "ServiceImpl");
    }

    @Test
    public void testLogger() {
        MainBeanLOG mainBean = ioc.instance(MainBeanLOG.class);
        assertEquals(mainBean.getLogMessage(), MainBeanLOG.class.getName() + "Logger:Message");
    }

    @Test
    public void testMessage() {
        MainBeanMSG mainBean = ioc.instance(MainBeanMSG.class);
        assertEquals(mainBean.getMessageText(), "MainBeanMSG");
    }

    @Test
    public void testMultiplugin() {
        MainBeanMLP mainBean = ioc.instance(MainBeanMLP.class);
        List<String> result = mainBean.getPluginsInfo();
        Collections.sort(result);
        assertEquals("Plugin1;Plugin2", result.get(0) + ";" + result.get(1));
    }

    @Test
    public void testNamed() {
        MainBeanNMD mainBean = ioc.instance(MainBeanNMD.class);
        assertEquals(mainBean.getNames(), "Default;Custom");
    }

    @Test
    public void testSubstitute() {
        MainBeanREP mainBean = ioc.instance(MainBeanREP.class);
        assertEquals(mainBean.getPluginInfo(), "CustomBean");
    }

    @Test
    public void testSingletonScope() {
        Singleton1 s1 = ioc.instance(Singleton1.class);
        assertEquals(s1.getMessage(), "Singleton1-0");
        s1 = ioc.instance(Singleton1.class);
        assertEquals(s1.getMessage(), "Singleton1-1");

        Singleton2 s2 = ioc.instance(Singleton2.class);
        assertEquals(s2.getMessage(), "Singleton2-0");
        s2 = ioc.instance(Singleton2.class);
        assertEquals(s2.getMessage(), "Singleton2-1");

        Singleton3 s3 = ioc.instance(Singleton3.class);
        assertEquals(s3.getMessage(), "Singleton3-0");
        s3 = ioc.instance(Singleton3.class);
        assertEquals(s3.getMessage(), "Singleton3-1");

    }

    @Test
    public void testThreadScope() {
        ThreadScoped1 ts = ioc.instance(ThreadScoped1.class);
        assertEquals(ts.getMessage(), "ThreadScoped1-0");
        //TODO: add second thread
    }

    @Test
    public void testRefreshScope() {
        RefreshScoped1 rs = ioc.instance(RefreshScoped1.class);
        assertEquals(rs.getMessage(), "RefreshScoped1-0");

        rs = ioc.instance(RefreshScoped1.class);
        assertEquals(rs.getMessage(), "RefreshScoped1-1");

        RefreshScope refreshScope = ioc.instance(RefreshScope.class);

        rs = refreshScope.refresh(RefreshScoped1.class);
        assertEquals(rs.getMessage(), "RefreshScoped1-0");

        rs = ioc.instance(RefreshScoped1.class);
        assertEquals(rs.getMessage(), "RefreshScoped1-1");
    }

    @Test
    public void testLifecycle() {
        MainBeanLFC lfc = ioc.instance(MainBeanLFC.class);
        assertEquals(lfc.getValue(), "ValueInit");
    }


}
