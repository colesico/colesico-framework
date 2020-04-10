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

package colesico.framework.weblet.codegen;

import colesico.framework.assist.CollectionUtils;
import colesico.framework.assist.codegen.model.AnnotationToolbox;
import colesico.framework.router.codegen.RoutesModulator;
import colesico.framework.service.codegen.model.*;
import colesico.framework.weblet.Origin;
import colesico.framework.weblet.ParamName;
import colesico.framework.weblet.ParamOrigin;
import colesico.framework.weblet.Weblet;
import colesico.framework.weblet.teleapi.*;
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
public class WebletModulator extends
        RoutesModulator<WebletTeleDriver, WebletDataPort, WebletTDRContext, WebletTDWContext, WebletTIContext> {

    @Override
    protected String getTeleType() {
        return Weblet.class.getSimpleName();
    }

    @Override
    protected boolean isTeleFacadeSupported(ServiceElement serviceElm) {
        AnnotationToolbox teleAnn = serviceElm.getOriginClass().getAnnotation(Weblet.class);
        return teleAnn != null;
    }

    @Override
    protected Class<WebletTeleDriver> getTeleDriverClass() {
        return WebletTeleDriver.class;
    }

    @Override
    protected Class<WebletDataPort> getDataPortClass() {
        return WebletDataPort.class;
    }

    @Override
    public Set<Class<? extends Annotation>> serviceAnnotations() {
        return CollectionUtils.annotationClassSet(Weblet.class);
    }

    @Override
    protected CodeBlock generateReadingContext(TeleParamElement teleParam) {
        return generateReadingContextImpl(teleParam);
    }

    public static CodeBlock generateReadingContextImpl(TeleParamElement teleParam) {
        TeleMethodElement teleMethod = teleParam.getParentTeleMethod();
        CodeBlock.Builder cb = CodeBlock.builder();

        List<String> paramNamesChain = new ArrayList<>();

        TeleVarElement curVar = teleParam;
        TeleVarElement rootVar = teleParam;
        while (curVar != null) {
            String name;
            AnnotationToolbox<ParamName> nameAnn = curVar.getOriginVariable().getAnnotation(ParamName.class);
            if (nameAnn != null) {
                name = nameAnn.unwrap().value();
            } else if (curVar instanceof TeleCompElement) {
                name = null;
            } else {
                name = curVar.getOriginVariable().getName();
            }

            if (StringUtils.isNotBlank(name)) {
                paramNamesChain.add(name);
            }
            rootVar = curVar;
            curVar = curVar.getParentVariable();
        }

        Collections.reverse(paramNamesChain);
        String paramName = StringUtils.join(paramNamesChain, "");

        String paramOrigin = Origin.DEFAULT.name();

        AnnotationToolbox<ParamOrigin> originAnn = teleParam.getOriginVariable().getAnnotation(ParamOrigin.class);
        if (originAnn == null) {
            originAnn = teleMethod.getProxyMethod().getOriginMethod().getAnnotation(ParamOrigin.class);
            if (originAnn == null) {
                originAnn = rootVar.getOriginVariable().getAnnotation(ParamOrigin.class);
            }
        }

        if (originAnn != null) {
            paramOrigin = originAnn.unwrap().value().name();
        }

        cb.add("new $T(", ClassName.get(WebletTDRContext.class));
        cb.add("$S,$T.$N", paramName, ClassName.get(OriginFacade.class), paramOrigin);
        cb.add(")");
        return cb.build();
    }
}
