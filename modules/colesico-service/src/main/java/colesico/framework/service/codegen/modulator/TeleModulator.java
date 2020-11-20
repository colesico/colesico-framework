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

package colesico.framework.service.codegen.modulator;

import colesico.framework.service.codegen.model.*;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.TeleDriver;
import colesico.framework.teleapi.TeleFacade;
import com.squareup.javapoet.CodeBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class TeleModulator<T extends TeleFacadeElement> extends Modulator {

    private final Logger log = LoggerFactory.getLogger(TeleModulator.class);

    public static final String LIGATURE_VAR = "ligature";

    abstract protected String getTeleType();

    abstract protected boolean isTeleFacadeSupported(ServiceElement serviceElm);

    abstract protected T createTeleFacade(ServiceElement serviceElm);

    /**
     * Called to process tele method after parsing completed
     */
    abstract protected void processTeleMethod(TeleMethodElement teleMethodElement);

    abstract protected CodeBlock generateLigatureMethodBody(T teleFacade);

    @Override
    public void onInitTeleFacade(ServiceElement serviceElm) {
        super.onInitTeleFacade(serviceElm);
        if (!isTeleFacadeSupported(serviceElm)) {
            return;
        }
        log.debug("Init tele-facade from modulator: {}", this.getClass().getCanonicalName());
        T teleFacade = createTeleFacade(serviceElm);
        serviceElm.setTeleFacade(teleFacade);
    }

    @Override
    public void onTeleMethodParsed(TeleMethodElement teleMethod) {
        super.onTeleMethodParsed(teleMethod);
        TeleFacadeElement teleFacade = teleMethod.getParentTeleFacade();
        if (!teleFacade.getTeleType().equals(getTeleType())) {
            return;
        }
        processTeleMethod(teleMethod);
        teleMethod.setInvocationContextCode(generateInvokingContext(teleMethod));
        teleMethod.setWritingContextCode(generateWritingContext(teleMethod));
        for (TeleParamElement teleParam : teleMethod.getParameters()) {
            teleParam.setReadingContextCode(generateReadingContext(teleParam));
        }
    }

    @Override
    public void onTeleFacadeParsed(TeleFacadeElement teleFacade) {
        super.onTeleFacadeParsed(teleFacade);
        if (!teleFacade.getTeleType().equals(getTeleType())) {
            return;
        }
        teleFacade.setLigatureMethodBody(generateLigatureMethodBody((T) teleFacade));
    }

    protected CodeBlock generateInvokingContext(TeleMethodElement teleMethod) {
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("null");
        return cb.build();
    }

    protected CodeBlock generateWritingContext(TeleMethodElement teleMethod) {
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("null");
        return cb.build();
    }

    protected CodeBlock generateReadingContext(TeleParamElement teleParam) {
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("null");
        return cb.build();
    }
}
