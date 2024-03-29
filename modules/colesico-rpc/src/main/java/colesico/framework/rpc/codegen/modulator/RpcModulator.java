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

package colesico.framework.rpc.codegen.modulator;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.model.*;
import colesico.framework.rpc.RpcApi;
import colesico.framework.rpc.codegen.model.*;
import colesico.framework.rpc.codegen.parser.RpcApiParser;
import colesico.framework.rpc.teleapi.*;
import colesico.framework.service.codegen.assist.ServiceCodegenUtils;
import colesico.framework.service.codegen.model.*;
import colesico.framework.service.codegen.model.teleapi.*;
import colesico.framework.service.codegen.modulator.TeleFacadeModulator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RpcModulator extends TeleFacadeModulator<RpcTeleFacadeElement> {

    private final Logger logger = LoggerFactory.getLogger(RpcModulator.class);

    @Override
    protected Class<?> getTeleType() {
        return Rpc.class;
    }

    @Override
    protected boolean isTeleFacadeSupported(ServiceElement service) {
        List<ClassType> allInterfaces = service.getOriginClass().getInterfaces();
        for (ClassType iface : allInterfaces) {
            if (null != iface.asTypeElement().getAnnotation(RpcApi.class)) {
                return true;
            }
        }
        return false;
    }

    protected RpcServiceElement createRpcServiceElement(ServiceElement serviceElm) {
        logger.debug("Create RpcServiceElement for service " + serviceElm.getOriginClass().getName());
        List<ClassType> allInterfaces = serviceElm.getOriginClass().getInterfaces();
        List<ClassType> rpcInterfaces = allInterfaces.stream()
                .filter(iface -> iface.asTypeElement().getAnnotation(RpcApi.class) != null)
                .collect(Collectors.toList());


        List<RpcApiElement> rpcApiList = new ArrayList<>();
        RpcApiParser rpcApiParser = new RpcApiParser(processorContext.getProcessingEnv());
        for (ClassType ifaceType : rpcInterfaces) {
            logger.debug("Found RPC interface: " + ifaceType.getName());
            RpcApiElement rpcApi = rpcApiParser.parse(ifaceType.asClassElement());
            rpcApiList.add(rpcApi);
        }

        RpcServiceElement rpcServiceElm = new RpcServiceElement(serviceElm, rpcApiList);
        return rpcServiceElm;
    }

    @Override
    protected RpcTeleFacadeElement createTeleFacade(ServiceElement serviceElm) {
        return new RpcTeleFacadeElement(
                getTeleType(),
                RpcTeleDriver.class,
                RpcDataPort.class,
                RpcLigature.class,
                TeleFacadeElement.IocQualifier.ofClassed(Rpc.class),
                createRpcServiceElement(serviceElm)
        );
    }

    @Override
    protected void processTeleMethod(TeleMethodElement teleMethodElm) {

        logger.debug("RPC API parser processing service method: " + teleMethodElm.getServiceMethod().getOriginMethod().unwrap().getSimpleName());

        List<RpcApiElement> rpcApiList = ((RpcTeleFacadeElement) teleMethodElm.getParentTeleFacade()).getRpcService().getAllRpcApi();
        for (RpcApiElement rpcApi : rpcApiList) {
            List<RpcApiMethodElement> rpcApiMethods = rpcApi.getRpcMethods();
            for (RpcApiMethodElement rpcApiMethod : rpcApiMethods) {
                boolean overrides = getProcessorContext()
                        .getElementUtils()
                        .overrides(teleMethodElm.getServiceMethod().getOriginMethod().unwrap(),
                                rpcApiMethod.getOriginMethod().unwrap(),
                                teleMethodElm.getParentTeleFacade().getParentService().getOriginClass().unwrap()
                        );
                if (overrides) {

                    // Rpc method
                    rpcApiMethod.setTeleMethod(teleMethodElm);
                    teleMethodElm.setProperty(RpcApiMethodElement.class, rpcApiMethod);

                    // Rpc params
                    List<RpcApiParamElement> rpcApiParams = rpcApiMethod.getParameters();
                    // Method params
                    List<TeleEntryElement> teleParams = teleMethodElm.getParameters();

                    if (rpcApiParams.size() != teleParams.size()) {
                        throw CodegenException.of()
                                .message("RPC API parameters number mismatch")
                                .element(teleMethodElm.getServiceMethod().getOriginMethod().unwrap())
                                .build();
                    }

                    for (int i = 0; i < teleParams.size(); i++) {
                        if (teleParams.get(i) instanceof TeleCompoundElement) {
                            throw CodegenException.of()
                                    .message("Compound parameters not supported")
                                    .element(teleParams.get(i).getOriginElement().unwrap())
                                    .build();
                        }
                        TeleParameterElement teleParam = (TeleParameterElement) teleParams.get(i);
                        RpcApiParamElement apiParam = rpcApiParams.get(i);
                        if (!getProcessorContext().getTypeUtils().isAssignable(
                                teleParam.getOriginElement().getOriginType(),
                                apiParam.getOriginParam().getOriginType()
                        )) {
                            throw CodegenException.of()
                                    .message("RPC API parameter type mismatch for " + teleParam.getOriginElement().getName() +
                                            "; service param " + teleParam.getOriginElement().unwrap().asType() + " -> api param " +
                                            apiParam.getOriginParam().unwrap().asType())
                                    .element(teleMethodElm.getServiceMethod().getOriginMethod().unwrap())
                                    .build();
                        }
                        apiParam.setTeleParam(teleParam);
                        teleParam.setProperty(RpcApiParamElement.class, apiParam);
                    }

                    // Debug log
                    if (logger.isDebugEnabled()) {
                        StringBuilder sb = new StringBuilder("Bind RPC API method ");
                        sb.append(rpcApi.getOriginInterface().getName())
                                .append(rpcApiMethod.getOriginMethod().getName())
                                .append("( ");
                        for (ParameterElement param : rpcApiMethod.getOriginMethod().getParameters()) {
                            sb.append(param.getName()).append(", ");
                        }
                        sb.append(" )");
                        logger.debug(sb.toString());
                    }
                    return;
                }
            }
        }
    }


    @Override
    protected CodeBlock generateLigatureMethodBody(RpcTeleFacadeElement teleFacade) {
        CodeBlock.Builder cb = CodeBlock.builder();

        // RpcLigature ligature = new RpcLigature();
        cb.addStatement("$T $N = new $T()",
                ClassName.get(RpcLigature.class),
                LIGATURE_VAR,
                ClassName.get(RpcLigature.class)
        );

        RpcServiceElement rpcServiceElm = teleFacade.getRpcService();

        // ligature
        cb.add("$N", LIGATURE_VAR);
        cb.indent();
        for (RpcApiElement rpcApi : rpcServiceElm.getAllRpcApi()) {
            // .addApi(
            cb.add("\n.$N(\n", RpcLigature.ADD_API_METHOD);
            cb.indent();
            // RpcLigature.api("ns","rpc.interface.name")
            if (RpcApi.DEFAULT_NAMESPACE.equals(rpcApi.getRpcNamespace())) {
                cb.add("$T.$N($S)\n", ClassName.get(RpcLigature.class), RpcLigature.API_METHOD, rpcApi.rpcName());
            } else {
                cb.add("$T.$N($S,$S)\n", ClassName.get(RpcLigature.class), RpcLigature.API_METHOD, rpcApi.getRpcNamespace(), rpcApi.rpcName());
            }
            cb.indent();
            for (RpcApiMethodElement rpcApiMethod : rpcApi.getRpcMethods()) {
                // .addMethod("rpcMethodName", teleMethodFactory()
                cb.add(".$N($S, $N())\n",
                        RpcLigature.RpcApiSpec.ADD_RPC_METHOD,
                        rpcApiMethod.rpcMethodName(),
                        rpcApiMethod.getTeleMethod().getBuilderName()
                );
            }
            cb.unindent();
            cb.unindent();
            cb.add(")");
        }
        cb.add(";\n");
        cb.unindent();
        cb.addStatement("return $N", LIGATURE_VAR);
        return cb.build();
    }

    @Override
    protected TRContextElement createReadingContext(TeleParameterElement teleParam) {
        CodeBlock.Builder cb = CodeBlock.builder();
        RpcApiMethodElement rpcApiMethod = getRpcApiMethod(teleParam.getParentTeleMethod());
        RpcApiParamElement apiParam = teleParam.getProperty(RpcApiParamElement.class);
        // RpcTRContext.of(EnvelopeClass.RequestClass::getterMethod)
        cb.add("$T.$N(", ClassName.get(RpcTRContext.class), RpcTRContext.OF_METHOD);
        ServiceCodegenUtils.generateTeleEntryType(teleParam, cb);
        cb.add(", $T::$N)", ClassName.bestGuess(rpcApiMethod.getRequestClassName()), apiParam.getterName());
        return new TRContextElement(teleParam, cb.build());
    }

    @Override
    protected TIContextElement createInvocationContext(TeleMethodElement teleMethod) {
        RpcApiMethodElement rpcApiMethod = getRpcApiMethod(teleMethod);
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("new $T($T.class, $T.class)", ClassName.get(RpcTIContext.class),
                ClassName.bestGuess(rpcApiMethod.getRequestClassName()),
                ClassName.bestGuess(rpcApiMethod.getResponseClassName())
        );
        return new TIContextElement(teleMethod, cb.build());
    }

    protected RpcApiMethodElement getRpcApiMethod(TeleMethodElement teleMethod) {
        RpcApiMethodElement rpcApiMethod = teleMethod.getProperty(RpcApiMethodElement.class);
        if (rpcApiMethod == null) {
            throw CodegenException.of()
                    .message("Unknown RPC API method for service method " +
                            teleMethod.getParentTeleFacade().getParentService().getOriginClass().getName() +
                            "->" + teleMethod.getName() + "(...)")
                    .element(teleMethod.getServiceMethod().getOriginMethod().unwrap())
                    .build();
        }
        return rpcApiMethod;
    }
}
