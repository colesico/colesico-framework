/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
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

package colesico.framework.service.codegen.model.teleapi;


import colesico.framework.assist.Elements;
import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.teleapi.TeleFacade;
import colesico.framework.teleapi.dataport.TRContext;
import colesico.framework.teleapi.dataport.TWContext;
import com.palantir.javapoet.CodeBlock;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Tele-facade representation
 * This class can be extended to use with concrete tele-type modulator
 */
public class TeleFacadeElement {

    /**
     * Parent service ref
     */
    protected ServiceElement parentService;

    /**
     * Tele-type id
     */
    private final Class<?> teleType;

    /**
     * Descriptors registry class
     */
    private final Class<?> descriptorsClass;

    /**
     * Descriptors registry method code
     */
    private CodeBlock descriptorsMethodBody;

    /**
     * Tele write context class
     */
    private final Class<? extends TRContext> readContextClass;

    /**
     * Tele read context class
     */
    private final Class<? extends TWContext> writeContextClass;

    /**
     * Tele methods.
     * This list can be different with the methods of service due to  {@link colesico.framework.service.LocalMethod}
     */
    private final Elements<TeleMethodElement> teleMethods = new Elements<>();

    /**
     * IoC Qualifier for producer method
     */
    private final IocQualifier iocQualifier;

    /**
     * Batch params support enabled
     *
     * @see colesico.framework.service.BatchField
     */
    private Boolean batchParams = false;

    /**
     * Tele-facade associated batches
     */
    private final TeleBatchPackElement batchPack;

    /**
     * Tele schemas for the facade
     */
    private final Map<Class<?>, TeleSchemeElement<?>> teleSchemes = new HashMap<>();
    ;

    /**
     * Common purpose properties
     */
    private final Map<Class<?>, Object> properties = new HashMap<>();

    public TeleFacadeElement(Class<?> teleType,
                             Class<?> descriptorsClass,
                             Class<? extends TRContext> readContextClass,
                             Class<? extends TWContext> writeContextClass,
                             IocQualifier iocQualifier) {
        this.teleType = teleType;
        this.descriptorsClass = descriptorsClass;
        this.readContextClass = readContextClass;
        this.writeContextClass = writeContextClass;
        this.iocQualifier = iocQualifier;

        this.batchPack = new TeleBatchPackElement(this);
    }

    /**
     * Returns tele-facade class simple name
     */
    public String facadeClassSimpleName() {
        String originClassName = parentService.originClass().getSimpleName();
        String teleTypeSuffix = StrUtils.firstCharToUpperCase(teleType.getSimpleName());

        if (StringUtils.endsWith(originClassName, teleTypeSuffix)) {
            return originClassName + TeleFacade.TELE_FACADE_SUFFIX;
        } else {
            return originClassName + teleTypeSuffix + TeleFacade.TELE_FACADE_SUFFIX;
        }
    }

    /**
     * Returns tele-facade class full name
     */
    public String facadeClassName() {
        return parentService.originClass().getPackageName() + '.' + facadeClassSimpleName();
    }

    public void addTeleMethod(TeleMethodElement teleMethod) {
        teleMethods.add(teleMethod);
        teleMethod.parentTeleFacade = this;
        teleMethod.index = teleMethods.size();
    }

    public <B> TeleSchemeElement<B> teleScheme(Class<B> schemeType) {
        return (TeleSchemeElement<B>) teleSchemes.get(schemeType);
    }

    public <B> void setTeleScheme(Class<B> schemeType, TeleSchemeElement<B> schemeBuilder) {
        teleSchemes.put(schemeType, schemeBuilder);
    }

    public <C> C property(Class<C> propertyClass) {
        return (C) properties.get(propertyClass);
    }

    public void setProperty(Class<?> propertyClass, Object property) {
        properties.put(propertyClass, property);
    }

    public CodeBlock descriptorsMethodBody() {
        if (descriptorsMethodBody == null) {
            CodegenException.of().message("Tele-ligature code is null");
        }
        return descriptorsMethodBody;
    }

    public ServiceElement parentService() {
        return parentService;
    }

    public Elements<TeleMethodElement> teleMethods() {
        return teleMethods;
    }

    public Class<?> teleType() {
        return teleType;
    }

    public void setDescriptorsMethodBody(CodeBlock descriptorsMethodBody) {
        this.descriptorsMethodBody = descriptorsMethodBody;
    }

    public Class<?> descriptorsClass() {
        return descriptorsClass;
    }

    public IocQualifier iocQualifier() {
        return iocQualifier;
    }

    public void setParentService(ServiceElement parentService) {
        this.parentService = parentService;
    }

    public Boolean batchParams() {
        return batchParams;
    }

    public void setBatchParams(Boolean batchParams) {
        this.batchParams = batchParams;
    }

    public TeleBatchPackElement batchPack() {
        return batchPack;
    }

    public Class<? extends TRContext> readContextClass() {
        return readContextClass;
    }

    public Class<? extends TWContext> writeContextClass() {
        return writeContextClass;
    }

    public static final class IocQualifier {
        private final String named;
        private final String classed;

        public String named() {
            return named;
        }

        public String classed() {
            return classed;
        }

        private IocQualifier(String named, String classed) {
            this.named = named;
            this.classed = classed;
        }

        public static IocQualifier ofEmpty() {
            return new IocQualifier(null, null);
        }

        public static IocQualifier ofNamed(String name) {
            return new IocQualifier(name, null);
        }

        public static IocQualifier ofClassed(String classed) {
            return new IocQualifier(null, classed);
        }

        public static IocQualifier ofClassed(Class<?> classed) {
            return new IocQualifier(null, classed.getName());
        }
    }
}
