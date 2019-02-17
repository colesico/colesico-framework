package colesico.framework.ioc.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractParser;
import colesico.framework.assist.codegen.Genstamp;
import colesico.framework.assist.codegen.model.*;
import colesico.framework.ioc.*;
import colesico.framework.ioc.codegen.model.*;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.lang.model.element.*;
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
            return new ScopeElement(null, ScopeElement.ScopeKind.SINGLETON);
        }

        AnnotationElement<Unscoped> unscoped = element.getAnnotation(Unscoped.class);
        if (unscoped != null) {
            return new ScopeElement(null, ScopeElement.ScopeKind.UNSCOPED);
        }

        ScopeElement result = null;
        for (AnnotationMirrorElement am : element.getAnnotationMirrors()) {
            ScopeElement scopeElm = null;

            AnnotationElement<CustomScope> customScope = am.getAnnotation(CustomScope.class);
            if (customScope != null) {
                TypeMirror scopeClass = customScope.getValueTypeMirror(a -> a.value());
                scopeElm = new ScopeElement(
                        new ClassType(getProcessingEnv(), (DeclaredType) scopeClass), ScopeElement.ScopeKind.CUSTOM
                );
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

        // Binding
        AnnotationElement<InlineInject> dynamicInjectAnn = parameter.getAnnotation(InlineInject.class);
        InjectableElement.LinkagePhase linkagePhase = dynamicInjectAnn == null ?
                InjectableElement.LinkagePhase.ACTIVATION :
                InjectableElement.LinkagePhase.PRODUCTION;

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

        String classed;
        AnnotationElement<Classed> classedAnn = parameter.getAnnotation(Classed.class);
        if (classedAnn != null) {
            if (injectionKind == InjectableElement.InjectionKind.MESSAGE) {
                throw CodegenException.of().message("@Classed message injection").element(parameter.unwrap()).build();
            }
            TypeMirror classifier = classedAnn.getValueTypeMirror(a -> a.value());
            classed = classifier.toString();
            // TODO: check  injectionKind
        } else {
            classed = null;
        }

        return new InjectableElement(parentFactory, parameter, injectedType, injectionKind, messageKind, linkagePhase, named, classed);
    }

    protected CustomFactoryElement createCustomFactoryElement(MethodElement method) {
        logger.debug("Parse custom factory element: " + method);

        if (method.getReturnClassType() == null) {
            throw CodegenException.of().message("Producing method returns not a class or interface instance").element(method.unwrap()).build();
        }

        ClassType suppliedType = method.getReturnClassType();

        // get scope
        ScopeElement scope = obtainScope(method);
        if (scope == null) {
            scope = obtainScope(suppliedType.asClassElement());
            if (scope == null) {
                scope = new ScopeElement(null, ScopeElement.ScopeKind.UNSCOPED);
            }
        }

        boolean polyproduce = method.getAnnotation(Polyproduce.class) != null;

        AnnotationElement<Named> namedAnn = method.getAnnotation(Named.class);
        String named = namedAnn == null ? null : namedAnn.unwrap().value();

        AnnotationElement<Classed> classedAnn = method.getAnnotation(Classed.class);
        String classed;
        if (classedAnn != null) {
            TypeMirror classifier = classedAnn.getValueTypeMirror(a -> a.value());
            classed = classifier.toString();
        } else {
            classed = null;
        }

        if (StringUtils.isNotEmpty(named) && classed != null) {
            CodegenException.of().message("Ambiguous injection qualifiers").element(method.unwrap()).build();
        }

        String factoryMethodbaseName = method.getName();

        CustomFactoryElement factory = new CustomFactoryElement(suppliedType, factoryMethodbaseName, scope, polyproduce, named, classed, method);

        for (ParameterElement param : method.getParameters()) {
            factory.addParameter(createInjectableElement(factory, param));
        }
        logger.debug("Custom factory element has been created: " + factory);
        return factory;
    }

    protected DefaultFactoryElement createDefaultFactoryElement(IocletElement iocletElement, AnnotationElement<Produce> produceAnn) {
        TypeMirror suppliedTypeMirr = produceAnn.getValueTypeMirror(p -> p.value());

        logger.debug("Parsing default factory for : " + suppliedTypeMirr.toString());

        TypeElement typeElement = (TypeElement) ((DeclaredType) suppliedTypeMirr).asElement();
        if (!(typeElement.getKind().isClass() || typeElement.getKind().isInterface())) {
            throw CodegenException.of().message("Unsupported type kind for:" + suppliedTypeMirr).element(iocletElement.getOriginProducer().unwrap()).build();
        }

        ClassType suppliedType = new ClassType(getProcessingEnv(), (DeclaredType) suppliedTypeMirr);

        // moduleMetamodel.addExports(CodegenUtils.getPackageName(typeElm));

        MethodElement constructor = getInjectableConstructor(suppliedType.asClassElement());
        if (constructor == null) {
            throw CodegenException.of().message("Unable to find injectable constructor for class: " + suppliedTypeMirr.toString()).build();
        }

        ScopeElement scope = obtainScope(suppliedType.asClassElement());
        if (scope == null) {
            scope = new ScopeElement(null, ScopeElement.ScopeKind.UNSCOPED);
        }

        boolean polyproduce = produceAnn.unwrap().polyproduce();

        String named = StringUtils.isEmpty(produceAnn.unwrap().named()) ? null : produceAnn.unwrap().named();
        if (named == null) {
            //TODO: check annotation @Named on target class ?
        }

        String classed;
        TypeMirror classifier = produceAnn.getValueTypeMirror(a -> a.classed());
        if (!Class.class.getName().equals(classifier.toString())) {
            classed = classifier.toString();
        } else {
            classed = null;
            //TODO: check annotation @Classed on target class ?
        }

        if (named != null && classed != null) {
            CodegenException.of().message("Ambiguous injection qualifiers for " + suppliedType.asClassElement().getName())
                    .element(iocletElement.getOriginProducer().unwrap()).build();
        }

        String factoryMethodBaseName = "get" + suppliedType.asClassElement().getSimpleName();

        DefaultFactoryElement factory =
                new DefaultFactoryElement(suppliedType, factoryMethodBaseName, scope, polyproduce, named, classed, constructor, produceAnn);

        for (ParameterElement param : constructor.getParameters()) {
            factory.addParameter(createInjectableElement(factory, param));
        }

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
