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

import colesico.framework.config.ConfigPrototype;
import colesico.framework.config.Configuration;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
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

    protected final ProcessingEnvironment processingEnv;
    protected final Elements elementUtils;

    protected final Map<String, ByRankMap> byPackageMap = new HashMap<>();

    public ConfRegistry(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        elementUtils = processingEnv.getElementUtils();
    }

    public boolean isEmpty() {
        return byPackageMap.isEmpty();
    }

    public void clear() {
        byPackageMap.clear();
    }

    public Map<String, ByRankMap> getByPackageMap() {
        return byPackageMap;
    }

    protected TypeElement getConfigBaseClass(TypeElement classElement) {
        TypeMirror superClass = null;
        do {
            superClass = classElement.getSuperclass();
            ConfigPrototype cpAnn = ((DeclaredType) superClass).asElement().getAnnotation(ConfigPrototype.class);
            if (cpAnn != null) {
                return (TypeElement) ((DeclaredType) superClass).asElement();
            }
        } while (!superClass.toString().equals(Object.class.getName()));

        throw  CodegenException.of().message("Unable to determine configuration prototype for: " + classElement.asType().toString()).element(classElement).build();
    }

    public ConfigElement register(TypeElement classElement) {

        String packageName = CodegenUtils.getPackageName(classElement);
        ByRankMap byRankMap = byPackageMap.computeIfAbsent(packageName, k -> new ByRankMap());

        Configuration configurationAnn = classElement.getAnnotation(Configuration.class);
        String rank = configurationAnn.rank();
        List<ConfigElement> rankConfigs = byRankMap.computeIfAbsent(rank, k -> new ArrayList<>());

        TypeElement prototypeClassElement = getConfigBaseClass(classElement);
        ConfigElement configurationElement = new ConfigElement(classElement, prototypeClassElement, rank);
        rankConfigs.add(configurationElement);

        logger.debug("Configuration " + configurationElement.getImplementation().asType().toString() + " has been registered");
        return configurationElement;
    }

    public static class ByRankMap extends HashMap<String, List<ConfigElement>> {
    }
}
