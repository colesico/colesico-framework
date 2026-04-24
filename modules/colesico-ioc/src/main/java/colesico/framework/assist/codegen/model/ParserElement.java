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

    abstract public TypeMirror originType();

    public String name() {
        return unwrap().getSimpleName().toString();
    }

    public ClassType asClassType() {
        if (originType().getKind() == TypeKind.DECLARED) {
            return new ClassType(processingEnv(), (DeclaredType) originType());
        }
        return null;
    }

    public String nameWithPrefix(String prefix) {
        if (StringUtils.isEmpty(prefix)) {
            return name();
        }
        return StrUtils.addPrefix(prefix, name());
    }

    public ModuleElement module() {
        return elementUtils().getModuleOf(unwrap());
    }

    public PackageElement packageElm() {
        Element enclosingElement = unwrap().getEnclosingElement();
        while (!(enclosingElement instanceof PackageElement)) {
            enclosingElement = enclosingElement.getEnclosingElement();
        }
        PackageElement packageElement = (PackageElement) enclosingElement;
        return packageElement;
    }

    public String packageName() {
        PackageElement packageElement = packageElm();
        return packageElement.getQualifiedName().toString();
    }


    public <A extends Annotation> AnnotationAssist<A> getAnnotation(Class<A> annClass) {
        A annotation = unwrap().getAnnotation(annClass);
        if (annotation == null) {
            return null;
        }
        return new AnnotationAssist<>(processingEnv, annotation);
    }

    public List<AnnotationType> annotationTypes() {
        List<? extends AnnotationMirror> ams = unwrap().getAnnotationMirrors();
        List<AnnotationType> result = new ArrayList<>();
        for (AnnotationMirror am : ams) {
            result.add(new AnnotationType(processingEnv, am));
        }
        return result;
    }

    public boolean checkPackageAccessibility(String targetModule) {
        if (module().isOpen()) {
            return true;
        }

        if (module().isUnnamed()) {
            return true;
        }

        List<? extends ModuleElement.Directive> directives = module().getDirectives();

        for (ModuleElement.Directive d : directives) {
            if (d.getKind() != ModuleElement.DirectiveKind.EXPORTS) {
                continue;
            }
            ModuleElement.ExportsDirective ed = (ModuleElement.ExportsDirective) d;
            String pkgName = ed.getPackage().getQualifiedName().toString();
            if (!packageElm().getQualifiedName().toString().equals(pkgName)) {
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
