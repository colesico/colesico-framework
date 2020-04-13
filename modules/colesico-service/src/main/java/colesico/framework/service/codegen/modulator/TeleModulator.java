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

import colesico.framework.assist.codegen.model.VarElement;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.TeleFacadeElement;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.service.codegen.model.TeleParamElement;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.TeleDriver;
import com.squareup.javapoet.CodeBlock;

import java.util.Deque;

/**
 * @param <D> Tele-Driver implementation class
 * @param <P> Data Port implementation class
 * @param <R> Data reading context
 * @param <W> Data writing context
 * @param <I> Tele-Invocation context
 * @param <M> Tele-Modulator codegen context
 * @param <L> Ligature impl class
 * @param <Q> Ioc Qualifier class for Tele-Facade producer method
 */
public abstract class TeleModulator<
        D extends TeleDriver<R, W, I, P>,
        P extends DataPort<R, W>,
        R, W, I, M,
        L, Q> extends Modulator {

    public static final String LIGATURE_VAR = "ligature";

    abstract protected String getTeleType();

    abstract protected boolean isTeleFacadeSupported(ServiceElement serviceElm);

    abstract protected Class<D> getTeleDriverClass();

    abstract protected Class<P> getDataPortClass();

    abstract protected Class<L> getLigatureClass();

    abstract protected Class<Q> getQualifierClass();

    abstract protected Class<M> getTeleModulatorContextClass();

    abstract protected M createTeleModulatorContext(ServiceElement serviceElm);

    /**
     * Called to add telemethod in context
     */
    abstract protected void addTeleMethodToContext(TeleMethodElement teleMethodElement, M teleModulatorContext);

    abstract protected CodeBlock generateLigatureMethodBody(TeleFacadeElement teleFacade);

    @Override
    public void onAddTeleFacade(ServiceElement serviceElm) {
        super.onService(serviceElm);

        if (!isTeleFacadeSupported(serviceElm)) {
            return;
        }

        TeleFacadeElement teleFacade = new TeleFacadeElement(
                getTeleType(),
                getTeleDriverClass(),
                getDataPortClass(),
                getLigatureClass(),
                TeleFacadeElement.IocQualifier.ofClassed(getQualifierClass()));
        serviceElm.addTeleFacade(teleFacade);

        M telegenContext = createTeleModulatorContext(serviceElm);
        teleFacade.setProperty(getTeleModulatorContextClass(), telegenContext);
    }

    @Override
    public void onAddTeleMethod(TeleMethodElement teleMethod) {
        super.onAddTeleMethod(teleMethod);
        TeleFacadeElement teleFacade = teleMethod.getParentTeleFacade();
        if (!teleFacade.getTeleType().equals(getTeleType())) {
            return;
        }
        M teleModulatorContext = teleFacade.getProperty(getTeleModulatorContextClass());
        addTeleMethodToContext(teleMethod, teleModulatorContext);
        teleMethod.setInvokingContext(generateInvokingContext(teleMethod));
        teleMethod.setWritingContext(generateWritingContext(teleMethod));
    }

    @Override
    public void onLinkTeleParam(TeleParamElement teleParam, Deque<VarElement> varStack) {
        super.onLinkTeleParam(teleParam, varStack);
        TeleMethodElement teleMethod = teleParam.getParentTeleMethod();
        TeleFacadeElement teleFacade = teleMethod.getParentTeleFacade();
        if (!teleFacade.getTeleType().equals(getTeleType())) {
            return;
        }
        teleParam.setReadingContext(generateReadingContext(teleParam));
    }

    @Override
    public void onTeleFacadeParsed(TeleFacadeElement teleFacade) {
        super.onTeleFacadeParsed(teleFacade);
        if (!teleFacade.getTeleType().equals(getTeleType())) {
            return;
        }
        teleFacade.setLigatureMethodBody(generateLigatureMethodBody(teleFacade));
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
