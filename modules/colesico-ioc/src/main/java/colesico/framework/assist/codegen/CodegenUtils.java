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
package colesico.framework.assist.codegen;


import colesico.framework.assist.StrUtils;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

/**
 * Code generation helper
 *
 * @author Vladlen Larionov
 */
public class CodegenUtils {

    protected static final Logger log = LoggerFactory.getLogger(CodegenUtils.class);

    public static final String OPTION_CODEGEN="colesico.framework.codegen";
    public static final String OPTION_CODEGEN_DEV="dev";
    public static final String OPTION_CODEGEN_PROD="prod";


    public static String getModuleName(TypeElement typeElement, Elements elementUtils) {
        ModuleElement module = elementUtils.getModuleOf(typeElement);
        return module.getSimpleName().toString();
    }

    public static String getPackageName(TypeElement typeElement) {
        Element enclosingElement = typeElement.getEnclosingElement();
        while (!(enclosingElement instanceof PackageElement)) {
            enclosingElement = enclosingElement.getEnclosingElement();
        }

        PackageElement packageElement = (PackageElement) enclosingElement;

        return packageElement.toString();
    }

    public static String getClassName(TypeElement typeElement) {
        Element enclosingElement = typeElement.getEnclosingElement();
        while (!(enclosingElement instanceof PackageElement)) {
            enclosingElement = enclosingElement.getEnclosingElement();
        }

        PackageElement packageElement = (PackageElement) enclosingElement;
        String className = packageElement.toString() + "." + typeElement.getSimpleName();
        return className;
    }

    public static String getMethodName(ExecutableElement methodElement) {
        return methodElement.getSimpleName().toString();
    }

    public static PackageElement getPackage(TypeElement typeElement) {
        PackageElement packageElement = (PackageElement) typeElement.getEnclosingElement();
        return packageElement;
    }


    public static boolean methodIsGetter(ExecutableElement methodElement) {
        return StringUtils.startsWith(methodElement.getSimpleName().toString(), "get") && methodElement.getParameters().isEmpty()
                && !(methodElement.getReturnType() instanceof NoType);
    }

    public static boolean methodIsSetter(ExecutableElement methodElement) {
        return StringUtils.startsWith(methodElement.getSimpleName().toString(), "set") && (methodElement.getParameters().size() == 1)
                && (methodElement.getReturnType() instanceof NoType);
    }

