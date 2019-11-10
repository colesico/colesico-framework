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

package colesico.framework.example.transaction;

import colesico.framework.ioc.Producer;
import colesico.framework.transaction.TransactionalShell;

import javax.inject.Named;
import javax.inject.Singleton;

@Producer
public class MyProducer {

    /**
     * Define default transactional shell
     * @return
     */
    @Singleton
    public TransactionalShell getTxExec1(){
        return new TransctionalShellMock("Default");
    }

    @Singleton
    @Named("custom")
    public TransactionalShell getTxExec2(){
        return new TransctionalShellMock("Custom");
    }

    @Singleton
    @Named("alternative")
    public TransactionalShell getTxExec3(){
        return new TransctionalShellMock("MyTx...");
    }
}
