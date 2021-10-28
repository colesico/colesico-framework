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

package colesico.framework.ioc.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractParser;
import colesico.framework.assist.codegen.Genstamp;
import colesico.framework.assist.codegen.model.*;
import colesico.framework.ioc.codegen.model.*;
import colesico.framework.ioc.conditional.Condition;
import colesico.framework.ioc.conditional.Requires;
import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.listener.PostConstruct;
import colesico.framework.ioc.listener.PostProduce;
import colesico.framework.ioc.listener.ListenersControl;
import colesico.framework.ioc.message.Contextual;
import colesico.framework.ioc.message.Message;
import colesico.framework.ioc.production.*;
import colesico.framework.ioc.scope.CustomScope;
import colesico.framework.ioc.scope.Unscoped;
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
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
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
            AnnotationAssist<Inject> injectAnn = method.getAnnotation(Inject.class);
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

    protected ScopeElement obtainScopeFromElementAnn(ParserElement element) {

        AnnotationAssist<Singleton> singleton = element.getAnnotation(Singleton.class);
        if (singleton != null) {
            return new ScopeElement(ScopeElement.ScopeKind.SINGLETON, null);
        }

        AnnotationAssist<Unscoped> unscoped = element.getAnnotation(Unscoped.class);
        if (unscoped != null) {
            return new ScopeElement(ScopeElement.ScopeKind.UNSCOPED, null);
        }

        // Find custom scope
        ScopeElement result = null;
        for (AnnotationType am : element.getAnnotationTypes()) {
            ScopeElement scopeElm = null;

            AnnotationAssist<CustomScope> customScope = am.asElement().getAnnotation(CustomScope.class);
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

    protected ScopeElement obtainScopeProduceAnn(AnnotationAssist<Produce> produceAnn) {
        TypeMirror scopeMirror = produceAnn.getValueTypeMirror(Produce::scope);
        if (CodegenUtils.isAssignable(Annotation.class, scopeMirror, processingEnv)) {
            return null;
        }

        if (CodegenUtils.isAssignable(Singleton.class, scopeMirror, processingEnv)) {
            return new ScopeElement(ScopeElement.ScopeKind.SINGLETON, null);
        }

        if (CodegenUtils.isAssignable(Unscoped.class, scopeMirror, processingEnv)) {
            return new ScopeElement(ScopeElement.ScopeKind.UNSCOPED, null);
        }

        ClassType scopeAnn = new ClassType(getProcessingEnv(), (DeclaredType) scopeMirror);
        AnnotationAssist<CustomScope> customScope = scopeAnn.asClassElement().getAnnotation(CustomScope.class);
        if (customScope != null) {
            TypeMirror scopeClass = customScope.getValueTypeMirror(CustomScope::value);
            return new ScopeElement(ScopeElement.ScopeKind.CUSTOM, new ClassType(getProcessingEnv(), (DeclaredType) scopeClass));
        }

        throw CodegenException.of()
                .message("Invalid scope class: " + scopeMirror.toString())
                .element(((DeclaredType) scopeMirror).asElement())
                .build();

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
            throw CodegenException.of()
                    .message("Unsupported type kind for parameter " + parameter.getName()
                            + "; Type: " + parameter.getOriginType()
                            + "; Type kind: " + parameter.unwrap().getKind()

                    )
                    .element(parameter.unwrap())
                    .build();
        }

        AnnotationAssist<Message> messageAnn = parameter.getAnnotation(Message.class);
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
        AnnotationAssist<Contextual> contextualAnn = parameter.getAnnotation(Contextual.class);
        InjectableElement.MessageKind messageKind = contextualAnn != null ?
                InjectableElement.MessageKind.INJECTION_POINT :
                InjectableElement.MessageKind.OUTER_MESSAGE;

        // Extra key types
        String named;
        AnnotationAssist<Named> namedAnn = parameter.getAnnotation(Named.class);
        if (namedAnn != null) {
            if (injectionKind == InjectableElement.InjectionKind.MESSAGE) {
                throw CodegenException.of().message("@Named message injection").element(parameter.unwrap()).build();
            }
            named = namedAnn.unwrap().value();
        } else {
            named = null;
        }

        ClassifierType classed;
        AnnotationAssist<Classed> classedAnn = parameter.getAnnotation(Classed.class);
        if (classedAnn != null) {
            if (injectionKind == InjectableElement.InjectionKind.MESSAGE) {
                throw CodegenException.of().message("@Classed message injection").element(parameter.unwrap()).build();
            }
            TypeMirror classifier = classedAnn.getValueTypeMirror(Classed::value);
            classed = new ClassifierType(getProcessingEnv(), classifier);
            // TODO: check  injectionKind
        } else {
            classed = null;
        }

        // Optional injection
        AnnotationAssist<OptionalInject> optionalAnn = parameter.getAnnotation(OptionalInject.class);
        boolean optional = optionalAnn != null;

        return new InjectableElement(parentFactory, parameter, injectedType, injectionKind, messageKind, optional, named, classed);
    }

    protected DefaultFactoryElement createDefaultFactoryElement(IocletElement iocletElement, AnnotationAssist<Produce> produceAnn) {
        TypeMirror suppliedMirror = getTypeUtils().erasure(produceAnn.getValueTypeMirror(Produce::value));

        logger.debug("Parsing default factory for : " + suppliedMirror);

        TypeElement typeElement = (TypeElement) ((DeclaredType) suppliedMirror).asElement();
        if (!(typeElement.getKind().isClass() || typeElement.getKind().isInterface())) {
            throw CodegenException.of().message("Unsupported type kind for:" + suppliedMirror).element(iocletElement.getOriginProducer().unwrap()).build();
        }

        final ClassType suppliedType = new ClassType(getProcessingEnv(), (DeclaredType) suppliedMirror);

        final String factoryMethodBaseName = "get" + suppliedType.asClassElement().getSimpleName();

        MethodElement constructor = getInjectableConstructor(suppliedType.asClassElement());
        if (constructor == null) {
            throw CodegenException.of().message("Unable to find injectable constructor for class: " + suppliedMirror.toString()).build();
        }

        // Detect scope
        ScopeElement scope = obtainScopeProduceAnn(produceAnn);
        if (scope == null) {
            scope = obtainScopeFromElementAnn(suppliedType.asClassElement());
            if (scope == null) {
                scope = new ScopeElement(ScopeElement.ScopeKind.UNSCOPED, null);
            }
        }

        // Polyproduce

        final boolean polyproduce = produceAnn.unwrap().polyproduce();

        // Named

        String named = StringUtils.isEmpty(produceAnn.unwrap().named()) ? null : produceAnn.unwrap().named();
        if (named == null) {
            // Get @Named from class definition
            AnnotationAssist<Named> namedAnn = suppliedType.asClassElement().getAnnotation(Named.class);
            if (namedAnn != null) {
                named = namedAnn.unwrap().value();
            }
        }

        // Classed

        ClassifierType classed = null;
        TypeMirror classifier = produceAnn.getValueTypeMirror(Produce::classed);
        if (!CodegenUtils.isAssignable(Class.class, classifier, processingEnv)) {
            classed = new ClassifierType(processingEnv, classifier);
        } else {
            // Get @Classed from class definition
            AnnotationAssist<Classed> classedAnn = suppliedType.asClassElement().getAnnotation(Classed.class);
            if (classedAnn != null) {
                classifier = classedAnn.getValueTypeMirror(Classed::value);
                classed = new ClassifierType(processingEnv, classifier);
            }
        }

        // Listeners

        final boolean notifyPostProduce = produceAnn.unwrap().postProduce();

        final boolean notifyPostConstruct = produceAnn.unwrap().postConstruct();

        final List<MethodElement> postConstructListeners = parsePostConstructListeners(suppliedType);

        if (BooleanUtils.toInteger(named != null)
                + BooleanUtils.toInteger(classed != null) > 1) {
            throw CodegenException.of().message("Ambiguous injection qualifiers for " + suppliedType.asClassElement().getName())
                    .element(iocletElement.getOriginProducer().unwrap()).build();
        }

        // Condition

        ConditionElement condition = null;
        TypeMirror conditionClass = produceAnn.getValueTypeMirror(Produce::requires);
        if (!CodegenUtils.isAssignable(Condition.class, conditionClass, processingEnv)) {
            condition = new ConditionElement(new ClassType(getProcessingEnv(), (DeclaredType) conditionClass));
        } else {
            AnnotationAssist<Requires> reqAnn = iocletElement.getOriginProducer().getAnnotation(Requires.class);
            if (reqAnn != null) {
                condition = new ConditionElement(new ClassType(getProcessingEnv(), (DeclaredType) reqAnn.getValueTypeMirror(Requires::value)));
            }
        }

        final DefaultFactoryElement factory =
                new DefaultFactoryElement(
                        suppliedType,
                        factoryMethodBaseName,
                        scope,
                        condition,
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

        // Supertypes
        TypeMirror[] supertypes = produceAnn.getValueTypeMirrors(a -> a.keyType());
        for (TypeMirror st : supertypes) {
            factory.addSupertype(new ClassType(processingEnv, (DeclaredType) st));
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

        // Detect post produce listener
        final PostProduceElement postProduce;
        AnnotationAssist<PostProduce> postProduceAnn = method.getAnnotation(PostProduce.class);
        if (postProduceAnn != null) {
            // Process withClassed
            TypeMirror classifier = postProduceAnn.getValueTypeMirror(PostProduce::withClassed);
            ClassifierType withClassed = null;
            if (!CodegenUtils.isAssignable(Class.class, classifier, processingEnv)) {
                withClassed = new ClassifierType(processingEnv, classifier);
            }
            // Process withNamed
            String withNamed = StringUtils.isNotBlank(postProduceAnn.unwrap().withNamed()) ? postProduceAnn.unwrap().withNamed() : null;
            postProduce = new PostProduceElement(withNamed, withClassed);
        } else {
            postProduce = null;
        }

        // Detect scope
        ScopeElement scope;
        if (postProduce == null) {
            scope = obtainScopeFromElementAnn(method);
            if (scope == null) {
                scope = obtainScopeFromElementAnn(suppliedType.asClassElement());
                if (scope == null) {
                    scope = new ScopeElement(ScopeElement.ScopeKind.UNSCOPED, null);
                }
            }
        } else {
            //  Post produce listener  always is the singleton
            scope = new ScopeElement(ScopeElement.ScopeKind.SINGLETON, null);
        }

        // Polyproduce
        boolean polyproduce = method.getAnnotation(Polyproduce.class) != null;

        // Named
        String named = null;
        AnnotationAssist<Named> methodNamedAnn = method.getAnnotation(Named.class);
        if (methodNamedAnn != null) {
            named = methodNamedAnn.unwrap().value();
        } else {
            if (postProduce == null) {
                // Get @Named from class definition
                AnnotationAssist<Named> classNamedAnn = suppliedType.asClassElement().getAnnotation(Named.class);
                if (classNamedAnn != null) {
                    named = classNamedAnn.unwrap().value();
                }
            }
        }

        // Classed
        AnnotationAssist<Classed> methodClassedAnn = method.getAnnotation(Classed.class);
        ClassifierType classed = null;
        TypeMirror classifier;
        if (methodClassedAnn != null) {
            classifier = methodClassedAnn.getValueTypeMirror(Classed::value);
            classed = new ClassifierType(processingEnv, classifier);
        } else {
            if (postProduce == null) {
                // Get @Classed from class definition
                AnnotationAssist<Classed> classClassedAnn = suppliedType.asClassElement().getAnnotation(Classed.class);
                if (classClassedAnn != null) {
                    classifier = classClassedAnn.getValueTypeMirror(Classed::value);
                    classed = new ClassifierType(processingEnv, classifier);
                }
            }
        }

        // notifyPostProduce notifyPostConstruct
        final boolean notifyPostProduce;
        final boolean notifyPostConstruct;
        AnnotationAssist<ListenersControl> listenersControlAnn = method.getAnnotation(ListenersControl.class);
        if (listenersControlAnn != null) {
            notifyPostProduce = listenersControlAnn.unwrap().postProduce();
            notifyPostConstruct = listenersControlAnn.unwrap().postConstruct();
        } else {
            // For custom production, listeners can be called manually or
            // there is no need to call them (for the case with services)
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

        // Condition
        AnnotationAssist<Requires> reqAnn = method.getAnnotation(Requires.class);
        if (reqAnn == null) {
            reqAnn = method.getParentClass().getAnnotation(Requires.class);
        }
        ConditionElement condition = null;
        if (reqAnn != null) {
            condition = new ConditionElement(new ClassType(getProcessingEnv(), (DeclaredType) reqAnn.getValueTypeMirror(Requires::value)));
        }

        // Substitution
        AnnotationAssist<Substitute> subsAnn = method.getAnnotation(Substitute.class);
        if (subsAnn == null) {
            subsAnn = method.getParentClass().getAnnotation(Substitute.class);
        }
        SubstitutionElement substitution = null;
        if (subsAnn != null) {
            substitution = new SubstitutionElement(subsAnn.unwrap().value());
        }

        final CustomFactoryElement factory = new CustomFactoryElement(
                suppliedType,
                factoryMethodBaseName,
                scope,
                condition,
                substitution,
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

        // Supertypes
        AnnotationAssist<KeyType> supertypesAnn = method.getAnnotation(KeyType.class);
        if (supertypesAnn != null) {
            TypeMirror[] supertypes = supertypesAnn.getValueTypeMirrors(a -> a.value());
            for (TypeMirror st : supertypes) {
                factory.addSupertype(new ClassType(processingEnv, (DeclaredType) st));
            }
        }

        logger.debug("Custom factory element has been created: " + factory);

        return factory;
    }

    protected void parseProducingMethods(IocletElement iocletElement) {
        // Scan producer methods
        List<MethodElement> methods = iocletElement.getOriginProducer().getMethodsFiltered(
                m -> !m.unwrap().getModifiers().contains(Modifier.FINAL)
                        && m.unwrap().getModifiers().contains(Modifier.PUBLIC)
                        && !m.unwrap().getModifiers().contains(Modifier.STATIC)
        );

        for (MethodElement method : methods) {
            logger.debug("Found custom factory method: " + method.getName());
            CustomFactoryElement factoryElm = createCustomFactoryElement(method);
            iocletElement.addFactory(factoryElm);
            checkSupertypes(factoryElm);
        }
    }

    protected void paresProducingAnnotations(IocletElement iocletElement) {
        // Scan @Produce annotation
        ClassElement producer = iocletElement.getOriginProducer();
        List<AnnotationAssist<Produce>> produceList = new ArrayList<>();
        AnnotationAssist<Produces> produces = producer.getAnnotation(Produces.class);
        if (produces != null) {
            for (Produce produce : produces.unwrap().value()) {
                produceList.add(new AnnotationAssist<>(getProcessingEnv(), produce));
            }
        } else {
            AnnotationAssist<Produce> produce = producer.getAnnotation(Produce.class);
            if (produce != null) {
                produceList.add(produce);
            }
        }

        for (AnnotationAssist<Produce> produce : produceList) {
            DefaultFactoryElement factElm = createDefaultFactoryElement(iocletElement, produce);
            iocletElement.addFactory(factElm);
            checkSupertypes(factElm);
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

    protected void checkSupertypes(FactoryElement facElm) {
        Types typeUtils = getTypeUtils();
        for (ClassType supType : facElm.getKeyTypes()) {
            if (!typeUtils.isAssignable(facElm.getSuppliedType().unwrap(), supType.unwrap())) {
                throw CodegenException.of()
                        .message("Not an instance class subtype: " + facElm.getSuppliedType().getName() + " extends/implements " + supType.unwrap().toString())
                        .element(facElm.getOriginElement())
                        .build();
            }
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

        String iocletId = producerElement.getName();

        IocletElement iocletElement = new IocletElement(producerElement, iocletId, iocletClassSimpleName, packageName);

        parseProducingMethods(iocletElement);
        paresProducingAnnotations(iocletElement);

        return iocletElement;
    }
}
