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
import colesico.framework.service.Compound;
import colesico.framework.service.LocalParam;
import colesico.framework.service.codegen.model.*;
import colesico.framework.service.codegen.modulator.TeleModulator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class RpcModulator extends
        TeleModulator<RpcTeleDriver, RpcDataPort, RpcTRContext, RpcTWContext, RpcTIContext, RpcServiceModulatorContext, RpcLigature, Rpc> {

    public static final String PARAM_NAME_PREFIX = "arg";

    private final Logger logger = LoggerFactory.getLogger(RpcModulator.class);

    @Override
    protected String getTeleType() {
        return Rpc.class.getSimpleName();
    }

    @Override
    protected boolean isTeleFacadeSupported(ServiceElement serviceElm) {
        List<ClassType> allInterfaces = serviceElm.getOriginClass().getInterfaces();
        for (ClassType iface : allInterfaces) {
            if (null != iface.asTypeElement().getAnnotation(RpcApi.class)) {
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
    protected Class<Rpc> getQualifierClass() {
        return Rpc.class;
    }

    @Override
    protected Class<RpcServiceModulatorContext> getTeleModulatorContextClass() {
        return RpcServiceModulatorContext.class;
    }

    @Override
    public void onTeleParamLinked(TeleParamElement teleParam, Deque<VarElement> varStack) {
        RpcServiceModulatorContext ctx = teleParam.getParentTeleMethod().getParentTeleFacade().getProperty(getTeleModulatorContextClass());
        if (ctx != null) {
            if (teleParam.getIsLocal()) {
                throw CodegenException.of()
                        .message("@" + LocalParam.class.getSimpleName() + " parameters not supported")
                        .element(teleParam.getOriginVariable().unwrap())
                        .build();
            }
        }
        super.onTeleParamLinked(teleParam, varStack);
    }

    @Override
    public void onTeleCompoundLinked(TeleCompElement teleComp) {
        RpcServiceModulatorContext ctx = teleComp.getParentTeleMethod().getParentTeleFacade().getProperty(getTeleModulatorContextClass());
        if (ctx != null) {
            throw CodegenException.of()
                    .message("@" + Compound.class.getSimpleName() + " parameters not supported")
                    .element(teleComp.getOriginVariable())
                    .build();
        }
    }

    @Override
    protected RpcServiceModulatorContext createTeleModulatorContext(ServiceElement serviceElm) {
        logger.debug("Create RpcModulator context for service " + serviceElm.getOriginClass().getName());
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
        return new RpcServiceModulatorContext(rpcServiceElm);
    }

    @Override
    protected void addTeleMethodToContext(TeleMethodElement teleMethodElement, RpcServiceModulatorContext teleModulatorContext) {

        logger.debug("RPC API parser processing service method: " + teleMethodElement.getProxyMethod().getOriginMethod().unwrap().getSimpleName());
        for (RpcApiElement rpcApi : teleModulatorContext.getRpcImplementation().getAllRpcApi()) {
            List<RpcApiMethodElement> rpcApiMethods = rpcApi.getRpcMethods();
            for (RpcApiMethodElement rpcApiMethod : rpcApiMethods) {
                boolean overrides = getProcessorContext()
                        .getElementUtils()
                        .overrides(teleMethodElement.getProxyMethod().getOriginMethod().unwrap(),
                                rpcApiMethod.getOriginMethod().unwrap(),
                                teleMethodElement.getParentTeleFacade().getParentService().getOriginClass().unwrap()
                        );
                if (overrides) {

                    // Bind method
                    rpcApiMethod.setTeleMethod(teleMethodElement);
                    teleMethodElement.setProperty(RpcApiMethodElement.class, rpcApiMethod);

                    // Bind params
                    List<RpcApiParamElement> rpcApiParams = rpcApiMethod.getParameters();
                    // method params or compound fields
                    List<TeleVarElement> teleParams = teleMethodElement.getParameters();

                    if (rpcApiParams.size() != teleParams.size()) {
                        throw CodegenException.of()
                                .message("RPC API parameters number mismatch")
                                .element(teleMethodElement.getProxyMethod().getOriginMethod().unwrap())
                                .build();
                    }

                    for (int i = 0; i < teleParams.size(); i++) {
                        TeleVarElement teleParam = teleParams.get(i);
                        RpcApiParamElement apiParam = rpcApiParams.get(i);
                        if (!getProcessorContext().getTypeUtils().isAssignable(
                                teleParam.getOriginVariable().unwrap().asType(),
                                apiParam.getOriginParam().unwrap().asType()
                        )) {
                            throw CodegenException.of()
                                    .message("RPC API parameter type mismatch for " + teleParam.getOriginVariable().getName() +
                                            "; service param " + teleParam.getOriginVariable().unwrap().asType() + " -> api param " +
                                            apiParam.getOriginParam().unwrap().asType())
                                    .element(teleMethodElement.getProxyMethod().getOriginMethod().unwrap())
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
    protected CodeBlock generateLigatureMethodBody(TeleFacadeElement teleFacade) {
        CodeBlock.Builder cb = CodeBlock.builder();

        // RpcLigature ligature = new RpcLigature();
        cb.addStatement("$T $N = new $T()",
                ClassName.get(RpcLigature.class),
                LIGATURE_VAR,
                ClassName.get(RpcLigature.class)
        );

        RpcServiceModulatorContext rpcmContext = teleFacade.getProperty(RpcServiceModulatorContext.class);

        // ligature
        cb.add("$N", LIGATURE_VAR);
        cb.indent();
        for (RpcApiElement rpcApi : rpcmContext.getRpcImplementation().getAllRpcApi()) {
            // .addApi(
            cb.add("\n.$N(\n", RpcLigature.ADD_API_METHOD);
            cb.indent();
            // RpcLigature.api("rpc.interface.name")
            cb.add("$T.$N($S)\n", ClassName.get(RpcLigature.class), RpcLigature.API_METHOD, rpcApi.rpcApiName());
            cb.indent();
            for (RpcApiMethodElement rpcApiMethod : rpcApi.getRpcMethods()) {
                // .addMethod("methodName", RpcLigature.method(...)
                cb.add(".$N($S, $T.$N(null,null,null) )\n",
                        RpcLigature.RpcApi.ADD_METHOD,
                        rpcApiMethod.rpcMethodName(),
                        ClassName.get(RpcLigature.class),
                        RpcLigature.METHOD_METHOD);
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
    protected CodeBlock generateReadingContext(TeleParamElement teleParam) {
        CodeBlock.Builder cb = CodeBlock.builder();

        RpcApiMethodElement rpcApiMethod = teleParam.getParentTeleMethod().getProperty(RpcApiMethodElement.class);
        String requestSchemeClassName = rpcApiMethod.getParentApi().getOriginInterface().getPackageName() + '.'
                + rpcApiMethod.getParentApi().getSchemeClassName() + '.' + rpcApiMethod.getRequestClassName();

        RpcApiParamElement apiParam = teleParam.getProperty(RpcApiParamElement.class);
        // RpcTRContext.of(SchemeClass.RequestClass::getterMethod)
        cb.add("$T.$N($T::$N)", ClassName.get(RpcTRContext.class),
                RpcTRContext.OF_METHOD,
                ClassName.bestGuess(requestSchemeClassName),
                apiParam.getterName()
        );
        return cb.build();
    }

}
