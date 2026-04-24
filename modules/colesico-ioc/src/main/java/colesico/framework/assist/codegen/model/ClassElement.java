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

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ClassElement extends ParserElement {

    protected final TypeElement originElement;
    protected final DeclaredType originType;

    public ClassElement(ProcessingEnvironment processingEnv, TypeElement originElement, DeclaredType originType) {
        super(processingEnv);
        this.originElement = originElement;
        this.originType = originType;
    }

    public static ClassElement of(ProcessingEnvironment processingEnv, Class clazz) {
        if (clazz == null) {
            throw CodegenException.of().message("class is  null").build();
        }
        TypeElement element = processingEnv.getElementUtils().getTypeElement(clazz.getCanonicalName());
        DeclaredType type = (DeclaredType) element.asType();
        return new ClassElement(processingEnv, element, type);
    }

    public static ClassElement of(ProcessingEnvironment processingEnv, String className) {
        if (StringUtils.isEmpty(className)) {
            throw CodegenException.of().message("className is empty").build();
        }
        TypeElement element = processingEnv.getElementUtils().getTypeElement(className);
        if (!(element.getKind().isClass() || element.getKind().isInterface())) {
            throw CodegenException.of().message("Unsupported element kind:" + element.getKind() + " for class name: " + className).build();
        }
        DeclaredType type = (DeclaredType) element.asType();
        return new ClassElement(processingEnv, element, type);
    }

    public static ClassElement of(ProcessingEnvironment processingEnv, TypeElement element) {
        if (element == null) {
            throw CodegenException.of().message("classElement is null").build();
        }
        if (!(element.getKind().isClass() || element.getKind().isInterface())) {
            throw CodegenException.of().message("Unsupported element kind:" + element.getKind()).element(element).build();
        }
        DeclaredType type = (DeclaredType) element.asType();
        return new ClassElement(processingEnv, element, type);
    }

    public static ClassElement of(ProcessingEnvironment processingEnv, DeclaredType type) {
        if (type == null) {
            throw CodegenException.of().message("classType is null").build();
        }
        TypeElement element = (TypeElement) type.asElement();
        return new ClassElement(processingEnv, element, type);
    }

    @Override
    public TypeElement unwrap() {
        return originElement;
    }

    public String name() {
        return originElement.getQualifiedName().toString();
    }

    public String simpleName() {
        return originElement.getSimpleName().toString();
    }

    @Override
    public DeclaredType originType() {
        return originType;
    }

    public TypeMirror memberType(Element element) {
        return typeUtils().asMemberOf(originType, element);
    }

    public ClassType asClassType() {
        return new ClassType(processingEnv(), originType());
    }

    /**
     * Return all class fields  (whether inherited or declared directly)
     */
    public List<FieldElement> fields() {
        List<FieldElement> result = new ArrayList<>();
        List<? extends Element> members = elementUtils().getAllMembers(originElement);
        List<VariableElement> fields = ElementFilter.fieldsIn(members);
        for (VariableElement field : fields) {
            result.add(new FieldElement(processingEnv, this, field));
        }
        return result;
    }

    public List<FieldElement> fieldsFiltered(Predicate<FieldElement> filter) {
        List<FieldElement> fields = fields();
        List<FieldElement> result = new ArrayList<>();
        for (FieldElement field : fields) {
            if (filter.test(field)) {
                result.add(field);
            }
        }
        return result;
    }

    public List<MethodElement> constructors() {
        List<MethodElement> result = new ArrayList<>();
        List<? extends Element> members = elementUtils().getAllMembers(originElement);
        List<ExecutableElement> methods = ElementFilter.constructorsIn(members);
        for (ExecutableElement method : methods) {
            TypeElement methodClass = (TypeElement) method.getEnclosingElement();
            String methodClassName = methodClass.asType().toString();
            if (methodClassName.equals(Object.class.getName())) {
                continue;
            }
            result.add(new MethodElement(processingEnv, this, method, (ExecutableType) method.asType()));
        }
        return result;
    }

    public List<MethodElement> constructorsFiltered(Predicate<MethodElement> filter) {
        List<MethodElement> constrs = constructors();
        List<MethodElement> result = new ArrayList<>();
        for (MethodElement method : constrs) {
            if (filter.test(method)) {
                result.add(method);
            }
        }
        return result;
    }

    /**
     * Return all class methods  (whether inherited or declared directly)
     */
    public List<MethodElement> methods() {
        List<MethodElement> result = new ArrayList<>();
        List<? extends Element> members = elementUtils().getAllMembers(originElement);
        List<ExecutableElement> methods = ElementFilter.methodsIn(members);
        for (ExecutableElement method : methods) {
            TypeElement methodClass = (TypeElement) method.getEnclosingElement();
            if (CodegenUtils.isAssignable(Object.class, methodClass.asType(), processingEnv)) {
                continue;
            }
            ExecutableType methodType = (ExecutableType) typeUtils().asMemberOf(originType, method);
            result.add(new MethodElement(processingEnv, this, method, methodType));
        }
        return result;
    }

    /**
     * Return class declared directly methods (not inherited)
     */
    public List<MethodElement> declaredMethods() {
        List<MethodElement> result = new ArrayList<>();
        List<? extends Element> members = originElement.getEnclosedElements();
        List<ExecutableElement> methods = ElementFilter.methodsIn(members);
        for (ExecutableElement method : methods) {
            TypeElement methodClass = (TypeElement) method.getEnclosingElement();
            if (CodegenUtils.isAssignable(Object.class, methodClass.asType(), processingEnv)) {
                continue;
            }
            ExecutableType methodType = (ExecutableType) typeUtils().asMemberOf(originType, method);
            result.add(new MethodElement(processingEnv, this, method, methodType));
        }
        return result;
    }

    public List<MethodElement> methodsFiltered(Predicate<MethodElement> filter) {
        List<MethodElement> methods = methods();
        List<MethodElement> result = new ArrayList<>();
        for (MethodElement method : methods) {
            if (filter.test(method)) {
                result.add(method);
            }
        }
        return result;
    }

    public ClassType superClass() {
        if (originElement.getSuperclass().getKind() == TypeKind.NONE) {
            return null;
        }
        return new ClassType(processingEnv, (DeclaredType) originElement.getSuperclass());
    }

    /**
     * Returns all direct and inherited interfaces
     */
    public List<ClassType> interfaces() {
        List<ClassType> result = new ArrayList<>();
        ClassElement classElm = this;
        while (null != classElm) {
            List<? extends TypeMirror> declaredIfaces = classElm.unwrap().getInterfaces();
            List<ClassType> ifacesClassTypes = declaredIfaces.stream()
                    .map(tiface -> new ClassType(processingEnv, (DeclaredType) tiface))
                    .collect(Collectors.toList());
            result.addAll(ifacesClassTypes);

            ClassType superClass = classElm.superClass();
            classElm = superClass == null ? null : superClass.asClassElement();
        }
        return result;
    }

    public boolean isSameType(Class clazz) {
        TypeMirror clazzTypeMirror = elementUtils().getTypeElement(clazz.getCanonicalName()).asType();
        return typeUtils().isSameType(clazzTypeMirror, originType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassElement that = (ClassElement) o;

        return Objects.equals(originElement, that.originElement);
    }

    @Override
    public int hashCode() {
        return originElement != null ? originElement.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ClassElement{" +
                "originElement=" + originElement +
                '}';
    }
}
