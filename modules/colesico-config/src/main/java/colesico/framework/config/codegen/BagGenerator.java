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

package colesico.framework.config.codegen;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.introspection.Introspect;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import java.util.List;

/**
 * Generates a Bag from config bean class.
 * Bag contains only fields of config bean that to be read from config source.
 */
public class BagGenerator extends FrameworkAbstractGenerator {

    public BagGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    private void generateBagProperties(TypeSpec.Builder bagBuilder, ConfigElement confElement) {
        for (SourceValueElement sve : confElement.getSource().getSourceValues()) {

            String fieldName = sve.getOriginField().getName();
            TypeName fieldTypeName = TypeName.get(sve.getOriginField().getOriginType());
            FieldSpec.Builder fb = FieldSpec.builder(fieldTypeName, fieldName, Modifier.PRIVATE);
            bagBuilder.addField(fb.build());

            MethodSpec.Builder sb = MethodSpec.methodBuilder("set" + StrUtils.firstCharToUpperCase(fieldName));
            sb.addModifiers(Modifier.PUBLIC);
            sb.returns(TypeName.VOID);
            sb.addParameter(fieldTypeName, fieldName);
            sb.addStatement("this.$N = $N", fieldName, fieldName);
            bagBuilder.addMethod(sb.build());

            MethodSpec.Builder gb = MethodSpec.methodBuilder("get" + StrUtils.firstCharToUpperCase(fieldName));
            gb.addModifiers(Modifier.PUBLIC);
            gb.returns(fieldTypeName);
            gb.addStatement("return this.$N", fieldName);
            bagBuilder.addMethod(gb.build());
        }
    }

    private void generateBagConstructor(TypeSpec.Builder bagBuilder) {
        MethodSpec.Builder mb = MethodSpec.constructorBuilder();
        mb.addModifiers(Modifier.PUBLIC);
        bagBuilder.addMethod(mb.build());
    }

    public void generateConfigBag(ConfigElement confElement) {
        String classSimpleName = confElement.getSource().getBagClassSimpleName();
        String packageName = confElement.getOriginClass().getPackageName();

        TypeSpec.Builder bagBuilder = TypeSpec.classBuilder(classSimpleName);

        bagBuilder.addAnnotation(CodegenUtils.generateGenstamp(BagGenerator.class.getName(), null, null));
        bagBuilder.addAnnotation(Introspect.class);

        bagBuilder.addModifiers(Modifier.PUBLIC);

        generateBagConstructor(bagBuilder);
        generateBagProperties(bagBuilder, confElement);

        final TypeSpec typeSpec = bagBuilder.build();
        CodegenUtils.createJavaFile(processingEnv, typeSpec, packageName, confElement.getOriginClass().unwrap());
    }

    public void generate(List<ConfigElement> configElements) {
        for (ConfigElement confElement : configElements) {
            logger.debug("Generate config bag for: " + confElement.toString());
            if (confElement.getSource() != null) {
                generateConfigBag(confElement);
            }
        }
    }
}
