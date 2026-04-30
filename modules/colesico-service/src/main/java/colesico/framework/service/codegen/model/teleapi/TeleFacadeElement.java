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
     * commands registry class
     */
    private final Class<? extends TeleFacade.Commands> commandsClass;

    /**
     * Commands registry method code
     */
    private CodeBlock commandsMethodBody;

    /**
     * Tele write context class
     */
    private final Class<? extends TRContext<?,?>> readContextClass;

    /**
     * Tele read context class
     */
    private final Class<? extends TWContext<?,?>> writeContextClass;

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

    /**
     * Common purpose properties
     */
    private final Map<Class<?>, Object> properties = new HashMap<>();

    public TeleFacadeElement(Class<?> teleType,
                             Class<? extends TeleFacade.Commands> commandsClass,
                             Class<? extends TRContext<?,?>> readContextClass,
                             Class<? extends TWContext<?,?>> writeContextClass,
                             IocQualifier iocQualifier) {
        this.teleType = teleType;
        this.commandsClass = commandsClass;
        this.readContextClass = readContextClass;
        this.writeContextClass = writeContextClass;
        this.iocQualifier = iocQualifier;

        this.batchPack = new TeleBatchPackElement(this);
    }

    /**
     * Returns tele-facade class simple name
     */
    public String facadeClassSimpleName() {
        String originClassName = parentService.originClass().simpleName();
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
        return parentService.originClass().packageName() + '.' + facadeClassSimpleName();
    }

    public void addTeleCommand(TeleCommandElement teleCommand) {
        teleCommands.add(teleCommand);
        teleCommand.parentTeleFacade = this;
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

    public Class<? extends TeleFacade.Commands> commandsClass() {
        return commandsClass;
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
