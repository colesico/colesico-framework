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

package colesico.framework.rpc.codegen;

import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.rpc.Remote;
import colesico.framework.rpc.codegen.model.RpcModulatorContext;
import colesico.framework.rpc.codegen.model.RpcTeleMethodElement;
import colesico.framework.rpc.teleapi.*;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.TeleFacadeElement;
import colesico.framework.service.codegen.model.TeleParamElement;
import colesico.framework.service.codegen.modulator.TeleModulator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;

public class RpcModulator extends
        TeleModulator<RpcTeleDriver, RpcDataPort, RpcTDRContext, RpcTDWContext, RpcTIContext, RpcModulatorContext, RpcLigature, Remote> {

    @Override
    protected String getTeleType() {
        return Remote.class.getSimpleName();
    }

    @Override
    protected boolean isTeleFacadeSupported(ServiceElement serviceElm) {
        List<ClassType> allInterfaces = serviceElm.getOriginClass().getInterfaces();
        for (ClassType iface : allInterfaces) {
            if (null != iface.asElement().getAnnotation(Remote.class)) {
                return true;
            }
        }
        return false;
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
        return RpcLigature.class;
    }

    @Override
    protected Class<Remote> getQualifierClass() {
        return Remote.class;
    }

    @Override
    protected Class<RpcModulatorContext> getTeleModulatorContextClass() {
        return RpcModulatorContext.class;
    }

    @Override
    protected RpcModulatorContext createTeleModulatorContext(ServiceElement serviceElm) {
        ClassElement serviceClass = serviceElm.getOriginClass();
        List<ClassType> allInterfaces = serviceClass.getInterfaces();
        List<ClassType> rpcInterfaces = new ArrayList<>();
        for (ClassType iface : allInterfaces) {
            if (null != iface.asElement().getAnnotation(Remote.class)) {
                rpcInterfaces.add(iface);
            }
        }

        return new RpcModulatorContext(serviceElm, rpcInterfaces);
    }

    @Override
    protected CodeBlock generateLigatureMethodBody(TeleFacadeElement teleFacade) {
        CodeBlock.Builder cb = CodeBlock.builder();

        cb.addStatement("$T $N = new $T($T.class)",
                ClassName.get(RpcLigature.class),
                LIGATURE_VAR,
                ClassName.get(RpcLigature.class),
                TypeName.get(teleFacade.getParentService().getOriginClass().asType())
        );

        RpcModulatorContext rpcmContext = teleFacade.getProperty(RpcModulatorContext.class);

        for (RpcTeleMethodElement teleMethod : rpcmContext.getTeleMethods()) {
            cb.addStatement("$N.$N($S,this::$N)",
                    LIGATURE_VAR,
                    RpcLigature.ADD_METHOD,
                    teleMethod.getTeleMethod().getName(),
                    teleMethod.getTeleMethod().getName()
            );
        }

        cb.addStatement("return $N", LIGATURE_VAR);
        return cb.build();
    }

    @Override
    protected CodeBlock generateReadingContext(TeleParamElement teleParam) {

        MethodElement serviceMethodElm = teleParam.getParentTeleMethod().getProxyMethod().getOriginMethod();
        getProcessorContext().getTypeUtils().asMemberOf()
        serviceMethodElm.unwrap().as

        //getProcessorContext().getElementUtils().getOrigin()
        String paramName = teleParam.getOriginVariable().getName();
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("new $T(", ClassName.get(RpcTDRContext.class));
        cb.add("$S", paramName);
        cb.add(")");
        return cb.build();
    }
}
