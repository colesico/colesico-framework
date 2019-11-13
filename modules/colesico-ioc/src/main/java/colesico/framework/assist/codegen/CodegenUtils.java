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
package colesico.framework.assist.codegen;


import colesico.framework.assist.TypeWrapper;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.assist.codegen.model.ParameterElement;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Code generation helper
 *
 * @author Vladlen Larionov
 */
public class CodegenUtils {

    public static final String OPTION_CODEGEN = "colesico.framework.codegen";

    public static final String ISO_DT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static void createJavaFile(ProcessingEnvironment procEnv, TypeSpec typeSpec, String packageName, Element... linkedElements) {
        final JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
            .addFileComment("This is automatically generated file. Do not modify!")
            .skipJavaLangImports(true)
            .indent("    ")
            .build();

        String fullName = javaFile.packageName + "." + typeSpec.name;
        try {
            final JavaFileObject sourceFile = procEnv.getFiler().createSourceFile(fullName, linkedElements);

            try (final Writer writer = new BufferedWriter(sourceFile.openWriter())) {
                javaFile.writeTo(writer);
            }
        } catch (IOException ex) {
            String errMsg = MessageFormat.format("Error creating java file: {0}; Cause message: {1}", fullName, ExceptionUtils.getRootCauseMessage(ex));
            throw CodegenException.of().message(errMsg).build();
        }
    }

    public static void createTextResourceFile(ProcessingEnvironment procEnv, String filePath, String text, Element... linkedElements) {
        try {
            final FileObject sourceFile = procEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", filePath, linkedElements);
            try (final Writer writer = new BufferedWriter(sourceFile.openWriter())) {
                writer.write(text);
            }
        } catch (IOException ex) {
            String errMsg = MessageFormat.format("Error creating text resource file: {0}; Cause message: {1}", filePath, ExceptionUtils.getRootCauseMessage(ex));
            throw CodegenException.of().message(errMsg).build();
        }
    }

    public static AnnotationSpec generateGenstamp(String generatorName, String hashId, String comments) {
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
        if (hashId == null) {
            hashId = UUID.randomUUID().toString();
        }
        generatedAnn.addMember("hashId", "$S", hashId);
        if (comments != null) {
            generatedAnn.addMember("comments", "$S", comments);
        }
        return generatedAnn.build();
    }

    public static CodeBlock generateSuperMethodCall(MethodElement method, String resultVarName, String paramPrefix) {
        if (StringUtils.isBlank(paramPrefix)) {
            paramPrefix = "";
        }
        if (resultVarName == null) {
            resultVarName = "writer";
        }

        // Process method parameters
        List<ParameterElement> methodParams = method.getParameters();
        ArrayCodegen paramsGen = new ArrayCodegen();

        for (ParameterElement param : methodParams) {
            String paramName = param.getNameWithPrefix(paramPrefix);
            paramsGen.add("$N", paramName);
        }

        CodeBlock.Builder cb = CodeBlock.builder();
        if (method.isConstractor()) {
            cb.addStatement("super(" + paramsGen.toFormat() + ")", paramsGen.toValues());
            return cb.build();
        } else {
            TypeMirror retType = method.getReturnType();
            if (retType instanceof NoType) {
                cb.add("super.$N(", method.getName());
                cb.add(paramsGen.toFormat(), paramsGen.toValues());
                cb.add(");\n");
                return cb.build();
            } else {
                cb.add("$N = super.$N(", resultVarName, method.getName());
                cb.add(paramsGen.toFormat(), paramsGen.toValues());
                cb.add(");\n");
                return cb.build();
            }
        }
    }

    public static MethodSpec.Builder createProxyMethodBuilder(MethodElement method, String methodPrefix, String paramPrefix, boolean skipAnnotations) {
        MethodSpec.Builder mb;
        if (method.isConstractor()) {
            mb = MethodSpec.constructorBuilder();
        } else {
            String proxyMethodName = method.getNameWithPrefix(methodPrefix);
            mb = MethodSpec.methodBuilder(proxyMethodName);
            mb.addAnnotation(Override.class);

            List<? extends TypeParameterElement> generics = method.unwrap().getTypeParameters();
            for (TypeParameterElement tpe : generics) {
                mb.addTypeVariable(TypeVariableName.get(tpe));
            }

            TypeMirror returnType = method.getReturnType();
            mb.returns(TypeName.get(returnType));

        }

        if (method.unwrap().getModifiers().contains(Modifier.PUBLIC)) {
            mb.addModifiers(Modifier.PUBLIC);
        } else if (method.unwrap().getModifiers().contains(Modifier.PROTECTED)) {
            mb.addModifiers(Modifier.PROTECTED);
        }

        List<ParameterSpec> params = generateProxyMethodParams(method, paramPrefix, skipAnnotations);
        for (ParameterSpec ps : params) {
            mb.addParameter(ps);
        }
        return mb;
    }

    public static List<ParameterSpec> generateProxyMethodParams(MethodElement method, String paramPrefix, boolean skipAnnotations) {
        List<ParameterSpec> result = new ArrayList<>();

        List<ParameterElement> methodParams = method.getParameters();
        for (ParameterElement param : methodParams) {
            Set<Modifier> modifiersSet = new HashSet<>(param.unwrap().getModifiers());
            modifiersSet.add(Modifier.FINAL);

            Modifier[] modifiers = modifiersSet.toArray(new Modifier[modifiersSet.size()]);
            String paramName = param.getNameWithPrefix(paramPrefix);
            TypeMirror paramType = param.asType();

            ParameterSpec.Builder paramBilder = ParameterSpec.builder(TypeName.get(paramType), paramName, modifiers);

            // Add param annotations
            if (!skipAnnotations) {
                List<AnnotationSpec> annotations = generateAnnotations(param.unwrap());
                for (AnnotationSpec ann : annotations) {
                    paramBilder.addAnnotation(ann);
                }
            }
            result.add(paramBilder.build());
        }
        return result;
    }

    public static List<AnnotationSpec> generateAnnotations(Element element) {
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

    /**
     * Generates type picking from type mirror with generics support.
     *
     * @param typeMirror
     * @param cb
     * @return true if the code is generated for generics type with TypeWrapper usage
     */
    public static boolean generateTypePick(TypeMirror typeMirror, CodeBlock.Builder cb) {

        boolean isGenericType;

        if (!(typeMirror instanceof DeclaredType)) {
            isGenericType = false;
        } else {
            DeclaredType declaredType = (DeclaredType) typeMirror;
            isGenericType = !declaredType.getTypeArguments().isEmpty(); // Actual types  e.g. <String,Long>
            //TypeElement typeElement = (TypeElement) declaredType.asElement();
            //isGenericType = !typeElement.getTypeParameters().isEmpty(); // Generic parameters e.g. "<T,V>"
        }

        if (isGenericType) {
            TypeName wrapperType = ParameterizedTypeName.get(ClassName.get(TypeWrapper.class), TypeName.get(typeMirror));
            cb.add("new $T(){}.$N()", wrapperType, TypeWrapper.UNWRAP_METHOD);
        } else {
            cb.add("$T.class", TypeName.get(typeMirror));
        }
        return isGenericType;
    }
}
