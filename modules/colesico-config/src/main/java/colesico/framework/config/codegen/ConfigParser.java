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

package colesico.framework.config.codegen;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractParser;
import colesico.framework.assist.codegen.model.*;
import colesico.framework.config.*;
import colesico.framework.ioc.conditional.Requires;
import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.conditional.Substitution;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.scope.CustomScope;
import colesico.framework.ioc.scope.Unscoped;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.HashMap;
import java.util.Map;

import static colesico.framework.config.UseFileSource.*;

/**
 * @author Vladlen Larionov
 */
public class ConfigParser extends FrameworkAbstractParser {

    private Logger logger = LoggerFactory.getLogger(ConfigParser.class);

    public ConfigParser(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    private ClassElement getConfigPrototypeClass(ClassElement config) {
        TypeElement superClass = config.unwrap();
        do {
            superClass = (TypeElement) ((DeclaredType) superClass.getSuperclass()).asElement();
            ConfigPrototype cpAnn = superClass.getAnnotation(ConfigPrototype.class);
            if (cpAnn != null) {
                return ClassElement.fromElement(processingEnv, superClass);
            }
        } while (!superClass.getSimpleName().toString().equals("Object"));

        logger.debug("Unable to determine configuration prototype for: " + config.getName());

        return null;
    }

    private ConfigScopedElement obtainConfigScope(ClassElement config) {

        ClassType scoped;

        AnnotationAssist<Unscoped> unscAnn = config.getAnnotation(Unscoped.class);
        if (unscAnn != null) {
            scoped = new ClassType(getProcessingEnv(), (DeclaredType) CodegenUtils.classToTypeMirror(Unscoped.class, getElementUtils()));
            return new ConfigScopedElement(scoped, true);
        }

        AnnotationAssist<Singleton> singAnn = config.getAnnotation(Singleton.class);
        if (singAnn != null) {
            scoped = new ClassType(getProcessingEnv(), (DeclaredType) CodegenUtils.classToTypeMirror(Singleton.class, getElementUtils()));
            return new ConfigScopedElement(scoped, true);
        }

        // Find custom scope declaration
        for (AnnotationType am : config.getAnnotationTypes()) {
            AnnotationAssist<CustomScope> customScope = am.asElement().getAnnotation(CustomScope.class);
            if (customScope != null) {
                scoped = new ClassType(getProcessingEnv(), (DeclaredType) am.unwrap());
                return new ConfigScopedElement(scoped, true);
            }
        }

        scoped = new ClassType(getProcessingEnv(), (DeclaredType) CodegenUtils.classToTypeMirror(Singleton.class, getElementUtils()));
        return new ConfigScopedElement(scoped, false);
    }

    private ConfigElement parseConfigElement(ClassElement config) {

        ClassElement configPrototype = getConfigPrototypeClass(config);

        ConfigModel model;
        ClassElement target;
        if (configPrototype != null) {
            AnnotationAssist<ConfigPrototype> prototypeAnn = configPrototype.getAnnotation(ConfigPrototype.class);
            model = prototypeAnn.unwrap().model();
            TypeMirror targetMirror = prototypeAnn.getValueTypeMirror(ConfigPrototype::target);
            if (CodegenUtils.isAssignable(Object.class, targetMirror, processingEnv)) {
                target = null;
            } else {
                target = ClassElement.fromType(processingEnv, (DeclaredType) targetMirror);
            }
        } else {
            target = null;
            model = ConfigModel.SINGLE;
        }

        AnnotationAssist<DefaultConfig> defaultAnn = config.getAnnotation(DefaultConfig.class);
        boolean defaultMessage;
        if (defaultAnn != null) {
            if (!ConfigModel.MESSAGE.equals(model)) {
                throw CodegenException.of().message("@" + DefaultConfig.class.getSimpleName() +
                        " annotation can be applied only to " + ConfigModel.MESSAGE.name() + " configuration model").build();
            }
            defaultMessage = true;
        } else {
            defaultMessage = false;
        }

        // Classed
        AnnotationAssist<Classed> classedAnn = config.getAnnotation(Classed.class);
        TypeMirror classed;
        if (classedAnn != null) {
            classed = classedAnn.getValueTypeMirror(Classed::value);
        } else {
            classed = null;
        }

        // Named
        AnnotationAssist<Named> namedAnn = config.getAnnotation(Named.class);
        String named = namedAnn == null ? null : namedAnn.unwrap().value();

        // Condition
        AnnotationAssist<Requires> reqAnn = config.getAnnotation(Requires.class);
        ClassType condition = null;
        if (reqAnn != null) {
            condition = new ClassType(getProcessingEnv(), (DeclaredType) reqAnn.getValueTypeMirror(a -> a.value()));
        }

        // Substitution
        AnnotationAssist<Substitute> subsAnn = config.getAnnotation(Substitute.class);
        Substitution substitution = null;
        if (subsAnn != null) {
            substitution = subsAnn.unwrap().value();
        }

        // Detect scope
        ConfigScopedElement scope = obtainConfigScope(config);

        ConfigElement configElement = new ConfigElement(config, configPrototype, condition, substitution, model, scope, target, defaultMessage, classed, named);

        // Config source
        AnnotationAssist<UseSource> useSourceAnn = config.getAnnotation(UseSource.class);
        AnnotationAssist<UseFileSource> useFileSourceAnn = config.getAnnotation(UseFileSource.class);
        ConfigSourceElement sourceElm = null;
        if (useSourceAnn != null || useFileSourceAnn != null) {
            TypeMirror sourceType;
            boolean bindAll;
            if (useSourceAnn != null) {
                sourceType = useSourceAnn.getValueTypeMirror(UseSource::type);
                bindAll = useSourceAnn.unwrap().bindAll();
            } else {
                sourceType = useFileSourceAnn.getValueTypeMirror(UseFileSource::type);
                bindAll = useFileSourceAnn.unwrap().bindAll();
            }
            ClassType sourceClassType = new ClassType(processingEnv, (DeclaredType) sourceType);
            Map<String, String> options = parseSourceOptions(config);
            sourceElm = new ConfigSourceElement(configElement, sourceClassType, options, bindAll);
            configElement.setSource(sourceElm);
            parseSourceValues(config, sourceElm);
        }

        return configElement;
    }

    private Map<String, String> parseSourceOptions(ClassElement configImpl) {
        Map<String, String> result = new HashMap<>();

        AnnotationAssist<UseFileSource> fileSourceAnn = configImpl.getAnnotation(UseFileSource.class);
        if (fileSourceAnn != null) {
            if (StringUtils.isNotBlank(fileSourceAnn.unwrap().file())) {
                result.put(FILE_OPTION, fileSourceAnn.unwrap().file());
            }
            if (StringUtils.isNotBlank(fileSourceAnn.unwrap().directory())) {
                result.put(DIRECTORY_OPTION, fileSourceAnn.unwrap().directory());
            }
            if (StringUtils.isNotBlank(fileSourceAnn.unwrap().classpath())) {
                result.put(CLASSPATH_OPTION, fileSourceAnn.unwrap().classpath());
            }
            if (StringUtils.isNotBlank(fileSourceAnn.unwrap().prefix())) {
                result.put(PREFIX_OPTION, fileSourceAnn.unwrap().prefix());
            }
        }

        AnnotationAssist<SourceOptions> sourceOptionsAnn = configImpl.getAnnotation(SourceOptions.class);
        if (sourceOptionsAnn == null) {
            AnnotationAssist<SourceOption> sourceOptAnn = configImpl.getAnnotation(SourceOption.class);
            if (sourceOptAnn != null) {
                result.put(sourceOptAnn.unwrap().name(), sourceOptAnn.unwrap().value());
            }
        } else {
            for (SourceOption sourceOption : sourceOptionsAnn.unwrap().value()) {
                result.put(sourceOption.name(), sourceOption.value());
            }
        }

        return result;
    }

    private ConfigSourceElement parseSourceValues(ClassElement configImplementation, ConfigSourceElement confSourceElm) {
        for (FieldElement me : configImplementation.getFields()) {
            AnnotationAssist<FromSource> fromSrcAnn = me.getAnnotation(FromSource.class);
            AnnotationAssist<NotFromSource> notFromSrcAnn = me.getAnnotation(NotFromSource.class);
            if (fromSrcAnn != null && notFromSrcAnn != null) {
                throw CodegenException.of().message("Simultaneous usage of @FromSource ana @NotFromSource").element(confSourceElm.getSourceType().asClassElement()).build();
            }
            if ((fromSrcAnn != null) || (confSourceElm.isBindAll() && (notFromSrcAnn == null))) {
                confSourceElm.addSourceValue(new SourceValueElement(me));
            }
        }
        return confSourceElm;
    }

    public ConfigElement parse(ClassElement configImplElement) {
        ConfigElement configElement = parseConfigElement(configImplElement);
        logger.debug("Configuration " + configElement.getOriginClass().getName() + " has been parsed");
        return configElement;
    }

}
