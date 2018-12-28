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

import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;
import colesico.framework.ioc.Produces;
import colesico.framework.ioc.codegen.generator.IocletGenerator;
import colesico.framework.ioc.codegen.model.CustomFactoryElement;
import colesico.framework.ioc.codegen.model.DefaultFactoryElement;
import colesico.framework.ioc.codegen.model.IocletElement;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.Genstamp;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.*;
import javax.inject.Inject;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Vladlen Larionov
 */
@AutoService(Processor.class)
public class ProducersProcessor extends AbstractProcessor {

    public static final String IOC_MODULE_NAME="colesico.framework.ioc";

    private Logger logger;

    protected ProcessingEnvironment processingEnv;
    protected Elements elementUtils;
    protected Types typeUtils;
    protected Messager messager;
    protected Filer filer;

    protected IocletGenerator iocletGenerator;
    protected final List<Element> iocletLinkedElements = new ArrayList<>();

    public ProducersProcessor() {
        try {
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
            System.setProperty("org.slf4j.simpleLogger.log.colesico.framework", "debug");
            logger = LoggerFactory.getLogger(ProducersProcessor.class);
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
        result.add(Producer.class.getName());
        return result;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        try {
            logger.debug("Initialize IOC annotation processor...");
            this.processingEnv = processingEnv;
            elementUtils = processingEnv.getElementUtils();
            typeUtils = processingEnv.getTypeUtils();
            messager = processingEnv.getMessager();
            filer = processingEnv.getFiler();
            iocletGenerator = new IocletGenerator();
        } catch (Throwable e) {
            System.out.print("Error initializing " + ProducersProcessor.class.getName() + " ");
            System.out.println(e);
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        logger.debug("Start IOC producers processing...");
        for (Element elm : roundEnv.getElementsAnnotatedWith(Producer.class)) {
            if (elm.getKind() != ElementKind.CLASS) {
                continue;
            }
            TypeElement producerElement = null;
            try {
                producerElement = (TypeElement) elm;
                IocletElement iocletElement = parseProducer(producerElement);
                generateIoclet(iocletElement);
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
                return true;
            }
        }

        if (roundEnv.processingOver()) {

        }
        return true;
    }

    protected ExecutableElement getInjectableConstructor(TypeElement producerElement) {
        List<? extends Element> members = elementUtils.getAllMembers(producerElement);
        List<ExecutableElement> methods = ElementFilter.constructorsIn(members);
        ExecutableElement constructor = null;
        ExecutableElement firstConstructor = null;
        for (ExecutableElement method : methods) {
            if (!method.getModifiers().contains(Modifier.PUBLIC)) {
                continue;
            }
            if (firstConstructor == null) {
                firstConstructor = method;
            }
            Inject constructorMarker = method.getAnnotation(Inject.class);
            if (constructorMarker == null) {
                continue;
            }
            constructor = method;
            break;
        }
        if (constructor == null) {
            constructor = firstConstructor;
        }
        return constructor;
    }

    protected IocletElement parseProducer(TypeElement producerElement) {

        Genstamp genstampAnn = producerElement.getAnnotation(Genstamp.class);
        if (genstampAnn != null) {
            logger.debug("Parse producer: " + producerElement.asType().toString() +
                    "; Generator=" + genstampAnn.generator() +
                    "; Timestamp=" + genstampAnn.timestamp() +
                    "; HashId=" + genstampAnn.hashId());
        } else {
            logger.debug("Parse producer: " + producerElement.asType().toString());
        }

        String packageName = CodegenUtils.getPackageName(producerElement);
        ModuleElement producerModule = elementUtils.getModuleOf(producerElement);

        if (!CodegenUtils.checkPackageAccessibility(producerModule, packageName, IOC_MODULE_NAME)) {
            String errMsg = String.format("Package %s must be exported from module %s to %s", packageName, producerModule.toString(),IOC_MODULE_NAME);
            logger.info(errMsg);
            //throw CodegenException.of().message(errMsg).element(producerModule).create();
        }

        iocletLinkedElements.clear();
        iocletLinkedElements.add(producerElement);

        IocletElement iocletElement = new IocletElement(producerElement);

        // Scan producer methods
        List<ExecutableElement> methods = CodegenUtils.getProxiableMethods(processingEnv, producerElement,
                new Modifier[]{Modifier.PUBLIC});
        for (ExecutableElement method : methods) {
            logger.debug("Found custom factory method: " + method.getSimpleName().toString());
            iocletElement.addFactory(new CustomFactoryElement(method));
            Element te = ((DeclaredType) method.getReturnType()).asElement();
            iocletLinkedElements.add(te);
        }

        // Scan @Produce annotation
        List<Produce> produceList = new ArrayList<>();
        Produces produces = producerElement.getAnnotation(Produces.class);
        if (produces != null) {
            for (Produce produce : produces.value()) {
                produceList.add(produce);
            }
        } else {
            Produce produce = producerElement.getAnnotation(Produce.class);
            if (produce != null) {
                produceList.add(produce);
            }
        }

        for (Produce produce : produceList) {
            TypeMirror typeMirr = CodegenUtils.getAnnotationValueTypeMirror(produce, (p) -> p.value());
            TypeElement typeElm = elementUtils.getTypeElement(typeMirr.toString());
            iocletLinkedElements.add(typeElm);
            logger.debug("Found default factory for : " + typeMirr.toString());

            //moduleMetamodel.addExports(CodegenUtils.getPackageName(typeElm));

            ExecutableElement injConstructor = getInjectableConstructor(typeElm);
            if (injConstructor == null) {
                throw CodegenException.of().message("Unable to find injectable constructor for class: " + typeMirr.toString()).build();
            }
            iocletElement.addFactory(new DefaultFactoryElement(producerElement, produce, injConstructor));
        }

        return iocletElement;

    }

    protected void generateIoclet(IocletElement iocletElement) {
        try {
            final TypeSpec typeSpec = iocletGenerator.generate(iocletElement);
            // Create class source file
            Element[] linkedElm = iocletLinkedElements.toArray(new Element[iocletLinkedElements.size()]);
            String packageName = CodegenUtils.getPackageName(iocletElement.getOriginProducer());
            CodegenUtils.createJavaFile(processingEnv, typeSpec, packageName, linkedElm);
        } catch (Exception e) {
            logger.debug("Error generating ioclet:" + ExceptionUtils.getRootCauseMessage(e));
            throw e;
        }
    }

}
