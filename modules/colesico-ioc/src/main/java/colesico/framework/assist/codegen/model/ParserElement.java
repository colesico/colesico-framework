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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ModuleElement;
import javax.lang.model.element.PackageElement;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

abstract public class ParserElement extends Atom {

    public ParserElement(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    abstract public Element unwrap();

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
        return packageElement.toString();
    }


    public <A extends Annotation> AnnotationAtom<A> getAnnotation(Class<A> annClass) {
        A annotation = unwrap().getAnnotation(annClass);
        if (annotation == null) {
            return null;
        }
        return new AnnotationAtom<>(processingEnv, annotation);
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
            String pkgName = ed.getPackage().toString();
            if (!getPackage().getQualifiedName().toString().equals(pkgName)) {
                continue;
            }
            List<? extends ModuleElement> targetModules = ed.getTargetModules();
            if (targetModules == null) {
                return true;
            }
            for (ModuleElement m : targetModules) {
                if (m.toString().equals(targetModule)) {
                    return true;
                }
            }
        }
        return false;
    }
}
