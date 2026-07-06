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
import colesico.framework.restlet.codegen.assist.RestletCodegenUtils;
import colesico.framework.router.RouterCommandsRegistry;
import colesico.framework.router.codegen.RouterTeleServiceElement;
import colesico.framework.router.codegen.RoutesModulator;
import colesico.framework.service.codegen.assist.ServiceCodegenUtils;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.teleapi.*;
import colesico.framework.teleapi.TeleFacade;
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
    protected Class<? extends TeleFacade.CommandsRegistry> commandsClass() {
        return RouterCommandsRegistry.class;
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
        // Enable batch params
        teleFacade.setBatchParams(true);
        return teleFacade;
    }

    @Override
    protected TeleReadElement createTeleRead(TeleOrdinaryParamElement teleParam) {

        String paramName = TeleHttpCodegenUtils.paramName(teleParam);

        CodeBlock.Builder cb = CodeBlock.builder();

        // new RestletTRContext(
        cb.add("$T.$N(", ClassName.get(RestletReadOptions.class), RestletReadOptions.OF_METHOD);

        ServiceCodegenUtils.generateTeleParamType(teleParam, cb);

        cb.add(", $S", paramName);

        String originName = TeleHttpCodegenUtils.originName(teleParam, Origin.AUTO);

        TypeMirror customReader = RestletCodegenUtils.customReaderClass(teleParam, getProcessorContext().getElementUtils());
        ClassType customReaderCT = null;

        if (!originName.equals(RestletOrigin.AUTO) || customReader != null) {
            cb.add(", $S", originName);
        }

        if (customReader != null) {
            cb.add(", $T.class", TypeName.get(customReader));
            customReaderCT = new ClassType(getProcessorContext().getProcessingEnv(), (DeclaredType) customReader);
        }

        cb.add(")");

        return new TeleHttpReadElement(teleParam, cb.build(), paramName, originName, customReaderCT);
    }

    @Override
    protected TeleWriteElement createTeleWrite(TeleCommandElement teleCommand) {
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("$T.$N(", ClassName.get(RestletWriteOptions.class), RestletWriteOptions.OF_METHOD);

        ServiceCodegenUtils.generateTeleResultType(teleCommand, cb);

        TypeMirror customWriter = getCustomWriterClass(teleCommand);
        ClassType customWriterCT = null;
        if (customWriter != null) {
            cb.add(", $T.class", TypeName.get(customWriter));
            customWriterCT = new ClassType(getProcessorContext().getProcessingEnv(), (DeclaredType) customWriter);
        }
        cb.add(")");
        return new TeleHttpWriteElement(teleCommand, cb.build(), customWriterCT);
    }

    protected TypeMirror getCustomWriterClass(TeleCommandElement teleCommand) {
        var wrAnn = teleCommand.serviceMethod().originMethod().annotation(RestletResponseWriter.class);
        if (wrAnn == null) {
            wrAnn = teleCommand.parentTeleService().parentService().originClass().annotation(RestletResponseWriter.class);
        }
        if (wrAnn == null) {
            return null;
        }
        return wrAnn.valueTypeMirror(a -> a.value());
    }

    @Override
    public void onTeleEntryParsed(TeleEntryElement teleEntry) {
        super.onTeleParameterParsed(teleEntry);
        if (teleEntry instanceof TeleBatchParamElement) {
            AnnotationAssist<ParamName> paramNameAnn = teleEntry.getOriginElement().getAnnotation(ParamName.class);
            if (paramNameAnn == null) {
                return;
            }
            ((TeleBatchParamElement) teleEntry).setName(paramNameAnn.unwrap().value());
        }
    }
}
