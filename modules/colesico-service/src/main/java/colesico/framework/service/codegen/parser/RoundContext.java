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

package colesico.framework.service.codegen.parser;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Vladlen Larionov
 */
public final class RoundContext {
    private final Set<? extends TypeElement> annotations;
    private final RoundEnvironment roundEnv;
    private final Map<Class, Object> properties;

    public RoundContext(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        this.annotations = annotations;
        this.roundEnv = roundEnv;
        this.properties = new HashMap();
    }

    public Set<? extends TypeElement> getAnnotations() {
        return annotations;
    }

    public RoundEnvironment getRoundEnv() {
        return roundEnv;
    }

    public Object getProperty(Class propertyClass) {
        return properties.get(propertyClass);
    }

    public void setProperty(Object property) {
        properties.put(property.getClass(), property);
    }
}
