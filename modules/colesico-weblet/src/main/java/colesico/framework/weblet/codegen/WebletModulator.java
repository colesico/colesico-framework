/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */

package colesico.framework.weblet.codegen;

import colesico.framework.assist.CollectionUtils;
import colesico.framework.assist.codegen.model.AnnotationElement;
import colesico.framework.router.codegen.RoutesModulator;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.service.codegen.model.TeleParamElement;
import colesico.framework.service.codegen.model.TeleVarElement;
import colesico.framework.service.codegen.modulator.Modulator;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.TeleDriver;
import colesico.framework.weblet.Origin;
import colesico.framework.weblet.ParamName;
import colesico.framework.weblet.ParamOrigin;
import colesico.framework.weblet.Weblet;
import colesico.framework.weblet.teleapi.OriginFacade;
import colesico.framework.weblet.teleapi.ReaderContext;
import colesico.framework.weblet.teleapi.WebletDataPort;
import colesico.framework.weblet.teleapi.WebletTeleDriver;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Vladlen Larionov
 */
public class WebletModulator extends RoutesModulator {

    @Override
    protected Class<? extends Annotation> getTeleAnnotation() {
        return Weblet.class;
    }

    @Override
    protected String getTeleType() {
        return Weblet.class.getSimpleName();
    }

    @Override
    protected Class<? extends TeleDriver> getTeleDriverClass() {
        return WebletTeleDriver.class;
    }

    @Override
    protected Class<? extends DataPort> getDataPortClass() {
        return WebletDataPort.class;
    }

    @Override
    public Set<Class<? extends Annotation>> serviceAnnotations() {
        return CollectionUtils.annotationClassSet(Weblet.class);
    }

    @Override
    protected CodeBlock generateReadingContext(TeleParamElement teleParam) {
        TeleMethodElement teleMethod = teleParam.getParentTeleMethod();
        CodeBlock.Builder cb = CodeBlock.builder();

        List<String> paramsChain = new ArrayList<>();

        TeleVarElement curVar = teleParam;
        TeleVarElement rootVar = teleParam;
        while (curVar != null) {
            String name;
            AnnotationElement<ParamName> nameAnn = curVar.getOriginVariable().getAnnotation(ParamName.class);
            if (nameAnn != null) {
                name = nameAnn.unwrap().value();
            } else {
                name = curVar.getOriginVariable().getName();
            }

            if (StringUtils.isNoneBlank(name)) {
                paramsChain.add(name);
            }
            rootVar = curVar;
            curVar = curVar.getParentVariable();
        }

        Collections.reverse(paramsChain);
        String paramName = StringUtils.join(paramsChain, ".");
        String paramOrigin = Origin.DEFAULT.name();

        AnnotationElement<ParamOrigin> originAnn = teleParam.getOriginVariable().getAnnotation(ParamOrigin.class);
        if (originAnn == null) {
            originAnn = teleMethod.getProxyMethod().getOriginMethod().getAnnotation(ParamOrigin.class);
            if (originAnn == null) {
                originAnn = rootVar.getOriginVariable().getAnnotation(ParamOrigin.class);
            }
        }

        if (originAnn != null) {
            paramOrigin = originAnn.unwrap().value().name();
        }

        AnnotationElement<ParamName> nameAnn = teleParam.getOriginVariable().getAnnotation(ParamName.class);
        if (nameAnn != null && nameAnn.unwrap().value() != "") {
            paramName = nameAnn.unwrap().value();
        }
        cb.add("new $T(", ClassName.get(ReaderContext.class));
        cb.add("$S,$T.$N", paramName, ClassName.get(OriginFacade.class), paramOrigin);
        cb.add(")");
        return cb.build();
    }
}
