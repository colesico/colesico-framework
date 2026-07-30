/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to  in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.restlet.codegen;


import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.restlet.*;
import colesico.framework.httprouter.codegen.RouterTeleServiceElement;
import colesico.framework.httprouter.codegen.RoutesModulator;
import colesico.framework.service.codegen.assist.ServiceCodegenUtils;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.teleapi.*;
import colesico.framework.teleapi.dataport.ReadOptions;
import colesico.framework.teleapi.dataport.WriteOptions;
import colesico.framework.telehttp.ParamName;
import colesico.framework.telehttp.codegen.TeleHttpCodegenUtils;
import colesico.framework.telehttp.codegen.TeleHttpReadElement;
import colesico.framework.telehttp.codegen.TeleHttpWriteElement;
import colesico.framework.telehttp.origin.Origin;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.TypeName;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author Vladlen Larionov
 */
public final class RestletModulator extends RoutesModulator {

    @Override
    protected Class<?> teleType() {
        return Restlet.class;
    }

    @Override
    protected boolean isTeleServiceSupported(ServiceElement service) {
        var teleAnn = service.originClass().annotation(Restlet.class);
        return teleAnn != null;
    }

    @Override
    protected Class<? extends ReadOptions> readOptionsClass() {
        return RestletReadOptions.class;
    }

    @Override
    protected Class<? extends WriteOptions> writeOptionsClass() {
        return RestletWriteOptions.class;
    }

    @Override
    public Set<Class<? extends Annotation>> serviceAnnotations() {
        return Set.of(Restlet.class);
    }

    /**
     * Assign json request pack element to  telefacade
     */
    @Override
    protected RouterTeleServiceElement createTeleService(ServiceElement serviceElm) {
        RouterTeleServiceElement teleFacade = super.createTeleService(serviceElm);
        // Enable  param beans
        teleFacade.setSupportParamBeans(true);
        // Enable param aggregates
        teleFacade.setSupportParamAggregates(true);
        return teleFacade;
    }

    @Override
    protected TeleReadElement createTeleRead(TeleOrdinaryParamElement teleParam) {

        CodeBlock.Builder valueTypeCode = CodeBlock.builder();
        ServiceCodegenUtils.generateTeleParamType(teleParam, valueTypeCode);

        CodeBlock.Builder optionsCode = CodeBlock.builder();

        // RestletReadOptions.builder(ParamType.class)
        optionsCode.add("$T.$N(", ClassName.get(RestletReadOptions.class), RestletReadOptions.BUILDER_METHOD);
        optionsCode.add(valueTypeCode.build());
        optionsCode.add(")");


        String paramName = TeleHttpCodegenUtils.paramName(teleParam);
        if (paramName != null) {
            optionsCode.add(".$N($S)", RestletReadOptions.PARAM_NAME_METHOD, paramName);
        }

        String originName = TeleHttpCodegenUtils.originName(teleParam, Origin.AUTO);
        if (originName != null) {
            optionsCode.add(".$N($S)", RestletReadOptions.ORIGIN_NAME_METHOD, originName);
        }

        TypeMirror customReader = TeleHttpCodegenUtils.customReaderClass(teleParam);
        ClassType customReaderCT = null;
        if (customReader != null) {
            optionsCode.add(".$N($T.class)", RestletReadOptions.CUSTOM_READER_METHOD, TypeName.get(customReader));
            customReaderCT = new ClassType(processorContext().processingEnv(), (DeclaredType) customReader);
        }

        optionsCode.add(".$N()", RestletReadOptions.BUILD_METHOD);

        return new TeleHttpReadElement(teleParam,
                null,
                optionsCode.build(),
                paramName,
                originName,
                customReaderCT
        );
    }

    @Override
    protected TeleWriteElement createTeleWrite(TeleCommandElement teleCommand) {
        CodeBlock.Builder optionsCode = CodeBlock.builder();
        optionsCode.add("$T.$N(", ClassName.get(RestletWriteOptions.class), RestletWriteOptions.BUILDER_METHOD);
        ServiceCodegenUtils.generateTeleResultType(teleCommand, optionsCode);
        optionsCode.add(")");

        TypeMirror customWriter = TeleHttpCodegenUtils.customWriterClass(teleCommand);
        ClassType customWriterCT = null;
        if (customWriter != null) {
            // .customWriter(CustomWriter.class)
            optionsCode.add(".$N($T.class)", RestletWriteOptions.CUSTOM_WRITER_METHOD, TypeName.get(customWriter));
            customWriterCT = new ClassType(processorContext().processingEnv(), (DeclaredType) customWriter);
        }

        // .build()
        optionsCode.add(".$N()", RestletWriteOptions.BUILD_METHOD);

        return new TeleHttpWriteElement(teleCommand, null, optionsCode.build(), customWriterCT);
    }

    @Override
    public void onTeleParameterParsed(TeleParameterElement teleParam) {
        super.onTeleParameterParsed(teleParam);
        if (teleParam instanceof TeleBeanFieldElement p) {
            AnnotationAssist<ParamName> paramNameAnn = p.originVariable().annotation(ParamName.class);
            if (paramNameAnn == null) {
                return;
            }
            p.setName(paramNameAnn.unwrap().value());
        }
    }
}
