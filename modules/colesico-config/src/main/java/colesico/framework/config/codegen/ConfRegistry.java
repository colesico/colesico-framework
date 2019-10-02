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
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.model.AnnotationElement;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import colesico.framework.config.Configuration;
import colesico.framework.config.Default;
import colesico.framework.ioc.Classed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Named;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vladlen Larionov
 */
public class ConfRegistry {

    private Logger logger = LoggerFactory.getLogger(ConfRegistry.class);

    private final ProcessingEnvironment processingEnv;
    private final List<ConfigElement> configElements = new ArrayList<>();

    public ConfRegistry(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    public boolean isEmpty() {
        return configElements.isEmpty();
    }

    public void clear() {
        configElements.clear();
    }

    public List<ConfigElement> getConfigElements() {
        return configElements;
    }

    private ClassElement getConfigBaseClass(ClassElement configImplementation) {
        TypeElement superClass = configImplementation.unwrap();
        do {
            superClass = (TypeElement) ((DeclaredType) superClass.getSuperclass()).asElement();
            ConfigPrototype cpAnn = superClass.getAnnotation(ConfigPrototype.class);
            if (cpAnn != null) {
                return new ClassElement(processingEnv, superClass);
            }
        } while (!superClass.getSimpleName().toString().equals("Object"));

        throw CodegenException.of().message("Unable to determine configuration prototype for: " + configImplementation.getName()).element(configImplementation).build();
    }

    private ConfigElement createConfigElement(ClassElement configImplementation) {

        ClassElement configPrototype = getConfigBaseClass(configImplementation);

        AnnotationElement<Configuration> configurationAnn = configImplementation.getAnnotation(Configuration.class);
        String rank = configurationAnn.unwrap().rank();

        AnnotationElement<ConfigPrototype> prototypeAnn = configPrototype.getAnnotation(ConfigPrototype.class);
        ConfigModel model = prototypeAnn.unwrap().model();
        TypeMirror targetMirror = prototypeAnn.getValueTypeMirror(a -> a.target());
        ClassElement target;
        if (targetMirror.toString().equals(Object.class.getName())) {
            target = null;
        } else {
            target = new ClassElement(processingEnv, (DeclaredType) targetMirror);
        }

        AnnotationElement<Default> defaultAnn = configImplementation.getAnnotation(Default.class);
        boolean defaultMessage;
        if (defaultAnn != null) {
            if (!ConfigModel.MESSAGE.equals(model)) {
                throw CodegenException.of().message("@" + Default.class.getSimpleName() +
                    " annotation can be applied only to " + ConfigModel.MESSAGE.name() + " configuration model").build();
            }
            defaultMessage = true;
        } else {
            defaultMessage = false;
        }

        AnnotationElement<Classed> classedAnn = configImplementation.getAnnotation(Classed.class);
        TypeMirror classed;
        if (classedAnn != null) {
            classed = classedAnn.getValueTypeMirror(a -> a.value());
        } else {
            classed = null;
        }

        AnnotationElement<Named> namedAnn = configImplementation.getAnnotation(Named.class);
        String named = namedAnn == null ? null : namedAnn.unwrap().value();

        return new ConfigElement(configImplementation, configPrototype, rank, model, target, defaultMessage, classed, named);
    }

    public ConfigElement register(ClassElement configImplElement) {
        ConfigElement configElement = createConfigElement(configImplElement);
        configElements.add(configElement);
        logger.debug("Configuration " + configElement.getImplementation().getName() + " has been registered");
        return configElement;
    }

}
