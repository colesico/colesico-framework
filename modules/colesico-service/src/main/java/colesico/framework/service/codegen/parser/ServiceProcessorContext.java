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

import colesico.framework.assist.codegen.CodegenMode;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.service.codegen.model.InterceptionPhases;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.modulator.ModulatorKit;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Service processor context
 */
public final class ServiceProcessorContext {

    private final ProcessingEnvironment processingEnv;

    private final ModulatorKit modulatorKit;

    private final InterceptionPhases interceptionPhases;

    /**
     * Custom purpose props
     */
    private final Map<Class, Object> properties = new HashMap<>();

    /**
     * Flag indicates to generate code for production
     */
    private final CodegenMode codegenMode;

    public ServiceProcessorContext(ModulatorKit modulatorKit, ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        this.modulatorKit = modulatorKit;
        this.interceptionPhases = new InterceptionPhases();
        this.codegenMode = CodegenMode.fromKey(processingEnv.getOptions().get(CodegenUtils.OPTION_CODEGEN));
    }

    public CodegenMode getCodegenMode() {
        return codegenMode;
    }

    public Object getProperty(Class propertyClass) {
        return properties.get(propertyClass);
    }

    public void setProperty(Object property) {
        properties.put(property.getClass(), property);
    }

    public ProcessingEnvironment getProcessingEnv() {
        return processingEnv;
    }

    public ModulatorKit getModulatorKit() {
        return modulatorKit;
    }

    public InterceptionPhases getInterceptionPhases() {
        return interceptionPhases;
    }

    public Elements getElementUtils() {
        return processingEnv.getElementUtils();
    }

    public Messager getMessager() {
        return processingEnv.getMessager();
    }

    public Filer getFiler() {
        return processingEnv.getFiler();
    }

    public Types getTypeUtils() {
        return processingEnv.getTypeUtils();
    }

}
