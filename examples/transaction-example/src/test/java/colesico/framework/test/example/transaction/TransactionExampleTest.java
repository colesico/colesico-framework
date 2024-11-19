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

package colesico.framework.test.example.transaction;

import colesico.framework.example.transaction.AppService;
import colesico.framework.example.transaction.TextBuffer;
import colesico.framework.example.transaction.TransctionalShellMock;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.ioc.key.NamedKey;
import colesico.framework.transaction.TransactionalShell;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TransactionExampleTest {

    Ioc ioc;
    TransctionalShellMock defaultTxShell;
    TransctionalShellMock customTxShell;
    TransctionalShellMock progTxShell;

    AppService service;

    TextBuffer result = TextBuffer.INSTANCE;


    @BeforeClass
    public void init() {
        ioc = IocBuilder.create().build();
        defaultTxShell = (TransctionalShellMock) ioc.instance(TransactionalShell.class);
        customTxShell = (TransctionalShellMock) ioc.instance(new NamedKey<>(TransactionalShell.class, "custom"), null);
        progTxShell = (TransctionalShellMock) ioc.instance(new NamedKey<>(TransactionalShell.class, "prog"), null);
        service = ioc.instance(AppService.class);
    }

    @Test
    public void testSave() throws Exception {
        result.reset();
        service.save("value");
        assertEquals(result.getValue(), "(RequiresNew-begin-Default)(save=value)(RequiresNew-end-Default)");
    }

    @Test
    public void testUpdate() throws Exception {
        result.reset();
        customTxShell.required(() -> service.update("value"));
        assertEquals(result.getValue(), "(Required-begin-Custom)(Mandatory-begin-Custom)(update=value)(Mandatory-end-Custom)(Required-end-Custom)");
    }

    @Test
    public void testDelete() throws Exception {
        result.reset();
        service.delete("value");
        assertEquals(result.getValue(), "(Required-begin-Programmatic)(delete=value)(Required-end-Programmatic)");
    }

    @Test
    public void testUpdate2() throws Exception {
        result.reset();
        service.update2("value");
        assertEquals(result.getValue(), "(Required-begin-Programmatic)(update2=value)(Required-end-Programmatic)");
    }

    @Test
    public void testCreate() throws Exception {
        result.reset();
        service.create("value");
        assertEquals(result.getValue(), "(Required-begin-Programmatic)(Never-begin-Programmatic)(create=value)(Never-end-Programmatic)(Required-end-Programmatic)");
    }
}
