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
import colesico.framework.assist.StringUtils;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.service.ParamBean;
import colesico.framework.service.BeanField;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.teleapi.TeleFacade;
import colesico.framework.teleapi.TeleInterceptor;
import colesico.framework.teleapi.dataport.ReadOptions;
import colesico.framework.teleapi.dataport.WriteOptions;
import com.palantir.javapoet.CodeBlock;

import java.util.HashMap;
import java.util.Map;

/**
 * Tele-service representation
 * Extends this class to support mo specific tele model
 */
public class TeleServiceElement {

    /**
     * Parent service ref
     */
    protected ServiceElement parentService;

    /**
     * Tele-type id
     */
    private final Class<?> teleType;

    /**
     * commands registry class
     */
    private final Class<? extends TeleFacade.CommandsRegistry> commandsClass;

    /**
     * Commands registry method code
     */
    private CodeBlock commandsMethodBody;

    /**
     * Tele write context class
     */
    private final Class<? extends ReadOptions> readOptionsClass;

    /**
     * Tele read context class
     */
    private final Class<? extends WriteOptions> writeOptionsClass;

    /**
     * Tele commands.
     * This list can be different with the methods of service due to  {@link colesico.framework.service.LocalMethod}
     */
    private final Elements<TeleCommandElement> teleCommands = new Elements<>();

    /**
     * IoC Qualifier for producer method
     */
    private final IocQualifier iocQualifier;

    /**
     * Param composition bean support enabled
     *
     * @see BeanField
     */
    private Boolean supportParamCompositions = false;

    /**
     * Param aggregation beans support enabled
     *
     * @see ParamBean
     */
    private Boolean supportParamAggregations = false;


    /**
     * Tele-facade associated param beans
     */
    private final TeleCompositionsPackElement compositionsPack;

    /**
     * Tele schemas for the facade
     */
    private final Map<Class<?>, TeleSchemeElement<?>> teleSchemes = new HashMap<>();

    /**
     * Common purpose properties
     */
    private final Map<Class<?>, Object> properties = new HashMap<>();

    public TeleServiceElement(Class<?> teleType,
                              Class<? extends TeleFacade.CommandsRegistry> commandsClass,
                              Class<? extends ReadOptions> readOptionsClass,
                              Class<? extends WriteOptions> writeOptionsClass,
                              IocQualifier iocQualifier) {
        this.teleType = teleType;
        this.commandsClass = commandsClass;
        this.readOptionsClass = readOptionsClass;
        this.writeOptionsClass = writeOptionsClass;
        this.iocQualifier = iocQualifier;

        this.compositionsPack = new TeleCompositionsPackElement(this);
    }

    /**
     * Returns tele-facade class simple name
     */
    public String facadeClassSimpleName() {
        String originClassName = parentService.originClass().simpleName();
        String teleTypeSuffix = StringUtils.firstCharToUpperCase(teleType.getSimpleName());

        if (StringUtils.endsWith(originClassName, teleTypeSuffix)) {
            return originClassName + TeleFacade.TELE_FACADE_SUFFIX;
        } else {
            return originClassName + teleTypeSuffix + TeleFacade.TELE_FACADE_SUFFIX;
        }
    }

    /**
     * Returns tele-interceptor class simple name
     */
    public String interceptorClassSimpleName() {
        String originClassName = parentService.originClass().simpleName();
        String teleTypeSuffix = StringUtils.firstCharToUpperCase(teleType.getSimpleName());

        if (StringUtils.endsWith(originClassName, teleTypeSuffix)) {
            return originClassName + TeleInterceptor.TELE_INTERCEPTOR_SUFFIX;
        } else {
            return originClassName + teleTypeSuffix + TeleInterceptor.TELE_INTERCEPTOR_SUFFIX;
        }
    }

    /**
     * Returns tele-facade class full name
     */
    public String facadeClassName() {
        return parentService.originClass().packageName() + '.' + facadeClassSimpleName();
    }

    public String interceptorClassName() {
        return parentService.originClass().packageName() + '.' + interceptorClassSimpleName();
    }

    public void addTeleCommand(TeleCommandElement teleCommand) {
        teleCommands.add(teleCommand);
        teleCommand.parentTeleService = this;
        teleCommand.index = teleCommands.size();
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

    public CodeBlock commandsMethodBody() {
        if (commandsMethodBody == null) {
            CodegenException.of().message("Tele commands registry method body code is null");
        }
        return commandsMethodBody;
    }

    public ServiceElement parentService() {
        return parentService;
    }

    public Elements<TeleCommandElement> teleCommands() {
        return teleCommands;
    }

    public Class<?> teleType() {
        return teleType;
    }

    public void setCommandsMethodBody(CodeBlock commandsMethodBody) {
        this.commandsMethodBody = commandsMethodBody;
    }

    public Class<? extends TeleFacade.CommandsRegistry> commandsClass() {
        return commandsClass;
    }

    public IocQualifier iocQualifier() {
        return iocQualifier;
    }

    public void setParentService(ServiceElement parentService) {
        this.parentService = parentService;
    }

    public Boolean supportParamCompositions() {
        return supportParamCompositions;
    }

    public Boolean supportParamAggregations() {
        return supportParamAggregations;
    }

    public void setSupportParamCompositions(Boolean supportParamCompositions) {
        this.supportParamCompositions = supportParamCompositions;
    }

    public void setSupportParamAggregations(Boolean supportParamAggregations) {
        this.supportParamAggregations = supportParamAggregations;
    }

    public TeleCompositionsPackElement compositionsPack() {
        return compositionsPack;
    }

    public Class<? extends ReadOptions> readOptionsClass() {
        return readOptionsClass;
    }

    public Class<? extends WriteOptions> writeOptionsClass() {
        return writeOptionsClass;
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
