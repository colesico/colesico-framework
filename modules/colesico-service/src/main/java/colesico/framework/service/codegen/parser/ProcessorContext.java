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

package colesico.framework.service.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class ProcessorContext {

    private final ProcessingEnvironment processingEnv;
    private final Elements elementUtils;
    private final Types typeUtils;
    private final Messager messager;
    private final Filer filer;

    private final ModulatorKit modulatorKit;

    private final InterceptionPhases interceptionPhases;

    private final Set<ServiceElement> processedServices = new HashSet<>();

    private final Map<Class, Object> properties;

    /**
     * Flag indicates to generate code for production
     */
    private final boolean productionCodegen;

    public ProcessorContext(ModulatorKit modulatorKit, ProcessingEnvironment processingEnv) {

        this.processingEnv = processingEnv;
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
        this.properties = new HashMap<>();
        this.modulatorKit = modulatorKit;
        this.interceptionPhases = new InterceptionPhases();

        // Detect production code generation mode
        String codegenMode = processingEnv.getOptions().get(CodegenUtils.OPTION_CODEGEN);
        if (codegenMode == null) {
            codegenMode = CodegenUtils.OPTION_CODEGEN_PROD;
        }
        switch (codegenMode) {
            case CodegenUtils.OPTION_CODEGEN_PROD:
                productionCodegen = true;
                break;
            case CodegenUtils.OPTION_CODEGEN_DEV:
                productionCodegen = false;
                break;
            default:
                throw CodegenException.of().message("Unsupported code generation mode: " + codegenMode +
                        ". Valid values: " + CodegenUtils.OPTION_CODEGEN_PROD + "; " + CodegenUtils.OPTION_CODEGEN_DEV).build();
        }
    }

    public boolean isProductionCodegen() {
        return productionCodegen;
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

    public Elements getElementUtils() {
        return elementUtils;
    }

    public Types getTypeUtils() {
        return typeUtils;
    }

    public Messager getMessager() {
        return messager;
    }

    public Filer getFiler() {
        return filer;
    }

    public Set<ServiceElement> getProcessedServices() {
        return processedServices;
    }

    public ModulatorKit getModulatorKit() {
        return modulatorKit;
    }

    public InterceptionPhases getInterceptionPhases() {
        return interceptionPhases;
    }
}
