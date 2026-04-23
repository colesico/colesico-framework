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

    public ClassElement parentClass() {
        return parentClass;
    }

    public ExecutableType originType() {
        return originType;
    }

    public TypeMirror getMemberType(Element element) {
        return typeUtils().asMemberOf((DeclaredType) originType(), element);
    }

    public TypeMirror teturnType() {
        return originType().getReturnType();
    }

    public boolean isVoidReturnType(){
        return originType().getReturnType() instanceof NoType;
    }

    public ClassType returnClassType() {
        if (teturnType().getKind() == TypeKind.DECLARED) {
            return new ClassType(processingEnv(), (DeclaredType) teturnType());
        }
        return null;
    }

    public String name() {
        return originElement.getSimpleName().toString();
    }

    public String nameWithPrefix(String prefix) {
        if (StringUtils.isEmpty(prefix)) {
            return name();
        }
        return StrUtils.addPrefix(prefix, name());
    }

    public List<ParameterElement> parameters() {
        List<? extends VariableElement> params = originElement.getParameters();

        List<? extends TypeMirror> paramTypes = originType().getParameterTypes();

        List<ParameterElement> result = new ArrayList<>();
        int i = 0;
        for (VariableElement paramElement : params) {
            TypeMirror paramType = paramTypes.get(i++);
            result.add(new ParameterElement(processingEnv, this, paramElement, paramType));
        }
        return result;
    }

    public List<ParameterElement> parametersFiltered(Predicate<ParserElement> filter) {
        throw new NotImplementedException("getParametersFiltered not implemented yet");
    }

    public boolean isConstractor() {
        return originElement.getKind() == ElementKind.CONSTRUCTOR;
    }

    public boolean isGetter() {
        return StringUtils.startsWith(name(), "get") && parameters().isEmpty()
                && !(teturnType() instanceof NoType);
    }

    public boolean isSetter() {
        return StringUtils.startsWith(name(), "set") && (parameters().size() == 1)
                && (teturnType() instanceof NoType);
    }

    /**
     * Returns supper method  (if this method overrides another)
     */
    public MethodElement superMethod() {
        ClassElement classElm = parentClass;
        ClassType superType;
        while (null != (superType = classElm.superClass())) {
            ClassElement superClass = superType.asClassElement();
            for (MethodElement superMethodElm : superClass.declaredMethods()) {
                ExecutableElement method = superMethodElm.unwrap();
                if (elementUtils().overrides(originElement, method, classElm.unwrap())) {
                    ExecutableType methodType = (ExecutableType) typeUtils().asMemberOf(superType.unwrap(), method);
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
    public <A extends Annotation> List<AnnotationAssist<A>> annotationsInherited(Class<A> annClass) {
        List<AnnotationAssist<A>> result = new ArrayList<>();
        MethodElement superMethod = this;
        do {
            AnnotationAssist<A> ann = superMethod.getAnnotation(annClass);
            if (ann != null) {
                result.add(ann);
            }
        } while ((superMethod = superMethod.superMethod()) != null);
        return result;
    }

    /**
     * Returns interface method  that is implemented by this method
     */
    public List<MethodElement> interfaceMethods() {
        List<MethodElement> result = new ArrayList<>();
        ClassElement classElement = parentClass;

        while (classElement != null) {
            List<ClassType> interfaces = classElement.interfaces();
            for (ClassType ifaceType : interfaces) {
                ClassElement ifaceElement = ifaceType.asClassElement();
                for (MethodElement methodElement : ifaceElement.declaredMethods()) {
                    ExecutableElement method = methodElement.unwrap();
                    if (elementUtils().overrides(originElement, method, classElement.unwrap())) {
                        ExecutableType methodType = (ExecutableType) typeUtils().asMemberOf(ifaceType.unwrap(), method);
                        result.add(new MethodElement(processingEnv, ifaceElement, method, methodType));
                        break;
                    }
                }
            }
            ClassType superClass = classElement.superClass();
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
