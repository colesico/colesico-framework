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

package colesico.framework.rpc.codegen.parser;

import colesico.framework.rpc.Remote;
import colesico.framework.rpc.codegen.model.RpcModulatorContext;
import colesico.framework.rpc.teleapi.*;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.TeleFacadeElement;
import colesico.framework.service.codegen.modulator.TeleModulator;
import com.squareup.javapoet.CodeBlock;

import java.lang.annotation.Annotation;

public class RpcModulator extends
        TeleModulator<RpcTeleDriver, RpcDataPort, RpcTDRContext, RpcTDWContext, RpcTIContext, RpcModulatorContext,RpcLigature,Remote> {

    @Override
    protected Class<? extends Annotation> getTeleAnnotation() {
        return Remote.class;
    }

    @Override
    protected String getTeleType() {
        return Remote.class.getSimpleName();
    }

    @Override
    protected Class<RpcTeleDriver> getTeleDriverClass() {
        return RpcTeleDriver.class;
    }

    @Override
    protected Class<RpcDataPort> getDataPortClass() {
        return RpcDataPort.class;
    }

    @Override
    protected Class<RpcLigature> getLigatureClass() {
        return null;
    }

    @Override
    protected Class<Remote> getQualifierClass() {
        return null;
    }

    @Override
    protected Class<RpcModulatorContext> getTeleModulatorContextClass() {
        return null;
    }

    @Override
    protected RpcModulatorContext createTeleModulatorContext(ServiceElement serviceElm) {
        return null;
    }

    @Override
    protected CodeBlock generateLigatureMethodBody(TeleFacadeElement teleFacade) {
        return null;
    }


}
