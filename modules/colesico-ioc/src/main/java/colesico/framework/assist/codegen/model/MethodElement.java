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
import java.lang.annotation.Annotation;
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
    protected final ExecutableElement originElement;
    protected final ExecutableType originType;

    public MethodElement(ProcessingEnvironment processingEnv, ClassElement parentClass, ExecutableElement originElement, ExecutableType originType) {
        super(processingEnv);
        this.parentClass = parentClass;
        this.originElement = originElement;
        this.originType = originType;
    }

    @Override
    public ExecutableElement unwrap() {
        return originElement;
    }

    public ClassElement getParentClass() {
        return parentClass;
    }

    public ExecutableType getOriginType() {
        return originType;
    }

    public TypeMirror getMemberType(Element element) {
        return getTypeUtils().asMemberOf((DeclaredType) getOriginType(), element);
    }

    public TypeMirror getReturnType() {
        return getOriginType().getReturnType();
    }

    public ClassType getReturnClassType() {
        if (getReturnType().getKind() == TypeKind.DECLARED) {
            return new ClassType(getProcessingEnv(), (DeclaredType) getReturnType());
        }
        return null;
    }

    public String getName() {
        return originElement.getSimpleName().toString();
    }

    public String getNameWithPrefix(String prefix) {
        if (StringUtils.isEmpty(prefix)) {
            return getName();
        }
        return StrUtils.addPrefix(prefix, getName());
    }

    public List<ParameterElement> getParameters() {
        List<? extends VariableElement> params = originElement.getParameters();

        List<? extends TypeMirror> paramTypes = getOriginType().getParameterTypes();

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
        return originElement.getKind() == ElementKind.CONSTRUCTOR;
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
        ClassType superType;
        while (null != (superType = classElm.getSuperClass())) {
            ClassElement superClass = superType.asClassElement();
            for (MethodElement superMethodElm : superClass.getDeclaredMethods()) {
                ExecutableElement method = superMethodElm.unwrap();
                if (getElementUtils().overrides(originElement, method, classElm.unwrap())) {
                    ExecutableType methodType = (ExecutableType) getTypeUtils().asMemberOf(superType.unwrap(), method);
                    return new MethodElement(processingEnv, superClass, method, methodType);
                }
            }
            classElm = superClass;
        }
        return null;
    }

    /**
     * Returns declared and inherited from super classes annotations
     */
    public <A extends Annotation> List<AnnotationAssist<A>> getAnnotationsInherited(Class<A> annClass) {
        List<AnnotationAssist<A>> result = new ArrayList<>();
        MethodElement superMethod = this;
        do {
            AnnotationAssist<A> ann = superMethod.getAnnotation(annClass);
            if (ann != null) {
                result.add(ann);
            }
        } while ((superMethod = superMethod.getSuperMethod()) != null);
        return result;
    }

    /**
     * Returns interface method  that is implemented by this method
     */
    public List<MethodElement> getInterfaceMethods() {
        List<MethodElement> result = new ArrayList<>();
        ClassElement classElement = parentClass;

        while (classElement != null) {
            List<ClassType> interfaces = classElement.getInterfaces();
            for (ClassType ifaceType : interfaces) {
                ClassElement ifaceElement = ifaceType.asClassElement();
                for (MethodElement methodElement : ifaceElement.getDeclaredMethods()) {
                    ExecutableElement method = methodElement.unwrap();
                    if (getElementUtils().overrides(originElement, method, classElement.unwrap())) {
                        ExecutableType methodType = (ExecutableType) getTypeUtils().asMemberOf(ifaceType.unwrap(), method);
                        result.add(new MethodElement(processingEnv, ifaceElement, method, methodType));
                        break;
                    }
                }
            }
            ClassType superClass = classElement.getSuperClass();
            classElement = superClass == null ? null : superClass.asClassElement();
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodElement that = (MethodElement) o;

        return Objects.equals(originElement, that.originElement);
    }

    @Override
    public int hashCode() {
        return originElement != null ? originElement.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MethodElement{" +
                "originElement=" + originElement +
                '}';
    }
}
