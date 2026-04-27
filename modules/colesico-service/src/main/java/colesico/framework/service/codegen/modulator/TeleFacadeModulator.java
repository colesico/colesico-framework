/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
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

import colesico.framework.service.codegen.assist.ServiceCodegenUtils;
import colesico.framework.service.codegen.model.*;
import colesico.framework.service.codegen.model.teleapi.*;
import colesico.framework.teleapi.TeleFacade;
import com.palantir.javapoet.CodeBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Tele-facades modulation support.
 * must be extended by any concrete tele-facades modulators.
 *
 * @see TeleFacade
 */
public abstract class TeleFacadeModulator<T extends TeleFacadeElement> extends Modulator {

    private final Logger log = LoggerFactory.getLogger(TeleFacadeModulator.class);

    public static final String COMMANDS_VAR = "commands";

    /**
     * Tele-type id.
     * Usually, it is the service alias annotation.
     */
    abstract protected Class<?> teleType();

    /**
     * Checks that the modulator can handle given service to produce tele-facade
     */
    abstract protected boolean isTeleFacadeSupported(ServiceElement service);

    /**
     * Returns custom tele-facade object for modulation process
     *
     * @see TeleFacade
     */
    abstract protected T createTeleFacade(ServiceElement serviceElm);

    /**
     * Called to process tele-command after parsing completed.
     * Override this method to custom processing.
     */
    protected void processTeleCommand(TeleCommandElement teleCommand) {

    }

    /**
     * Called to process tele facade after parsing completed.
     * Override this method to custom processing.
     */
    protected void processTeleFacade(TeleFacadeElement teleFacade) {

    }

    abstract protected CodeBlock generatecommandsMethodBody(T teleFacade);

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

    private void createParamReadContexts(List<TeleInputElement> params) {
        for (var param : params) {

            // Skip batch fields
            if (param instanceof TeleBatchFieldElement) {
                TeleBatchFieldElement batchField = (TeleBatchFieldElement) param;
                if (batchField.parentBatch().readContext() == null) {
                    batchField.parentBatch().setReadContext(createReadContext(batchField.parentBatch()));
                }
                continue;
            }

            TeleParameterElement teleParam = (TeleParameterElement) param;
            teleParam.setReadContext(createReadContext(teleParam));

        }
    }

    @Override
    public void onTeleCommandParsed(TeleCommandElement teleCommand) {
        super.onTeleCommandParsed(teleCommand);
        TeleFacadeElement teleFacade = teleCommand.parentTeleFacade();
        if (!teleFacade.teleType().equals(teleType())) {
            return;
        }
        processTeleCommand(teleCommand);
        createParamReadContexts(teleCommand.parameters());
        teleCommand.setInvocationContext(createInvocationContext(teleCommand));
        teleCommand.setWritingContext(createWriteContext(teleCommand));
    }

    @Override
    public void onTeleFacadeParsed(TeleFacadeElement teleFacade) {
        super.onTeleFacadeParsed(teleFacade);
        if (!teleFacade.teleType().equals(teleType())) {
            return;
        }
        processTeleFacade(teleFacade);
        teleFacade.setCommandsMethodBody(generatecommandsMethodBody((T) teleFacade));
    }

    protected TIContextElement createInvocationContext(TeleCommandElement teleCommand) {
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("null");
        return new TIContextElement(teleCommand, cb.build());
    }

    protected TWContextElement createWriteContext(TeleCommandElement teleCommand) {
        CodeBlock.Builder cb = CodeBlock.builder();
        ServiceCodegenUtils.generateTeleResultType(teleCommand, cb);
        return new TWContextElement(teleCommand, cb.build());
    }

    protected TRContextElement createReadContext(TeleParameterElement teleParam) {
        CodeBlock.Builder cb = CodeBlock.builder();
        ServiceCodegenUtils.generateTeleInputType(teleParam, cb);
        return new TRContextElement(teleParam, cb.build());
    }

    protected TRContextElement createReadContext(TeleBatchElement teleBatch) {
        CodeBlock.Builder cb = CodeBlock.builder();
        ServiceCodegenUtils.generateTeleBatchType(teleBatch, cb);
        return new TRContextElement(teleBatch, cb.build());
    }
}
