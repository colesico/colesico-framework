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

package colesico.framework.service.codegen.model.teleapi;


import colesico.framework.assist.Elements;
import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.TeleDriver;
import colesico.framework.teleapi.TeleFacade;
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
     * Tele-driver type
     */
    private final Class<? extends TeleDriver> teleDriverClass;

    /**
     * Data port type
     */
    private final Class<? extends DataPort> dataPortClass;

    /**
     * Ligature class
     */
    private final Class<?> ligatureClass;

    /**
     * Tele methods.
     * This list can be different with the methods of service due to  {@link colesico.framework.service.LocalMethod}
     */
    private final Elements<TeleMethodElement> teleMethods;

    /**
     * Ligature method code
     */
    private CodeBlock ligatureMethodBody;

    /**
     * IoC Qualifier for  producer method
     */
    private final IocQualifier iocQualifier;

    /**
     * Compound params support enabled
     *
     * @see colesico.framework.service.Compound
     */
    private Boolean compoundParams = false;

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
    private final Map<Class, TeleSchemeElement> teleSchemes;

    /**
     * Common purpose properties
     */
    private final Map<Class, Object> properties;

    public TeleFacadeElement(Class<?> teleType,
                             Class<? extends TeleDriver> teleDriverClass,
                             Class<? extends DataPort> dataPortClass,
                             Class<?> ligatureClass,
                             IocQualifier iocQualifier) {
        this.teleType = teleType;
        this.teleDriverClass = teleDriverClass;
        this.ligatureClass = ligatureClass;
        this.dataPortClass = dataPortClass;
        this.teleMethods = new Elements<>();
        this.iocQualifier = iocQualifier;

        this.teleSchemes = new HashMap<>();
        this.properties = new HashMap<>();
        this.batchPack = new TeleBatchPackElement(this);
    }

    public ServiceElement getParentService() {
        return parentService;
    }

    /**
     * Returns tele-facade class simple name
     */
    public String getFacadeClassSimpleName() {
        String originClassName = parentService.getOriginClass().getSimpleName();
        String teleTypeSuffix = StrUtils.firstCharToUpperCase(teleType.getSimpleName());

        if (StringUtils.endsWith(originClassName, teleTypeSuffix)) {
            return originClassName + TeleFacade.TELE_FACADE_SUFFIX;
        } else {
            return originClassName + teleTypeSuffix + TeleFacade.TELE_FACADE_SUFFIX;
        }
    }

    /**
     * Returns tele-facade class full name
     *
     * @return
     */
    public String getFacadeClassName() {
        return parentService.getOriginClass().getPackageName() + '.' + getFacadeClassSimpleName();
    }

    public void addTeleMethod(TeleMethodElement teleMethod) {
        teleMethods.add(teleMethod);
        teleMethod.parentTeleFacade = this;
        teleMethod.index = teleMethods.size();
    }

    public Elements<TeleMethodElement> getTeleMethods() {
        return teleMethods;
    }

    public Class<?> getTeleType() {
        return teleType;
    }

    public <B> TeleSchemeElement<B> getTeleScheme(Class<B> schemeType) {
        return teleSchemes.get(schemeType);
    }

    public <B> void setTeleScheme(Class<B> schemeType, TeleSchemeElement<B> schemeBuilder) {
        teleSchemes.put(schemeType, schemeBuilder);
    }

    public <C> C getProperty(Class<C> propertyClass) {
        return (C) properties.get(propertyClass);
    }

    public void setProperty(Class<?> propertyClass, Object property) {
        properties.put(propertyClass, property);
    }

    public Class<? extends TeleDriver> getTeleDriverClass() {
        return teleDriverClass;
    }

    public Class<? extends DataPort> getDataPortClass() {
        return dataPortClass;
    }

    public CodeBlock getLigatureMethodBody() {
        if (ligatureMethodBody == null) {
            CodegenException.of().message("Tele-ligature code is null");
        }
        return ligatureMethodBody;
    }

    public void setLigatureMethodBody(CodeBlock ligatureMethodBody) {
        this.ligatureMethodBody = ligatureMethodBody;
    }

    public Class<?> getLigatureClass() {
        return ligatureClass;
    }

    public IocQualifier getIocQualifier() {
        return iocQualifier;
    }

    public Boolean getCompoundParams() {
        return compoundParams;
    }

    public void setCompoundParams(Boolean compoundParams) {
        this.compoundParams = compoundParams;
    }

    public Boolean getBatchParams() {
        return batchParams;
    }

    public void setBatchParams(Boolean batchParams) {
        this.batchParams = batchParams;
    }

    public TeleBatchPackElement getBatchPack() {
        return batchPack;
    }

    public void setParentService(ServiceElement parentService) {
        this.parentService = parentService;
    }

    public static final class IocQualifier {
        private final String named;
        private final String classed;

        public String getNamed() {
            return named;
        }

        public String getClassed() {
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

        public static IocQualifier ofClassed(Class classed) {
            return new IocQualifier(null, classed.getName());
        }
    }


}
