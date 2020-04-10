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

package colesico.framework.service.codegen.model;

import colesico.framework.assist.Elements;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.service.ServiceProxy;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
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
     * Service proxy interfaces
     */
    private final Elements<TypeName> interfaces;

    /**
     * Service proxy extra fields
     */
    private final Elements<ProxyFieldElement> fields;

    private final Elements<ProxyMethodElement> proxyMethods;

    private final Elements<CustomMethodElement> customMethods;

    private final Elements<CodeBlock> constructorExtraCode;

    private final Elements<TeleFacadeElement> teleFacades;

    /**
     * Common purpose properties
     */
    private final Map<Class, Object> properties;

    private final ClassType customScopeType;

    public ServiceElement(ClassElement originClass, ClassType customScopeType) {
        this.originClass = originClass;
        this.customScopeType = customScopeType;
        this.interfaces = new Elements<>();
        this.proxyMethods = new Elements<>();
        this.fields = new Elements<>();
        this.customMethods = new Elements<>();
        this.constructorExtraCode = new Elements<>();
        this.teleFacades = new Elements<>();
        this.properties = new HashMap();
    }

    public void addField(final ProxyFieldElement field) {
        ProxyFieldElement mfe = fields.find(f -> f.getName().equals(field.getName()));
        if (mfe != null) {
            if (mfe.getTypeName().equals(field.getTypeName())) {
                return;
            }
            throw CodegenException.of().message("Duplicate field name:" + field.getName()).element(getOriginClass()).build();
        }
        fields.add(field);
        field.parentService = this;
    }

    public void addInterface(TypeName interfaceTypeName) {
        TypeName intf = interfaces.find(i -> i.toString().equals(interfaceTypeName.toString()));
        if (intf != null) {
            return;
        }
        interfaces.add(interfaceTypeName);
    }

    public void addProxyMethod(final ProxyMethodElement proxyMethod) {
        proxyMethods.add(proxyMethod);
        proxyMethod.parentService = this;
    }

    public void addCustomMethod(final CustomMethodElement customMethod) {
        CustomMethodElement cme = customMethods.find(m -> m.getName().equals(customMethod.getName()));
        if (cme != null) {
            throw CodegenException.of().message("Duplicate method name:" + customMethod.getName()).element(getOriginClass()).build();
        }
        customMethods.add(customMethod);
        customMethod.parentService = this;
    }

    public void addTeleFacade(TeleFacadeElement teleFacade) {
        TeleFacadeElement ta = teleFacades.find(t -> t.getTeleType().equals(teleFacade.getTeleType()));
        if (ta != null) {
            throw CodegenException.of().message("Duplicate tele-facade: " + teleFacade.getTeleType()).element(getOriginClass()).build();
        }
        teleFacade.parentService = this;
        teleFacades.add(teleFacade);
    }

    public void addConstuctorExtraCode(CodeBlock cb) {
        constructorExtraCode.add(cb);
    }

    public Elements<TypeName> getInterfaces() {
        return interfaces;
    }

    public Elements<ProxyFieldElement> getFields() {
        return fields;
    }

    public Elements<CustomMethodElement> getCustomMethods() {
        return customMethods;
    }

    public Elements<ProxyMethodElement> getProxyMethods() {
        return proxyMethods;
    }

    public <T> T getProperty(Class<T> propertyClass) {
        return (T) properties.get(propertyClass);
    }

    public void setProperty(Object property) {
        properties.put(property.getClass(), property);
    }

    public Elements<CodeBlock> getConstructorExtraCode() {
        return constructorExtraCode;
    }

    public Elements<TeleFacadeElement> getTeleFacades() {
        return teleFacades;
    }


    public ClassType getCustomScopeType() {
        return customScopeType;
    }

    public ClassElement getOriginClass() {
        return originClass;
    }


    public String getProxyClassSimpleName() {
        String originClassName = originClass.getSimpleName();

        if (StringUtils.endsWith(originClassName, ServiceProxy.SERVICE_CLASS_SUFFIX)) {
            return originClassName + ServiceProxy.PROXY_CLASS_SUFFIX;
        } else {
            return originClassName + ServiceProxy.SERVICE_CLASS_SUFFIX + ServiceProxy.PROXY_CLASS_SUFFIX;
        }
    }

    public String getProxyClassName() {
        return originClass.getPackageName()+'.'+getProxyClassSimpleName();
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
