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
import colesico.framework.assist.codegen.FrameworkAbstractParser;
import colesico.framework.assist.codegen.model.AnnotationElement;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.config.*;
import colesico.framework.ioc.Classed;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Named;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

/**
 * @author Vladlen Larionov
 */
public class ConfigParser extends FrameworkAbstractParser {

    private Logger logger = LoggerFactory.getLogger(ConfigParser.class);

    public ConfigParser(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    private ClassElement getConfigPrototypeClass(ClassElement configImplementation) {
        TypeElement superClass = configImplementation.unwrap();
        do {
            superClass = (TypeElement) ((DeclaredType) superClass.getSuperclass()).asElement();
            ConfigPrototype cpAnn = superClass.getAnnotation(ConfigPrototype.class);
            if (cpAnn != null) {
                return new ClassElement(processingEnv, superClass);
            }
        } while (!superClass.getSimpleName().toString().equals("Object"));

        logger.debug("Unable to determine configuration prototype for: " + configImplementation.getName());

        return null;
    }

    private ConfigElement createConfigElement(ClassElement configImplementation) {

        AnnotationElement<Config> configurationAnn = configImplementation.getAnnotation(Config.class);
        String rank = configurationAnn.unwrap().rank();

        ClassElement configPrototype = getConfigPrototypeClass(configImplementation);

        ConfigModel model;
        ClassElement target;
        if (configPrototype != null) {
            AnnotationElement<ConfigPrototype> prototypeAnn = configPrototype.getAnnotation(ConfigPrototype.class);
            model = prototypeAnn.unwrap().model();
            TypeMirror targetMirror = prototypeAnn.getValueTypeMirror(ConfigPrototype::target);
            if (targetMirror.toString().equals(Object.class.getName())) {
                target = null;
            } else {
                target = new ClassElement(processingEnv, (DeclaredType) targetMirror);
            }
        } else {
            target = null;
            model = ConfigModel.SINGLE;
        }

        AnnotationElement<DefaultConfig> defaultAnn = configImplementation.getAnnotation(DefaultConfig.class);
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

        AnnotationElement<Classed> classedAnn = configImplementation.getAnnotation(Classed.class);
        TypeMirror classed;
        if (classedAnn != null) {
            classed = classedAnn.getValueTypeMirror(Classed::value);
        } else {
            classed = null;
        }

        AnnotationElement<Named> namedAnn = configImplementation.getAnnotation(Named.class);
        String named = namedAnn == null ? null : namedAnn.unwrap().value();

        // Config source
        AnnotationElement<UseSource> useSourceAnn = configImplementation.getAnnotation(UseSource.class);
        ConfigSourceElement sourceElm = null;
        if (useSourceAnn != null) {
            TypeMirror driverType = useSourceAnn.getValueTypeMirror(UseSource::type);
            ClassType driverClassType = new ClassType(processingEnv, (DeclaredType) driverType);
            String[] params = useSourceAnn.unwrap().params();
            sourceElm = new ConfigSourceElement(driverClassType, params, useSourceAnn.unwrap().bindAll());
            parseSourceValues(configImplementation, sourceElm);
        }

        return new ConfigElement(configImplementation, configPrototype, rank, model, target, sourceElm, defaultMessage, classed, named);
    }

    private ConfigSourceElement parseSourceValues(ClassElement configImplementation, ConfigSourceElement confSourceElm) {
        for (FieldElement me : configImplementation.getFields()) {
            AnnotationElement<FromSource> sourceValueAnn = me.getAnnotation(FromSource.class);
            if (sourceValueAnn != null) {
                String query = me.getName();
                if (StringUtils.isNotBlank(sourceValueAnn.unwrap().value())) {
                    query = sourceValueAnn.unwrap().value();
                }
                confSourceElm.addSourceValue(new SourceValueElement(me, query));
            } else if (confSourceElm.isBindAll()) {
                confSourceElm.addSourceValue(new SourceValueElement(me, me.getName()));
            }
        }
        return confSourceElm;
    }

    public ConfigElement parse(ClassElement configImplElement) {
        ConfigElement configElement = createConfigElement(configImplElement);
        logger.debug("Configuration " + configElement.getImplementation().getName() + " has been parsed");
        return configElement;
    }

}
