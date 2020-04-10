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

package colesico.framework.assist.codegen.model;

import colesico.framework.assist.StrUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class MethodElement extends ParserElement {

    /**
     * Origin service class
     */
    protected final ClassElement parentClass;

    /**
     * Origin service method
     */
    protected final ExecutableElement originExecutableElement;

    public MethodElement(ProcessingEnvironment processingEnv, ClassElement parentClass, ExecutableElement methodElement) {
        super(processingEnv);
        this.parentClass = parentClass;
        this.originExecutableElement = methodElement;
    }

    @Override
    public ExecutableElement unwrap() {
        return originExecutableElement;
    }

    public ClassElement getParentClass() {
        return parentClass;
    }

    public ExecutableType getExecutableType() {
        ClassElement parentClass = getParentClass();
        ExecutableType result = (ExecutableType) parentClass.asClassType().getMemberType(originExecutableElement);
        return result;
    }

    public TypeMirror getReturnType() {
        return getExecutableType().getReturnType();
    }

    public ClassType getReturnClassType() {
        if (getReturnType().getKind() == TypeKind.DECLARED) {
            return new ClassType(getProcessingEnv(), (DeclaredType) getReturnType());
        }
        return null;
    }

    public String getName() {
        return originExecutableElement.getSimpleName().toString();
    }

    public String getNameWithPrefix(String prefix) {
        if (StringUtils.isEmpty(prefix)) {
            return getName();
        }
        return StrUtils.addPrefix(prefix, getName());
    }

    public List<ParameterElement> getParameters() {
        List<? extends VariableElement> params = originExecutableElement.getParameters();

        ExecutableType methodType = (ExecutableType) getTypeUtils().asMemberOf(parentClass.asType(), originExecutableElement);
        List<? extends TypeMirror> paramTypes = methodType.getParameterTypes();

        List<ParameterElement> result = new ArrayList<>();
        int i = 0;
        for (VariableElement paramElement : params) {
            TypeMirror paramType = paramTypes.get(i++);
            result.add(new ParameterElement(processingEnv, this, paramElement, paramType));
        }
        return result;
    }

    public List<ParameterElement> getParametersFiltered(Predicate<ParserElement> filter) {
        throw new NotImplementedException("getParametersFiltered not implemented yet");
    }

    public boolean isConstractor() {
        return originExecutableElement.getKind() == ElementKind.CONSTRUCTOR;
    }

    public boolean isGetter() {
        return StringUtils.startsWith(getName(), "get") && getParameters().isEmpty()
                && !(getReturnType() instanceof NoType);
    }

    public boolean isSetter() {
        return StringUtils.startsWith(getName(), "set") && (getParameters().size() == 1)
                && (getReturnType() instanceof NoType);
    }

    /**
     * Returns supper method  (if this method overrides another)
     */
    public MethodElement getSuperMethod() {
        ClassElement classElm = parentClass;
        ClassType superClass;
        while (null != (superClass = classElm.getSuperClass())) {
            ClassElement superClassElm = superClass.asClassElement();
            for (MethodElement superMethodElm : superClassElm.getDeclaredMethods()) {
                ExecutableElement method = superMethodElm.unwrap();
                if (getElementUtils().overrides(originExecutableElement, method, classElm.unwrap())) {
                    return new MethodElement(processingEnv, superClassElm, method);
                }
            }
            classElm = superClassElm;
        }
        return null;
    }

    /**
     * Returns interface method  that is implemented by this method
     */
    public List<MethodElement> getInterfaceMethods() {
        List<MethodElement> result = new ArrayList<>();
        ClassElement classElm = parentClass;

        while (classElm != null) {
            List<ClassType> interfaces = classElm.getInterfaces();
            for (ClassType ifaceType : interfaces) {
                ClassElement ifaceElement = ifaceType.asClassElement();
                for (MethodElement ifaceMethodElm : ifaceElement.getDeclaredMethods()) {
                    ExecutableElement ifaceMethod = ifaceMethodElm.unwrap();
                    if (getElementUtils().overrides(originExecutableElement, ifaceMethod, classElm.unwrap())) {
                        result.add(new MethodElement(processingEnv, ifaceElement, ifaceMethod));
                        break;
                    }
                }
            }
            ClassType superClass = classElm.getSuperClass();
            classElm = superClass == null ? null : superClass.asClassElement();
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodElement that = (MethodElement) o;

        return Objects.equals(originExecutableElement, that.originExecutableElement);
    }

    @Override
    public int hashCode() {
        return originExecutableElement != null ? originExecutableElement.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MethodElement{" +
                "originElement=" + originExecutableElement +
                '}';
    }
}
