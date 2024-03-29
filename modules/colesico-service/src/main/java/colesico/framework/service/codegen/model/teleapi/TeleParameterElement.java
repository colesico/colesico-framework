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

package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.codegen.model.VarElement;

/**
 * Tele-method  parameter that can be directly read from data port
 */
public final class TeleParameterElement extends TeleEntryElement implements TeleInputElement {

    protected TRContextElement readingContext;

    public TeleParameterElement(TeleMethodElement parentTeleMethod, VarElement originElement) {
        super(parentTeleMethod, originElement);
    }

    public TRContextElement getReadingContext() {
        return readingContext;
    }

    public void setReadingContext(TRContextElement readingContext) {
        this.readingContext = readingContext;
    }

    @Override
    public String toString() {
        return "TeleParamElement{" +
                "originParam=" + originElement +
                '}';
    }
}
