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

package colesico.framework.service.codegen.model;

import colesico.framework.assist.Elements;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.service.ServiceProxy;
import colesico.framework.service.codegen.model.teleapi.TeleFacadeElement;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.TypeName;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a service proxy
 *
 * @author Vladlen Larionov
 */
public final class ServiceElement {

    /**
     * Service origin class element
     */
    private final ClassElement originClass;

    /**
     * Service proxy class auxiliary interfaces
     */
    private final Elements<TypeName> customInterfaces;

    /**
     * Service proxy class auxiliary fields
     */
    private final Elements<ServiceFieldElement> customFields;

    /**
     * Service class methods
     */
    private final Elements<ServiceMethodElement> serviceMethods;

    /**
     * Service proxy class auxiliary methods
     */
    private final Elements<CustomMethodElement> customMethods;

    /**
     * Constructor auxiliary code
     */
    private final Elements<CodeBlock> constructorCustomCode;

    /**
     * Service tele-facade if specified
     */
    private TeleFacadeElement teleFacade;

    /**
     * Common purpose properties
     */
    private final Map<Class<?>, Object> properties;

    /**
     * Service custom scope
     */
    private final ClassType customScopeType;

    public ServiceElement(ClassElement originClass, ClassType customScopeType) {
        this.originClass = originClass;
        this.customScopeType = customScopeType;
        this.customInterfaces = new Elements<>();
        this.serviceMethods = new Elements<>();
        this.customFields = new Elements<>();
        this.customMethods = new Elements<>();
        this.constructorCustomCode = new Elements<>();
        this.properties = new HashMap<>();
    }

    public void addCustomField(final ServiceFieldElement field) {
        ServiceFieldElement mfe = customFields.find(f -> f.name().equals(field.name()));
        if (mfe != null) {
            if (mfe.typeName().equals(field.typeName())) {
                return;
            }
            throw CodegenException.of().message("Duplicate field name:" + field.name()).element(originClass()).build();
        }
        customFields.add(field);
        field.parentService = this;
    }

    public void addCustomInterface(TypeName interfaceTypeName) {
        TypeName intf = customInterfaces.find(i -> i.toString().equals(interfaceTypeName.toString()));
        if (intf != null) {
            return;
        }
        customInterfaces.add(interfaceTypeName);
    }

    public void addServiceMethod(final ServiceMethodElement proxyMethod) {
        serviceMethods.add(proxyMethod);
        proxyMethod.parentService = this;
    }

    public void addCustomMethod(final CustomMethodElement customMethod) {
        CustomMethodElement cme = customMethods.find(m -> m.name().equals(customMethod.name()));
        if (cme != null) {
            throw CodegenException.of().message("Duplicate method name:" + customMethod.name()).element(originClass()).build();
        }
        customMethods.add(customMethod);
        customMethod.parentService = this;
    }

    public void setTeleFacade(TeleFacadeElement teleFacade) {
        if (this.teleFacade != null) {
            throw CodegenException.of()
                    .message("Tele-facade has already been set: " + teleFacade.teleType())
                    .element(originClass())
                    .build();
        }
        teleFacade.setParentService(this);
        this.teleFacade = teleFacade;
    }

    public void addConstructorCustomCode(CodeBlock cb) {
        constructorCustomCode.add(cb);
    }

    public Elements<TypeName> customInterfaces() {
        return customInterfaces;
    }

    public Elements<ServiceFieldElement> customFields() {
        return customFields;
    }

    public Elements<CustomMethodElement> customMethods() {
        return customMethods;
    }

    public Elements<ServiceMethodElement> serviceMethods() {
        return serviceMethods;
    }

    public <T> T property(Class<T> propertyClass) {
        return (T) properties.get(propertyClass);
    }

    public void setProperty(Object property) {
        properties.put(property.getClass(), property);
    }

    public Elements<CodeBlock> constructorCustomCode() {
        return constructorCustomCode;
    }

    public TeleFacadeElement teleFacade() {
        return teleFacade;
    }

    public ClassType customScopeType() {
        return customScopeType;
    }

    public ClassElement originClass() {
        return originClass;
    }

    public String proxyClassSimpleName() {
        String originClassName = originClass.getSimpleName();

        if (StringUtils.endsWith(originClassName, ServiceProxy.SERVICE_CLASS_SUFFIX)) {
            return originClassName + ServiceProxy.PROXY_CLASS_SUFFIX;
        } else {
            return originClassName + ServiceProxy.SERVICE_CLASS_SUFFIX + ServiceProxy.PROXY_CLASS_SUFFIX;
        }
    }

    public String proxyClassName() {
        return originClass.getPackageName() + '.' + proxyClassSimpleName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceElement that = (ServiceElement) o;
        return Objects.equals(originClass, that.originClass);
    }

    @Override
    public int hashCode() {

        return Objects.hash(originClass);
    }
}
