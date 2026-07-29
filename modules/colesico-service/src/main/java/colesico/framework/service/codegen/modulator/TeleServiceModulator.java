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
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.util.List;

/**
 * Tele-facades modulation support.
 * must be extended by any concrete tele-facades modulators.
 *
 * @see TeleFacade
 */
public abstract class TeleServiceModulator<T extends TeleServiceElement> extends Modulator {

    private final Logger log = LoggerFactory.getLogger(TeleServiceModulator.class);

    public static final String COMMANDS_VAR = "commands";
    public static final String TELE_INTERCEPTOR_FIELD = "teleInterceptor";

    /**
     * Tele-type id.
     * Usually, it is the service alias annotation.
     */
    abstract protected Class<?> teleType();

    /**
     * Checks that the modulator can handle given service to produce tele-service
     */
    abstract protected boolean isTeleServiceSupported(ServiceElement service);

    /**
     * Returns custom tele-service object for modulation process
     *
     * @see TeleServiceElement
     */
    abstract protected T createTeleService(ServiceElement serviceElm);

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
    protected void processTeleService(TeleServiceElement teleService) {

    }

    abstract protected CodeBlock generateCommandsMethodBody(T teleService);

    @Override
    public void onInitTeleService(ServiceElement serviceElm) {
        super.onInitTeleService(serviceElm);
        if (!isTeleServiceSupported(serviceElm)) {
            return;
        }
        log.debug("Init tele-facade from modulator: {}", this.getClass().getCanonicalName());
        T teleService = createTeleService(serviceElm);
        serviceElm.setTeleService(teleService);
    }

    private void createParamReadOptions(List<TeleParameterElement> params) {
        for (var param : params) {

            // Process request bean fields
            if (param instanceof TeleFieldParamElement paramBundleField) {
                if (paramBundleField.parentBean().readSpec() == null) {
                    paramBundleField.parentBean().setReadSpec(createTeleRead(paramBundleField.parentBean()));
                }
                continue;
            }

            TeleOrdinaryParamElement teleParam = (TeleOrdinaryParamElement) param;
            teleParam.setReadSpec(createTeleRead(teleParam));

        }
    }

    protected void addTeleInterception(TeleCommandElement teleCommand) {
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("$N::$N", TELE_INTERCEPTOR_FIELD, teleCommand.interceptorMethodName());
        InterceptionElement interception = new InterceptionElement(cb.build());
        teleCommand.serviceMethod().addInterception(InterceptionPhases.TELE_DATA_MAPPING, interception);
    }

    @Override
    public void onTeleCommandParsed(TeleCommandElement teleCommand) {
        super.onTeleCommandParsed(teleCommand);
        TeleServiceElement teleService = teleCommand.parentTeleService();
        if (!teleService.teleType().equals(teleType())) {
            return;
        }
        processTeleCommand(teleCommand);
        createParamReadOptions(teleCommand.parameters());
        teleCommand.setWriteSpec(createTeleWrite(teleCommand));
        addTeleInterception(teleCommand);
    }

    protected void addTeleInterceptorField(TeleServiceElement teleService) {
        FieldSpec.Builder ti = FieldSpec.builder(
                ClassName.bestGuess(teleService.interceptorClassName()),
                TELE_INTERCEPTOR_FIELD,
                Modifier.FINAL, Modifier.PRIVATE
        );
        ServiceFieldElement teleInterceptor = new ServiceFieldElement(ti.build());
        teleInterceptor.inject();
        teleService.parentService().addCustomField(teleInterceptor);
    }

    @Override
    public void onTeleServiceParsed(TeleServiceElement teleService) {
        super.onTeleServiceParsed(teleService);
        if (!teleService.teleType().equals(teleType())) {
            return;
        }
        processTeleService(teleService);
        addTeleInterceptorField(teleService);
        teleService.setCommandsMethodBody(generateCommandsMethodBody((T) teleService));
    }

    protected TeleWriteElement createTeleWrite(TeleCommandElement teleCommand) {
        CodeBlock.Builder valueTypeCode = CodeBlock.builder();
        ServiceCodegenUtils.generateTeleResultType(teleCommand, valueTypeCode);
        return new TeleWriteElement(teleCommand, valueTypeCode.build(), null);
    }

    protected TeleReadElement createTeleRead(TeleOrdinaryParamElement teleParam) {
        CodeBlock.Builder valueTypeCode = CodeBlock.builder();
        ServiceCodegenUtils.generateTeleParamType(teleParam, valueTypeCode);
        return new TeleReadElement(teleParam, valueTypeCode.build(), null);
    }

    protected TeleReadElement createTeleRead(TeleParamBundleElement paramBundle) {
        CodeBlock.Builder valueTypeCode = CodeBlock.builder();
        ServiceCodegenUtils.generateTeleParamBundleType(paramBundle, valueTypeCode);
        return new TeleReadElement(paramBundle, valueTypeCode.build(), null);
    }


}
