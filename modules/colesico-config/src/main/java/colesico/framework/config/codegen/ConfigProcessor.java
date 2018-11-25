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

import colesico.framework.config.Configuration;
import colesico.framework.assist.codegen.CodegenException;
import com.google.auto.service.AutoService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Vladlen Larionov
 */
@AutoService(Processor.class)
public class ConfigProcessor extends AbstractProcessor {

    private Logger logger;

    protected ProcessingEnvironment processingEnv;
    protected Elements elementUtils;

    protected IocGenerator iocGenerator;

    protected ConfRegistry confRegistry;

    public ConfigProcessor() {
        try {
            logger = LoggerFactory.getLogger(ConfigProcessor.class);
        } catch (Throwable e) {
            System.out.print("Logger creation error: ");
            System.out.println(e);
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> result = new HashSet<>();
        result.add(Configuration.class.getName());
        return result;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        try {
            this.processingEnv = processingEnv;
            this.elementUtils = processingEnv.getElementUtils();
            this.iocGenerator = new IocGenerator(processingEnv);
            this.confRegistry = new ConfRegistry(processingEnv);
        } catch (Throwable e) {
            System.out.print("Error initializing " + ConfigProcessor.class.getName() + " ");
            System.out.println(e);
        }
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
