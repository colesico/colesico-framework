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

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.service.codegen.model.*;
import colesico.framework.teleapi.TeleFacade;
import com.squareup.javapoet.CodeBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.type.TypeMirror;

/**
 * Tele-facades modulation support.
 * must be extended by any concrete tele-facades modulators.
 *
 * @see TeleFacade
 */
public abstract class TeleModulator<T extends TeleFacadeElement> extends Modulator {

    private final Logger log = LoggerFactory.getLogger(TeleModulator.class);

    public static final String LIGATURE_VAR = "ligature";

    /**
     * Tele type id.
     * Usually, it is the service alias annotation.
     */
    abstract protected Class<?> getTeleType();

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
     * Called to process tele method after parsing completed.
     * Override this method to custom processing.
     */
    protected void processTeleMethod(TeleMethodElement teleMethod) {

    }

    /**
     * Called to process tele facade after parsing completed.
     * Override this method to custom processing.
     */
    protected void processTeleFacade(TeleFacadeElement teleFacade) {

    }

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
        for (TeleParamElement teleParam : teleMethod.getParameters()) {
            teleParam.setReadingContextCode(generateReadingContext(teleParam));
        }
        teleMethod.setInvocationContextCode(generateInvocationContext(teleMethod));
        teleMethod.setWritingContextCode(generateWritingContext(teleMethod));
    }

    @Override
    public void onTeleFacadeParsed(TeleFacadeElement teleFacade) {
        super.onTeleFacadeParsed(teleFacade);
        if (!teleFacade.getTeleType().equals(getTeleType())) {
            return;
        }
        processTeleFacade(teleFacade);
        teleFacade.setLigatureMethodBody(generateLigatureMethodBody((T) teleFacade));
    }

    protected CodeBlock generateInvocationContext(TeleMethodElement teleMethod) {
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("null");
        return cb.build();
    }

    protected void generateResultType(TeleMethodElement teleMethod, CodeBlock.Builder cb) {
        TypeMirror returnType = teleMethod.getServiceMethod().getOriginMethod().getReturnType();
        CodegenUtils.generateTypePick(returnType, cb);
    }

    protected CodeBlock generateWritingContext(TeleMethodElement teleMethod) {
        CodeBlock.Builder cb = CodeBlock.builder();
        generateResultType(teleMethod, cb);
        return cb.build();
    }

    protected void generateParamType(TeleParamElement teleParam, CodeBlock.Builder cb) {
        // Detect param type considering generics
        TypeMirror paramType = teleParam.getOriginParam().getOriginType();
        // ParamType.class or  for generics: new TypeWrapper<TheType>(){}.unwrap()
        CodegenUtils.generateTypePick(paramType, cb);
    }

    protected CodeBlock generateReadingContext(TeleParamElement teleParam) {
        CodeBlock.Builder cb = CodeBlock.builder();
        generateParamType(teleParam, cb);
        return cb.build();
    }
}
