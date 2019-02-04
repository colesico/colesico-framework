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

package colesico.framework.config.codegen;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractProcessor;
import colesico.framework.config.Configuration;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Vladlen Larionov
 */
public class ConfigProcessor extends FrameworkAbstractProcessor {

    protected IocGenerator iocGenerator;
    protected ConfRegistry confRegistry;

    public ConfigProcessor() {
        super();
    }

    @Override
    protected Class<? extends Annotation>[] getSupportedAnnotations() {
        return new Class[]{Configuration.class};
    }

    @Override
    protected void onInit() {
        this.iocGenerator = new IocGenerator(processingEnv);
        this.confRegistry = new ConfRegistry(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        confRegistry.clear();
        logger.debug("Start configurations processing...");
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Configuration.class);
        for (Element elm : elements) {
            if (elm.getKind() != ElementKind.CLASS) {
                continue;
            }
            TypeElement classElement = null;
            try {
                classElement = (TypeElement) elm;
                logger.debug("Processing configuration: " + classElement.asType().toString());
                confRegistry.register(classElement);
            } catch (CodegenException ce) {
                String message = "Error processing configuration class '" + elm.toString() + "': " + ce.getMessage();
                logger.debug(message);
                ce.print(processingEnv, elm);
            } catch (Exception e) {
                StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));
                String msg = ExceptionUtils.getRootCauseMessage(e);
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
                if (logger.isDebugEnabled()) {
                    e.printStackTrace();
                }
                return true;
            }
        }

        if (!confRegistry.isEmpty()) {
            for (Map.Entry<String, ConfRegistry.ByRankMap> packageToRank : confRegistry.getByPackageMap().entrySet()) {
                for (Map.Entry<String, List<ConfigElement>> rankToConfig : packageToRank.getValue().entrySet()) {
                    iocGenerator.generateProducerClass(/* pakage name */ packageToRank.getKey(), /* rank  */ rankToConfig.getKey(), rankToConfig.getValue());
                }
            }
        }

        return true;
    }

}
