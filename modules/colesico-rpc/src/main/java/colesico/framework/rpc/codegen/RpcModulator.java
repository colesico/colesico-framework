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

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.model.*;
import colesico.framework.rpc.Remote;
import colesico.framework.rpc.codegen.model.RpcModulatorContext;
import colesico.framework.rpc.codegen.model.RpcTeleMethodElement;
import colesico.framework.rpc.teleapi.*;
import colesico.framework.service.codegen.model.*;
import colesico.framework.service.codegen.modulator.TeleModulator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.ExecutableElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RpcModulator extends
        TeleModulator<RpcTeleDriver, RpcDataPort, RpcTDRContext, RpcTDWContext, RpcTIContext, RpcModulatorContext, RpcLigature, Remote> {

    public static final String PARAM_NAME_PREFIX = "arg";

    protected final Logger logger = LoggerFactory.getLogger(RpcModulator.class);

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
        logger.debug("Create RpcModulator context for service " + serviceElm.getOriginClass().getName());
        List<ClassType> allInterfaces = serviceElm.getOriginClass().getInterfaces();
        List<ClassType> rpcInterfaces = allInterfaces.stream()
                .filter(iface -> iface.asElement().getAnnotation(Remote.class) != null)
                .collect(Collectors.toList());

        if (rpcInterfaces.size() != 1) {
            throw CodegenException.of()
                    .message("Multiple RPC interfaces for service: " + serviceElm.getOriginClass().getName())
                    .element(serviceElm.getOriginClass().unwrap()).build();
        }

        logger.debug("RPC interface: " + rpcInterfaces.get(0).unwrap().toString());
        return new RpcModulatorContext(serviceElm, rpcInterfaces.get(0));
    }

    @Override
    protected void addTeleMethodToContext(TeleMethodElement teleMethodElement, RpcModulatorContext teleModulatorContext) {

        List<MethodElement> rpcMethods = teleModulatorContext.getRpcInterface().asClassElement().getMethods();
        for (MethodElement rpcMethodElm : rpcMethods) {
            logger.debug("Processing service method: " + teleMethodElement.getProxyMethod().getOriginMethod().unwrap().getSimpleName());
            boolean overrides = getProcessorContext()
                    .getElementUtils()
                    .overrides(teleMethodElement.getProxyMethod().getOriginMethod().unwrap(),
                            rpcMethodElm.unwrap(),
                            teleMethodElement.getParentTeleFacade().getParentService().getOriginClass().unwrap()
                    );

            if (overrides) {
                RpcTeleMethodElement rpcTme = new RpcTeleMethodElement(teleMethodElement, rpcMethodElm);
                teleMethodElement.setProperty(RpcTeleMethodElement.class, rpcTme);
                teleModulatorContext.addTeleMethod(rpcTme);
                if (logger.isDebugEnabled()){
                    StringBuilder sb = new StringBuilder(teleModulatorContext.getRpcInterface().unwrap().toString()+"( ");
                    for (ParameterElement param: rpcMethodElm.getParameters()){
                        sb.append(param.getName()).append(", ");
                    }
                    sb.append(" )");
                    logger.debug(sb.toString());
                }
                return;
            }
        }

        ExecutableElement method = teleMethodElement.getProxyMethod().getOriginMethod().unwrap();
        throw CodegenException.of()
                .message("RPC interface method is not defined for method: " + method.getSimpleName())
                .element(method).build();

    }

    @Override
    protected CodeBlock generateLigatureMethodBody(TeleFacadeElement teleFacade) {
        CodeBlock.Builder cb = CodeBlock.builder();

        cb.addStatement("$T $N = new $T($S)",
                ClassName.get(RpcLigature.class),
                LIGATURE_VAR,
                ClassName.get(RpcLigature.class),
                teleFacade.getParentService().getOriginClass().getName()
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
        // Build param names chain
        List<String> paramNamesChain = new ArrayList<>();
        TeleVarElement curVar = teleParam;
        while (curVar != null) {
            String name = curVar.getOriginVariable().getName();
            paramNamesChain.add(name);
            curVar = curVar.getParentVariable();
        }
        Collections.reverse(paramNamesChain);

        // Replace first param name with its index
        paramNamesChain.set(0, PARAM_NAME_PREFIX + teleParam.getParamIndex().toString());

        String paramName = StringUtils.join(paramNamesChain, ".");

        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("new $T(", ClassName.get(RpcTDRContext.class));
        cb.add("$S", paramName);
        cb.add(")");
        return cb.build();
    }
}
