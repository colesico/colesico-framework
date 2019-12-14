/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.ioc.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractParser;
import colesico.framework.assist.codegen.Genstamp;
import colesico.framework.assist.codegen.model.*;
import colesico.framework.ioc.*;
import colesico.framework.ioc.codegen.model.*;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.ModuleElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

public class ProducerParser extends FrameworkAbstractParser {

    public static final String IOC_MODULE_NAME = "colesico.framework.ioc";

    public ProducerParser(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    protected MethodElement getInjectableConstructor(ClassElement producer) {
        List<MethodElement> methods = producer.getConstructorsFiltered(
            m -> m.unwrap().getModifiers().contains(Modifier.PUBLIC)
                && !m.unwrap().getModifiers().contains(Modifier.FINAL)
        );

        MethodElement constructor = null;
        MethodElement firstConstructor = null;
        for (MethodElement method : methods) {
            if (firstConstructor == null) {
                firstConstructor = method;
            }
            AnnotationElement<Inject> injectAnn = method.getAnnotation(Inject.class);
            if (injectAnn == null) {
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

    protected ScopeElement obtainScope(ParserElement element) {

        AnnotationElement<Singleton> singleton = element.getAnnotation(Singleton.class);
        if (singleton != null) {
            return new ScopeElement(ScopeElement.ScopeKind.SINGLETON, null);
        }

        AnnotationElement<Unscoped> unscoped = element.getAnnotation(Unscoped.class);
        if (unscoped != null) {
            return new ScopeElement(ScopeElement.ScopeKind.UNSCOPED, null);
        }

        // Find custom scope
        ScopeElement result = null;
        for (AnnotationMirrorElement am : element.getAnnotationMirrors()) {
            ScopeElement scopeElm = null;

            AnnotationElement<CustomScope> customScope = am.getAnnotation(CustomScope.class);
            if (customScope != null) {
                TypeMirror scopeClass = customScope.getValueTypeMirror(CustomScope::value);
                scopeElm = new ScopeElement(ScopeElement.ScopeKind.CUSTOM, new ClassType(getProcessingEnv(), (DeclaredType) scopeClass));
            }

            if (scopeElm != null) {
                if (result == null) {
                    result = scopeElm;
                } else {
                    throw CodegenException.of().message("Ambiguous scope definition").element(element).build();
                }
            }
        }
        return result;
    }

    private List<MethodElement> parsePostConstructListeners(ClassType suppliedType) {
        List<MethodElement> listeners = new ArrayList<>();
        ClassElement suppliedTypeElement = suppliedType.asClassElement();
        for (MethodElement me : suppliedTypeElement.getMethods()) {
            if (me.getAnnotation(PostConstruct.class) == null) {
                continue;
            }
            if (!me.getParameters().isEmpty()) {
                throw CodegenException.of().message("Post construct method '"
                    + suppliedTypeElement.getName()
                    + "." + me.getName() + "(...)'  should not have arguments").element(me.unwrap()).build();
            }
            if (!me.unwrap().getModifiers().contains(Modifier.PUBLIC)) {
                throw CodegenException.of().message("Post construct method '"
                    + suppliedTypeElement.getName()
                    + "." + me.getName() + "(...)'  should be a public").element(me.unwrap()).build();
            }
            listeners.add(me);
        }
        return listeners;
    }

    protected InjectableElement createInjectableElement(final FactoryElement parentFactory, final ParameterElement parameter) {
        logger.debug("Create injectable element for: " + parameter);

        if (parameter.asClassType() == null) {
            throw CodegenException.of().message("Unsupported parameter type kind for " + parameter.getName()).element(parameter.unwrap()).build();
        }

        AnnotationElement<Message> messageAnn = parameter.getAnnotation(Message.class);
        boolean isMessage = messageAnn != null;

        String parameterClassName = parameter.asClassType().getErasure().toString();
        boolean isSupplier = parameterClassName.equals(Supplier.class.getName());
        boolean isProvider = parameterClassName.equals(Provider.class.getName());
        boolean isPolysupplier = parameterClassName.equals(Polysupplier.class.getName());

        // Injection kind detection
        InjectableElement.InjectionKind injectionKind;
        ClassType injectedType;

        if (isMessage) {
            injectionKind = InjectableElement.InjectionKind.MESSAGE;
            injectedType = parameter.asClassType();
        } else if (isSupplier || isProvider || isPolysupplier) {
            List<? extends TypeMirror> generics = parameter.asClassType().unwrap().getTypeArguments();
            if (generics.isEmpty()) {
                throw CodegenException.of().message("Unable to determine injecting type").element(parameter).build();
            }
            TypeMirror genericType = generics.get(0);
            injectedType = new ClassType(getProcessingEnv(), (DeclaredType) genericType);
            if (isSupplier) {
                injectionKind = InjectableElement.InjectionKind.SUPPLIER;
            } else if (isProvider) {
                injectionKind = InjectableElement.InjectionKind.PROVIDER;
            } else {
                injectionKind = InjectableElement.InjectionKind.POLYSUPPLIER;
            }
        } else {
            injectionKind = InjectableElement.InjectionKind.INSTANCE;
            injectedType = parameter.asClassType();
        }

        // Messaging
        AnnotationElement<Contextual> contextualAnn = parameter.getAnnotation(Contextual.class);
        InjectableElement.MessageKind messageKind = contextualAnn != null ?
            InjectableElement.MessageKind.INJECTION_POINT :
            InjectableElement.MessageKind.OUTER_MESSAGE;

        // Extra key types
        String named;
        AnnotationElement<Named> namedAnn = parameter.getAnnotation(Named.class);
        if (namedAnn != null) {
            if (injectionKind == InjectableElement.InjectionKind.MESSAGE) {
                throw CodegenException.of().message("@Named message injection").element(parameter.unwrap()).build();
            }
            named = namedAnn.unwrap().value();
        } else {
            named = null;
        }

        ClassType classed;
        AnnotationElement<Classed> classedAnn = parameter.getAnnotation(Classed.class);
        if (classedAnn != null) {
            if (injectionKind == InjectableElement.InjectionKind.MESSAGE) {
                throw CodegenException.of().message("@Classed message injection").element(parameter.unwrap()).build();
            }
            TypeMirror classifier = classedAnn.getValueTypeMirror(Classed::value);
            classed = new ClassType(getProcessingEnv(), (DeclaredType) classifier);
            // TODO: check  injectionKind
        } else {
            classed = null;
        }

        // Optional injection
        AnnotationElement<OptionalInject> optionalAnn = parameter.getAnnotation(OptionalInject.class);
        boolean optional = optionalAnn != null;

        return new InjectableElement(parentFactory, parameter, injectedType, injectionKind, messageKind, optional, named, classed);
    }

    protected DefaultFactoryElement createDefaultFactoryElement(IocletElement iocletElement, AnnotationElement<Produce> produceAnn) {
        TypeMirror suppliedTypeMirr = produceAnn.getValueTypeMirror(Produce::value);

        logger.debug("Parsing default factory for : " + suppliedTypeMirr.toString());

        TypeElement typeElement = (TypeElement) ((DeclaredType) suppliedTypeMirr).asElement();
        if (!(typeElement.getKind().isClass() || typeElement.getKind().isInterface())) {
            throw CodegenException.of().message("Unsupported type kind for:" + suppliedTypeMirr).element(iocletElement.getOriginProducer().unwrap()).build();
        }

        final ClassType suppliedType = new ClassType(getProcessingEnv(), (DeclaredType) suppliedTypeMirr);

        final String factoryMethodBaseName = "get" + suppliedType.asClassElement().getSimpleName();

        MethodElement constructor = getInjectableConstructor(suppliedType.asClassElement());
        if (constructor == null) {
            throw CodegenException.of().message("Unable to find injectable constructor for class: " + suppliedTypeMirr.toString()).build();
        }

        ScopeElement scope = obtainScope(suppliedType.asClassElement());
        if (scope == null) {
            scope = new ScopeElement(ScopeElement.ScopeKind.UNSCOPED, null);
        }

        final boolean polyproduce = produceAnn.unwrap().polyproduce();

        String named = StringUtils.isEmpty(produceAnn.unwrap().named()) ? null : produceAnn.unwrap().named();
        if (named == null) {
            // Get @Named from class definition
            AnnotationElement<Named> namedAnn = suppliedType.asClassElement().getAnnotation(Named.class);
            if (namedAnn != null) {
                named = namedAnn.unwrap().value();
            }
        }

        ClassType classed = null;
        TypeMirror classifier = produceAnn.getValueTypeMirror(Produce::classed);
        if (!Class.class.getName().equals(classifier.toString())) {
            classed = new ClassType(processingEnv, (DeclaredType) classifier);
        } else {
            // Get @Classed from class definition
            AnnotationElement<Classed> classedAnn = suppliedType.asClassElement().getAnnotation(Classed.class);
            if (classedAnn != null) {
                classifier = classedAnn.getValueTypeMirror(Classed::value);
                classed = new ClassType(processingEnv, (DeclaredType) classifier);
            }
        }

        final boolean notifyPostProduce = produceAnn.unwrap().postProduce();

        final boolean notifyPostConstruct = produceAnn.unwrap().postConstruct();

        final List<MethodElement> postConstructListeners = parsePostConstructListeners(suppliedType);

        if (BooleanUtils.toInteger(named != null)
            + BooleanUtils.toInteger(classed != null) > 1) {
            throw CodegenException.of().message("Ambiguous injection qualifiers for " + suppliedType.asClassElement().getName())
                .element(iocletElement.getOriginProducer().unwrap()).build();
        }

        final DefaultFactoryElement factory =
            new DefaultFactoryElement(
                suppliedType,
                factoryMethodBaseName,
                scope,
                polyproduce,
                named,
                classed,
                notifyPostProduce,
                notifyPostConstruct,
                postConstructListeners,
                constructor,
                produceAnn);

        for (ParameterElement param : constructor.getParameters()) {
            factory.addParameter(createInjectableElement(factory, param));
        }

        return factory;
    }

    protected CustomFactoryElement createCustomFactoryElement(MethodElement method) {
        logger.debug("Parse custom factory element: " + method);

        if (method.getReturnClassType() == null) {
            throw CodegenException.of().message("Producing method returns not a class or interface instance").element(method.unwrap()).build();
        }

        // suppliedType
        final ClassType suppliedType = method.getReturnClassType();

        // factoryMethodBaseName
        final String factoryMethodBaseName = method.getName();

        // Post produce listener
        AnnotationElement<PostProduce> postProduceAnn = method.getAnnotation(PostProduce.class);
        final PPLDefinitionElement postProduce;
        if (postProduceAnn != null) {
            TypeMirror classifier = postProduceAnn.getValueTypeMirror(PostProduce::withClassed);
            ClassType withClassed = null;
            if (!Class.class.getName().equals(classifier.toString())) {
                withClassed = new ClassType(processingEnv, (DeclaredType) classifier);
            }
            String withNamed = StringUtils.isNotBlank(postProduceAnn.unwrap().withNamed()) ? postProduceAnn.unwrap().withNamed() : null;
            postProduce = new PPLDefinitionElement(withNamed, withClassed);
        } else {
            postProduce = null;
        }

        // scope
        ScopeElement scope;
        if (postProduce == null) {
            scope = obtainScope(method);
            if (scope == null) {
                scope = obtainScope(suppliedType.asClassElement());
                if (scope == null) {
                    scope = new ScopeElement(ScopeElement.ScopeKind.UNSCOPED, null);
                }
            }
        } else {
            scope = new ScopeElement(ScopeElement.ScopeKind.SINGLETON, null);
        }

        // polyproduce
        boolean polyproduce = method.getAnnotation(Polyproduce.class) != null;

        // named
        String named = null;
        AnnotationElement<Named> methodNamedAnn = method.getAnnotation(Named.class);
        if (methodNamedAnn != null) {
            named = methodNamedAnn.unwrap().value();
        } else {
            if (postProduce == null) {
                // Get @Named from class definition
                AnnotationElement<Named> classNamedAnn = suppliedType.asClassElement().getAnnotation(Named.class);
                if (classNamedAnn != null) {
                    named = classNamedAnn.unwrap().value();
                }
            }
        }

        // classed
        AnnotationElement<Classed> methodClassedAnn = method.getAnnotation(Classed.class);
        ClassType classed = null;
        TypeMirror classifier;
        if (methodClassedAnn != null) {
            classifier = methodClassedAnn.getValueTypeMirror(Classed::value);
            classed = new ClassType(processingEnv, (DeclaredType) classifier);
        } else {
            if (postProduce == null) {
                // Get @Classed from class definition
                AnnotationElement<Classed> classClassedAnn = suppliedType.asClassElement().getAnnotation(Classed.class);
                if (classClassedAnn != null) {
                    classifier = classClassedAnn.getValueTypeMirror(Classed::value);
                    classed = new ClassType(processingEnv, (DeclaredType) classifier);
                }
            }
        }

        // notifyPostProduce notifyPostConstruct
        final boolean notifyPostProduce;
        final boolean notifyPostConstruct;
        AnnotationElement<ProducingOptions> optionsAnn = method.getAnnotation(ProducingOptions.class);
        if (optionsAnn != null) {
            notifyPostProduce = optionsAnn.unwrap().postProduce();
            notifyPostConstruct = optionsAnn.unwrap().postConstruct();
        } else {
            notifyPostProduce = false;
            notifyPostConstruct = false;
        }

        // postConstructListeners
        final List<MethodElement> postConstructListeners = parsePostConstructListeners(suppliedType);

        if (BooleanUtils.toInteger(StringUtils.isNotEmpty(named)) +
            BooleanUtils.toInteger(classed != null) +
            BooleanUtils.toInteger(postProduce != null)
            > 1
        ) {
            throw CodegenException.of().message("Ambiguous injection qualifiers").element(method.unwrap()).build();
        }

        final CustomFactoryElement factory = new CustomFactoryElement(
            suppliedType,
            factoryMethodBaseName,
            scope,
            polyproduce,
            postProduce,
            named,
            classed,
            notifyPostProduce,
            notifyPostConstruct,
            postConstructListeners,
            method);

        for (ParameterElement param : method.getParameters()) {
            factory.addParameter(createInjectableElement(factory, param));
        }

        logger.debug("Custom factory element has been created: " + factory);

        return factory;
    }

    protected void parseProducingMethods(IocletElement iocletElement) {
        // Scan producer methods
        List<MethodElement> methods = iocletElement.getOriginProducer().getMethodsFiltered(
            m -> !m.unwrap().getModifiers().contains(Modifier.FINAL) & m.unwrap().getModifiers().contains(Modifier.PUBLIC)
        );

        for (MethodElement method : methods) {
            logger.debug("Found custom factory method: " + method.getName());
            iocletElement.addFactory(createCustomFactoryElement(method));
        }
    }

    protected void paresProducingAnnotations(IocletElement iocletElement) {
        // Scan @Produce annotation
        ClassElement producer = iocletElement.getOriginProducer();
        List<AnnotationElement<Produce>> produceList = new ArrayList<>();
        AnnotationElement<Produces> produces = producer.getAnnotation(Produces.class);
        if (produces != null) {
            for (Produce produce : produces.unwrap().value()) {
                produceList.add(new AnnotationElement<>(getProcessingEnv(), produce));
            }
        } else {
            AnnotationElement<Produce> produce = producer.getAnnotation(Produce.class);
            if (produce != null) {
                produceList.add(produce);
            }
        }

        for (AnnotationElement<Produce> produce : produceList) {
            iocletElement.addFactory(createDefaultFactoryElement(iocletElement, produce));
        }
    }

    protected void logGenstamp(ClassElement producerElement) {
        Genstamp genstampAnn = producerElement.unwrap().getAnnotation(Genstamp.class);
        if (genstampAnn != null) {
            logger.debug(Genstamp.class.getSimpleName() + "{" +
                "Generator=" + genstampAnn.generator() +
                "; Timestamp=" + genstampAnn.timestamp() +
                "; HashId=" + genstampAnn.hashId() +
                "}");
        }
    }

    public IocletElement parse(ClassElement producerElement) {

        logger.debug("Parse producer: " + producerElement);
        logGenstamp(producerElement);

        String packageName = producerElement.getPackageName();

        if (!producerElement.checkPackageAccessibility(IOC_MODULE_NAME)) {
            ModuleElement producerModule = producerElement.getModule();
            String errMsg = String.format("Package %s must be exported from module %s to %s", packageName, producerModule.toString(), IOC_MODULE_NAME);
            logger.info(errMsg);
            //throw CodegenException.of().message(errMsg).element(producerModule).create();
        }

        String producerClassSimpleName = producerElement.getSimpleName();
        if (producerClassSimpleName.endsWith(IocletElement.PRODUCER_SUFFIX)) {
            producerClassSimpleName = producerClassSimpleName.substring(0, producerClassSimpleName.length() - IocletElement.PRODUCER_SUFFIX.length());
        }

        String iocletClassSimpleName = producerClassSimpleName + IocletElement.IOCLET_SUFFIX;

        String producerId = producerElement.getName();

        AnnotationElement<Producer> producerAnn = producerElement.getAnnotation(Producer.class);
        String producerRank = producerAnn.unwrap().value();

        IocletElement iocletElement = new IocletElement(producerElement, producerId, producerRank, iocletClassSimpleName, packageName);

        parseProducingMethods(iocletElement);
        paresProducingAnnotations(iocletElement);
        return iocletElement;
    }
}
