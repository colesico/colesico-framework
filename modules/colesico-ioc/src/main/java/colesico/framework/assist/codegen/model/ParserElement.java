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
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ModuleElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

abstract public class ParserElement extends Assist {

    public ParserElement(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    /**
     * Return origin java parser element
     */
    abstract public Element unwrap();

    abstract public TypeMirror getOriginType();

    public String getName() {
        return unwrap().getSimpleName().toString();
    }

    public ClassType asClassType() {
        if (getOriginType().getKind() == TypeKind.DECLARED) {
            return new ClassType(getProcessingEnv(), (DeclaredType) getOriginType());
        }
        return null;
    }

    public String getNameWithPrefix(String prefix) {
        if (StringUtils.isEmpty(prefix)) {
            return getName();
        }
        return StrUtils.addPrefix(prefix, getName());
    }

    public ModuleElement getModule() {
        return getElementUtils().getModuleOf(unwrap());
    }

    public PackageElement getPackage() {
        Element enclosingElement = unwrap().getEnclosingElement();
        while (!(enclosingElement instanceof PackageElement)) {
            enclosingElement = enclosingElement.getEnclosingElement();
        }
        PackageElement packageElement = (PackageElement) enclosingElement;
        return packageElement;
    }

    public String getPackageName() {
        PackageElement packageElement = getPackage();
        return packageElement.getQualifiedName().toString();
    }


    public <A extends Annotation> AnnotationAssist<A> getAnnotation(Class<A> annClass) {
        A annotation = unwrap().getAnnotation(annClass);
        if (annotation == null) {
            return null;
        }
        return new AnnotationAssist<>(processingEnv, annotation);
    }

    public List<AnnotationType> getAnnotationTypes() {
        List<? extends AnnotationMirror> ams = unwrap().getAnnotationMirrors();
        List<AnnotationType> result = new ArrayList<>();
        for (AnnotationMirror am : ams) {
            result.add(new AnnotationType(processingEnv, am));
        }
        return result;
    }

    public boolean checkPackageAccessibility(String targetModule) {
        if (getModule().isOpen()) {
            return true;
        }

        if (getModule().isUnnamed()) {
            return true;
        }

        List<? extends ModuleElement.Directive> directives = getModule().getDirectives();

        for (ModuleElement.Directive d : directives) {
            if (d.getKind() != ModuleElement.DirectiveKind.EXPORTS) {
                continue;
            }
            ModuleElement.ExportsDirective ed = (ModuleElement.ExportsDirective) d;
            String pkgName = ed.getPackage().getQualifiedName().toString();
            if (!getPackage().getQualifiedName().toString().equals(pkgName)) {
                continue;
            }
            List<? extends ModuleElement> targetModules = ed.getTargetModules();
            if (targetModules == null) {
                return true;
            }
            for (ModuleElement m : targetModules) {
                if (m.getQualifiedName().toString().equals(targetModule)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParameterElement that = (ParameterElement) o;

        return Objects.equals(unwrap(), that.unwrap());
    }

    @Override
    public int hashCode() {
        return unwrap() != null ? unwrap().hashCode() : 0;
    }

}
