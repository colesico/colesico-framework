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

package colesico.framework.example.transaction;

import colesico.framework.service.Service;
import colesico.framework.transaction.TransactionPropagation;
import colesico.framework.transaction.Transactional;
import colesico.framework.transaction.TransactionalShell;

import javax.inject.Named;

/**
 * An application service.
 * This service implicitly uses default and "custom" transactional shells
 * and explicitly uses "prog" shell for programmatic transaction control.
 * Each method demonstrates the way the tx control can be used.
 */
@Service
public class AppService {

    TextBuffer out = TextBuffer.INSTANCE;

    /**
     * Shell to be used in programmatic tx control manner
     */
    private final TransactionalShell<Object> progShell;

    public AppService(@Named("prog") TransactionalShell progShell) {
        this.progShell = progShell;
    }

    /**
     * Declarative transaction control style with default transactional shell
     *
     * @param value
     * @return
     */
    @Transactional(propagation = TransactionPropagation.REQUIRES_NEW)
    public Boolean save(String value) {
        out.append("save=" + value);
        return Boolean.TRUE;
    }

    /**
     * Declarative  transaction control style with custom transactional shell.
     * shell = "custom" refers to @Named("custom") TransactionalSell in the producer
     *
     * @param value
     * @return
     */
    @Transactional(shell = "custom",propagation = TransactionPropagation.MANDATORY)
    public Boolean update(String value) {
        out.append("update=" + value);
        return Boolean.TRUE;
    }

    /**
     * Programmatic transaction control with "prog" transactional shell
     */
    public Boolean delete(String value) {
        return progShell.required(() -> {
            out.append("delete=" + value);
            return Boolean.FALSE;
        });
    }

    /**
     * Declarative  transaction control  with "prog" transactional shell.
     */
    @Transactional(shell = "prog")
    public Boolean update2(String value) {
        out.append("update2=" + value);
        return Boolean.TRUE;
    }

    /**
     * Mixed programmatic  and declarative transaction control style
     */
    @Transactional(shell = "prog")
    public Boolean create(String value) {
        return progShell.never(() -> {
            out.append("create=" + value);
            return Boolean.FALSE;
        });
    }


}
