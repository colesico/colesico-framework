/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.assist.codegen.model;

import colesico.framework.assist.codegen.CodegenException;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ClassElement extends ParserElement {

    protected final TypeElement originTypeElement;

    public ClassElement(ProcessingEnvironment processingEnv, Class clazz) {
        super(processingEnv);
        if (clazz == null) {
            throw CodegenException.of().message("class is  null").build();
        }
        this.originTypeElement = getElementUtils().getTypeElement(clazz.getCanonicalName());
    }

    public ClassElement(ProcessingEnvironment processingEnv, String className) {
        super(processingEnv);
        if (StringUtils.isEmpty(className)) {
            throw CodegenException.of().message("className is empty").build();
        }
        TypeElement elm = getElementUtils().getTypeElement(className);
        if (!(elm.getKind().isClass() || elm.getKind().isInterface())) {
            throw CodegenException.of().message("Unsupported element kind:" + elm.getKind() + " for class name: " + className).build();
        }
        this.originTypeElement = elm;
    }

    public ClassElement(ProcessingEnvironment processingEnv, TypeElement classElement) {
        super(processingEnv);
        if (classElement == null) {
            throw CodegenException.of().message("classElement is null").build();
        }
        if (!(classElement.getKind().isClass() || classElement.getKind().isInterface())) {
            throw CodegenException.of().message("Unsupported element kind:" + classElement.getKind()).element(classElement).build();
        }
        this.originTypeElement = classElement;
    }

    public ClassElement(ProcessingEnvironment processingEnv, DeclaredType classType) {
        super(processingEnv);
        if (classType == null) {
            throw CodegenException.of().message("classType is null").build();
        }
        this.originTypeElement = (TypeElement) classType.asElement();
    }

    @Override
    public TypeElement unwrap() {
        return originTypeElement;
    }

    public String getName() {
        return originTypeElement.toString();
    }

    public String getSimpleName() {
        return originTypeElement.getSimpleName().toString();
    }

    public DeclaredType asType() {
        return (DeclaredType) originTypeElement.asType();
    }

    public ClassType asClassType() {
        return new ClassType(getProcessingEnv(), asType());
    }

    public List<FieldElement> getFields() {
        List<FieldElement> result = new ArrayList<>();
        List<? extends Element> members = getElementUtils().getAllMembers(originTypeElement);
        List<VariableElement> fields = ElementFilter.fieldsIn(members);
        for (VariableElement field : fields) {
            result.add(new FieldElement(processingEnv, this, field));
        }
        return result;
    }

    public List<FieldElement> getFieldsFiltered(Predicate<FieldElement> filter) {
        List<FieldElement> fields = getFields();
        List<FieldElement> result = new ArrayList<>();
        for (FieldElement field : fields) {
            if (filter.test(field)) {
                result.add(field);
            }
        }
        return result;
    }

    public List<MethodElement> getConstructors() {
        List<MethodElement> result = new ArrayList<>();
        List<? extends Element> members = getElementUtils().getAllMembers(originTypeElement);
        List<ExecutableElement> constrs = ElementFilter.constructorsIn(members);
        for (ExecutableElement constr : constrs) {
            TypeElement methodClass = (TypeElement) constr.getEnclosingElement();
            String methodClassName = methodClass.asType().toString();
            if (methodClassName.equals(Object.class.getName())) {
                continue;
            }
            result.add(new MethodElement(processingEnv, this, constr));
        }
        return result;
    }

    public List<MethodElement> getConstructorsFiltered(Predicate<MethodElement> filter) {
        List<MethodElement> constrs = getConstructors();
        List<MethodElement> result = new ArrayList<>();
        for (MethodElement method : constrs) {
            if (filter.test(method)) {
                result.add(method);
            }
        }
        return result;
    }

    public List<MethodElement> getMethods() {
        List<MethodElement> result = new ArrayList<>();
        List<? extends Element> members = getElementUtils().getAllMembers(originTypeElement);
        List<ExecutableElement> methods = ElementFilter.methodsIn(members);
        for (ExecutableElement method : methods) {
            TypeElement methodClass = (TypeElement) method.getEnclosingElement();
            String methodClassName = methodClass.asType().toString();
            if (methodClassName.equals(Object.class.getName())) {
                continue;
            }
            result.add(new MethodElement(processingEnv, this, method));
        }
        return result;
    }

    public List<MethodElement> getMethodsFiltered(Predicate<MethodElement> filter) {
        List<MethodElement> methods = getMethods();
        List<MethodElement> result = new ArrayList<>();
        for (MethodElement method : methods) {
            if (filter.test(method)) {
                result.add(method);
            }
        }
        return result;
    }

    public boolean isSameType(Class clazz) {
        TypeMirror clazzTypeMirror = getElementUtils().getTypeElement(clazz.getCanonicalName()).asType();
        return getTypeUtils().isSameType(clazzTypeMirror, originTypeElement.asType());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassElement that = (ClassElement) o;

        return originTypeElement != null ? originTypeElement.equals(that.originTypeElement) : that.originTypeElement == null;
    }

    @Override
    public int hashCode() {
        return originTypeElement != null ? originTypeElement.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ClassElement{" +
                "originElement=" + originTypeElement +
                '}';
    }
}