    public static AnnotationValue getAnnotationValue(Class<?> annotation, String fieldName, List<? extends AnnotationMirror> elementAnnotations) {
        final String annotationClassName = annotation.getName();
        for (AnnotationMirror am : elementAnnotations) {
            if (annotationClassName.equals(am.getAnnotationType().toString())) {
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : am.getElementValues().entrySet()) {
                    if (fieldName.equals(entry.getKey().getSimpleName().toString())) {
                        return entry.getValue();
                    }
                }
            }
        }
        return null;
    }

    public static AnnotationValue getAnnotationValue(AnnotationMirror annotation, String fieldName) {
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotation.getElementValues().entrySet()) {
            if (fieldName.equals(entry.getKey().getSimpleName().toString())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static Map<? extends ExecutableElement, ? extends AnnotationValue> getAnnotationValuesWithDefaults(AnnotationMirror ad) {
        Map<ExecutableElement, AnnotationValue> valMap = new HashMap<>();
        if (ad.getElementValues() != null) {
            valMap.putAll(ad.getElementValues());
        }
        for (ExecutableElement meth : ElementFilter.methodsIn(ad.getAnnotationType().asElement().getEnclosedElements())) {
            AnnotationValue defaultValue = meth.getDefaultValue();
            if (defaultValue != null && !valMap.containsKey(meth)) {
                valMap.put(meth, defaultValue);
            }
        }
        return valMap;
    }

    public static <A extends Annotation> TypeMirror getAnnotationValueTypeMirror(A annotation, Consumer<A> fieldAccessor) {
        TypeMirror typeMirror = null;
        try {
            fieldAccessor.accept(annotation);
        } catch (MirroredTypeException mte) {
            typeMirror = mte.getTypeMirror();
        }
        return typeMirror;
    }

    public static <A extends Annotation> TypeMirror[] getAnnotationValueTypeMirrors(A annotation, Consumer<A> fieldAccessor) {
        try {
            fieldAccessor.accept(annotation);
        } catch (javax.lang.model.type.MirroredTypesException mte) {
            List<? extends TypeMirror> typeMirrors = mte.getTypeMirrors();
            TypeMirror[] result = new TypeMirror[typeMirrors.size()];
            for (int i = 0; i < typeMirrors.size(); i++) {
                result[i] = typeMirrors.get(i);
            }
            return result;
        }
        return null;
    }


    public static List<VariableElement> getFields(ProcessingEnvironment processingEnv, TypeElement classElement, Modifier[] accessModifiers, Class<? extends Annotation> annotationType) {
        List<VariableElement> result = new ArrayList<>();

        Elements utils = processingEnv.getElementUtils();
        List<? extends Element> members = utils.getAllMembers(classElement);
        List<VariableElement> fields = ElementFilter.fieldsIn(members);

        for (VariableElement field : fields) {
            boolean acceptable = false;
            for (Modifier mod : accessModifiers) {
                if (field.getModifiers().contains(mod)) {
                    acceptable = true;
                    break;
                }
            }

            if (!acceptable) {
                continue;
            }

            if ((annotationType != null) && (field.getAnnotation(annotationType) == null)) {
                continue;
            }

            result.add(field);

        }
        return result;
    }

    public static List<ExecutableElement> getProxiableMethods(ProcessingEnvironment processingEnv, TypeElement classElement, Modifier[] accessModifiers) {
        if (accessModifiers == null) {
            String errMsg = MessageFormat.format("Access modifiers is null; Class element: {0}", classElement.toString());
            log.error(errMsg);
            throw CodegenException.of().message(errMsg).element(classElement).build();
        }
        List<ExecutableElement> result = new ArrayList<>();
        Elements utils = processingEnv.getElementUtils();
        List<? extends Element> members = utils.getAllMembers(classElement);
        List<ExecutableElement> methods = ElementFilter.methodsIn(members);
        for (ExecutableElement method : methods) {
            TypeElement methodClass = (TypeElement) method.getEnclosingElement();
            String methodClassName = methodClass.asType().toString();

            if (method.getModifiers().contains(Modifier.FINAL)
                    || method.getModifiers().contains(Modifier.PRIVATE)
                    || methodClass.getKind().isInterface()
                    || methodClassName.equals(Object.class.getName())) {
                continue;
            }

            boolean acceptable = false;
            for (Modifier mod : accessModifiers) {
                if (method.getModifiers().contains(mod)) {
                    acceptable = true;
                    break;
                }
            }

            if (!acceptable) {
                continue;
            }

            result.add(method);
        }
        return result;
    }


    public static void addSuperMethodCall(MethodSpec.Builder mb, boolean isConstructor, ExecutableElement method,
                                          String resultVarName, String paramPrefix) {

        // Process method parameters
        List<? extends VariableElement> methodParams = method.getParameters();
        List paramItems = new ArrayList();
        List<String> paramFormats = new ArrayList<>();

        for (VariableElement paramElm : methodParams) {
            String paramName = StrUtils.addPrefix(paramPrefix, paramElm.getSimpleName().toString());
            paramItems.add(paramName);
            paramFormats.add("$N");
        }

        if (resultVarName == null) {
            resultVarName = "writer";
        }

        if (isConstructor) {
            Object[] statementArgs = paramItems.toArray(new Object[paramItems.size()]);
            mb.addStatement("super(" + StringUtils.join(paramFormats, ",") + ")", statementArgs);
            return;
        } else {
            String methodName = getMethodName(method);
            TypeMirror retType = method.getReturnType();
            if (retType instanceof NoType) {
                paramItems.add(0, methodName);
                Object[] statementArgs = paramItems.toArray(new Object[paramItems.size()]);
                mb.addStatement("super.$N(" + StringUtils.join(paramFormats, ",") + ")", statementArgs);
                return;
            } else {
                paramItems.add(0, resultVarName);
                paramItems.add(1, methodName);
                Object[] statementArgs = paramItems.toArray();
                mb.addStatement("$N = super.$N(" + StringUtils.join(paramFormats, ",") + ")", statementArgs);
                return;
            }
        }
    }

    public static MethodSpec.Builder createBuilderProxyMethod(boolean isConstructor,
                                                              ExecutableElement baseMetod,
                                                              Modifier accessModifier,
                                                              String methodName,
                                                              String paramPrefix,
                                                              boolean paramFinalize,
                                                              boolean skipAnnotations) {
        if (baseMetod == null) {
            String errMsg = MessageFormat.format("Base method is null for method '{0}'", methodName);
            log.error(errMsg);
            throw CodegenException.of().message(errMsg).build();
        }
        if ((methodName == null) || isConstructor) {
            methodName = getMethodName(baseMetod);
        }
        MethodSpec.Builder mb;
        if (isConstructor) {
            mb = MethodSpec.constructorBuilder();
        } else {
            mb = MethodSpec.methodBuilder(methodName);
        }

        if (accessModifier == null) {
            if (baseMetod.getModifiers().contains(Modifier.PUBLIC)) {
                mb.addModifiers(Modifier.PUBLIC);
            } else if (baseMetod.getModifiers().contains(Modifier.PROTECTED)) {
                mb.addModifiers(Modifier.PROTECTED);
            } else if (baseMetod.getModifiers().contains(Modifier.PRIVATE)) {
                mb.addModifiers(Modifier.PRIVATE);
            }
        } else {
            mb.addModifiers(accessModifier);
        }

        if (!isConstructor) {
            mb.addAnnotation(Override.class);
            mb.returns(TypeName.get(baseMetod.getReturnType()));
        }

        List<? extends VariableElement> methodParams = baseMetod.getParameters();
        for (VariableElement paramElm : methodParams) {
            Set<Modifier> modifiersSet = new HashSet<>();
            modifiersSet.addAll(paramElm.getModifiers());
            if (paramFinalize) {
                modifiersSet.add(Modifier.FINAL);
            }
            Modifier[] modifiers = (Modifier[]) modifiersSet.toArray(new Modifier[modifiersSet.size()]);
            String paramName = StrUtils.addPrefix(paramPrefix, paramElm.getSimpleName().toString());

            ParameterSpec.Builder paramBilder = ParameterSpec.builder(TypeName.get(paramElm.asType()), paramName, modifiers);

            // Add param annotations
            if (!skipAnnotations) {
                List<AnnotationSpec> annotations = getElementAnnotationsSpec(paramElm);
                for (AnnotationSpec ann : annotations) {
                    paramBilder.addAnnotation(ann);
                }
            }

            mb.addParameter(paramBilder.build());
        }

        return mb;
    }

    public static void createJavaFile(ProcessingEnvironment processingEnv, TypeSpec typeSpec, String packageName, Element... linkedElements) {
        final JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .addFileComment("This is automatically generated file. Do not modify!")
                .skipJavaLangImports(true)
                .indent("    ")
                .build();

        String fullName = javaFile.packageName + "." + typeSpec.name;
        try {
            final JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(fullName, linkedElements);

            try (final Writer writer = new BufferedWriter(sourceFile.openWriter())) {
                javaFile.writeTo(writer);
            }
        } catch (IOException ex) {
            String errMsg = MessageFormat.format("Error creating java file: {0}; Cause message: {1}", fullName, ExceptionUtils.getRootCauseMessage(ex));
            log.error(errMsg);
            throw CodegenException.of().message(errMsg).build();
        }
    }

    public static void createTextResourceFile(ProcessingEnvironment processingEnv, String filePath, String fileText, Element... linkedElements) {
        try {
            final FileObject sourceFile = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", filePath, linkedElements);
            try (final Writer writer = new BufferedWriter(sourceFile.openWriter())) {
                writer.write(fileText);
            }
        } catch (IOException ex) {
            String errMsg = MessageFormat.format("Error creating text resource file: {0}; Cause message: {1}", filePath, ExceptionUtils.getRootCauseMessage(ex));
            log.error(errMsg);
            throw CodegenException.of().message(errMsg).build();
        }
    }

    public static String[] typeMirrorsToString(TypeMirror[] typeMirrors) {
        String[] result = new String[typeMirrors.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = typeMirrors[i].toString();
        }
        return result;
    }

    public static boolean checkPackageNameСonsistency(TypeElement typeElement, Elements elementUtils) {
        String moduleName = getModuleName(typeElement, elementUtils);
        String packageName = getPackageName(typeElement);
        return StringUtils.startsWith(packageName, moduleName);
    }

    public static void errorIfInconsistentPackageName(TypeElement typeElement, Elements elementUtils) {
        String moduleName = getModuleName(typeElement, elementUtils);
        String packageName = getPackageName(typeElement);
        if (!StringUtils.startsWith(packageName, moduleName)) {
            throw CodegenException.of()
                    .message("Inconsistent package name: '" + packageName + "'. Package name should start with partition name: '" + moduleName + "'")
                    .element(typeElement)
                    .build();
        }
    }

    public static boolean checkPackageAccessibility(ModuleElement module, String packageName, String targetModule) {
        if (module.isOpen()) {
            return true;
        }

        if (module.isUnnamed()) {
            return true;
        }

        List<? extends ModuleElement.Directive> directives = module.getDirectives();

        for (ModuleElement.Directive d : directives) {
            if (d.getKind() != ModuleElement.DirectiveKind.EXPORTS) {
                continue;
            }
            ModuleElement.ExportsDirective ed = (ModuleElement.ExportsDirective) d;
            String pkgName = ed.getPackage().toString();
            if (!packageName.equals(pkgName)) {
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

    public static TypeMirror typeMirrorFromClass(Class clazz, ProcessingEnvironment processingEnv) {
        return processingEnv.getElementUtils().getTypeElement(clazz.getCanonicalName()).asType();
    }

    public static final String ISO_DT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static AnnotationSpec buildGenstampAnnotation(String generatorName, String hashId, String comments) {
        SimpleDateFormat sdf = new SimpleDateFormat(ISO_DT);

        /*
        AnnotationSpec.Builder generatedAnn = AnnotationSpec.builder(Generated.class);
        generatedAnn.addMember("value", "$S", generatorName);
        generatedAnn.addMember("date", "$S", sdf.format(new Date()));
        if (comments != null) {
            generatedAnn.addMember("comments", "$S", comments);
        }
        */

        AnnotationSpec.Builder generatedAnn = AnnotationSpec.builder(Genstamp.class);
        generatedAnn.addMember("generator", "$S", generatorName);
        generatedAnn.addMember("timestamp", "$S", sdf.format(new Date()));
        if (hashId==null){
            hashId = UUID.randomUUID().toString();
        }
        generatedAnn.addMember("hashId", "$S", hashId);
        if (comments != null) {
            generatedAnn.addMember("comments", "$S", comments);
        }
        return generatedAnn.build();
    }

    public static List<AnnotationSpec> getElementAnnotationsSpec(Element element) {
        List<AnnotationSpec> result = new ArrayList<>();
        List<? extends AnnotationMirror> annotations = element.getAnnotationMirrors();
        for (AnnotationMirror ann : annotations) {
            TypeElement annTypeElm = (TypeElement) ann.getAnnotationType().asElement();
            ClassName annClassName = ClassName.get(annTypeElm);
            AnnotationSpec.Builder annBuilder = AnnotationSpec.builder(annClassName);

            Map<? extends ExecutableElement, ? extends AnnotationValue> annValues = ann.getElementValues();

            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annValues.entrySet()) {
                String name = entry.getKey().getSimpleName().toString();
                AnnotationValue value = entry.getValue();

                // unstable untested code:
                annBuilder.addMember(name, "$N", value.toString());
            }

            result.add(annBuilder.build());
        }
        return result;
    }

    public static String getOption(ProcessingEnvironment processingEnv,String optionKey){
        return processingEnv.getOptions().get(optionKey);
    }
}
