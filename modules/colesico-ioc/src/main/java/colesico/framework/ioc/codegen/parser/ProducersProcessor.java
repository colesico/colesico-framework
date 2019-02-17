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

package colesico.framework.ioc.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractProcessor;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.ioc.Producer;
import colesico.framework.ioc.codegen.generator.IocletGenerator;
import colesico.framework.ioc.codegen.generator.SPIGenerator;
import colesico.framework.ioc.codegen.model.IocletElement;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Vladlen Larionov
 */
public class ProducersProcessor extends FrameworkAbstractProcessor {


    protected ProducerParser parseProducer;
    protected IocletGenerator iocletGenerator;
    protected SPIGenerator spiGenerator;
    protected final Map<TypeElement, IocletElement> createdIoclets = new HashMap<>();

    public ProducersProcessor() {
        super();
    }

    @Override
    protected Class<? extends Annotation>[] getSupportedAnnotations() {
        return new Class[]{Producer.class};
    }

    @Override
    protected void onInit() {
        iocletGenerator = new IocletGenerator();
        spiGenerator = new SPIGenerator(processingEnv);
        createdIoclets.clear();
        parseProducer = new ProducerParser(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        logger.debug("Start IOC producers processing...");
        boolean result = false;

        for (Element elm : roundEnv.getElementsAnnotatedWith(Producer.class)) {
            if (elm.getKind() != ElementKind.CLASS) {
                continue;
            }
            TypeElement producerElement = null;
            try {
                producerElement = (TypeElement) elm;
                IocletElement iocletElement = parseProducer.parse(new ClassElement(processingEnv, producerElement));
                createdIoclets.put(producerElement, iocletElement);
                result = true;
            } catch (CodegenException ce) {
                String message = "Error processing class '" + elm.toString() + "': " + ce.getMessage();
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
                return false;
            }
        }

        // Write Ioclets java files only at last round

        if (roundEnv.processingOver()) {
            logger.debug("Ioclets generation is starting: " + createdIoclets.size());
            for (IocletElement ie : createdIoclets.values()) {
                generateIoclet(ie);
            }
            spiGenerator.generateSPIFile(createdIoclets.values());
        }

        return result;
    }


    protected void generateIoclet(IocletElement iocletElement) {
        try {
            final TypeSpec typeSpec = iocletGenerator.generate(iocletElement);
            // Create class source file
            String packageName = iocletElement.getOriginProducer().getPackageName();
            CodegenUtils.createJavaFile(processingEnv, typeSpec, packageName, iocletElement.getOriginProducer().unwrap());
        } catch (Exception e) {
            logger.debug("Error generating ioclet: " + ExceptionUtils.getRootCauseMessage(e));
            throw e;
        }
    }

}
