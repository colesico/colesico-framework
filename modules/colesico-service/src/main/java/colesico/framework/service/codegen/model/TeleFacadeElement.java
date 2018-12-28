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

package colesico.framework.service.codegen.model;


import colesico.framework.assist.Elements;
import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.TeleDriver;
import com.squareup.javapoet.CodeBlock;

import javax.lang.model.element.PackageElement;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vladlen Larionov
 */
public final class TeleFacadeElement {

    protected ServiceElement parentService;

    private final String teleType;
    private final Class<? extends TeleDriver> teleDriverClass;
    private final Class<? extends DataPort> dataPortClass;
    private final Class<?> ligatureClass;
    private final Elements<TeleMethodElement> teleMethods;

    private CodeBlock ligatureMethodBody;

    private final IocQualifiers iocQualifiers;

    private final Map<Class, Object> properties;

    public TeleFacadeElement(String teleType,
                             Class<? extends TeleDriver> teleDriverClass,
                             Class<? extends DataPort> dataPortClass,
                             Class<?> ligatureClass,
                             IocQualifiers iocQualifiers) {
        this.teleType = checkTeleType(teleType);
        this.teleDriverClass = teleDriverClass;
        this.ligatureClass = ligatureClass;
        this.dataPortClass = dataPortClass;
        this.teleMethods = new Elements<>();
        this.properties = new HashMap();
        this.iocQualifiers = iocQualifiers;
    }

    private String checkTeleType(String teleType) {
        if (!teleType.matches("^[a-zA-Z]+$")) {
            throw CodegenException.of().message("Tele-scope must contain only letters: " + teleType).build();
        }
        return teleType;
    }


    public ServiceElement getParentService() {
        return parentService;
    }

    public String getClassSimpleName() {
        return parentService.getOriginClass().getSimpleName().toString() + StrUtils.firstCharToUpperCase(teleType);
    }

    public String getClassName() {
        PackageElement packageElement = (PackageElement) parentService.getOriginClass().getEnclosingElement();
        return packageElement.toString() + "." + getClassSimpleName();
    }

    public void addTeleMethod(TeleMethodElement teleMethod) {
        if (teleMethods.find((tm) -> tm.getName().equals(teleMethod.getName())) != null) {
            throw CodegenException.of().message("Duplicate tele-method name: " + teleMethod.getName()).element(teleMethod.getProxyMethod().getOriginMethod()).build();
        }
        teleMethods.add(teleMethod);
        teleMethod.parentTeleFacade = this;
    }

    public Elements<TeleMethodElement> getTeleMethods() {
        return teleMethods;
    }

    public String getTeleType() {
        return teleType;
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

    public IocQualifiers getIocQualifiers() {
        return iocQualifiers;
    }

    public static final class IocQualifiers {
        private final String named;
        private final String classed;

        public String getNamed() {
            return named;
        }

        public String getClassed() {
            return classed;
        }

        private IocQualifiers(String named, String classed) {
            this.named = named;
            this.classed = classed;
        }

        public static IocQualifiers ofEmpty() {
            return new IocQualifiers(null, null);
        }

        public static IocQualifiers ofNamed(String name) {
            return new IocQualifiers(name, null);
        }

        public static IocQualifiers ofClassed(String classed) {
            return new IocQualifiers(null, classed);
        }

        public static IocQualifiers ofClassed(Class classed) {
            return new IocQualifiers(null, classed.getName());
        }
    }


}
