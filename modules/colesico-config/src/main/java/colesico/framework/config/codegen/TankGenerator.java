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
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import java.util.List;

public class TankGenerator extends FrameworkAbstractGenerator {

    public TankGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    private void generateTankProperties(TypeSpec.Builder mirrorBuilder, ConfigElement confElement) {
        for (SourceValueElement sve : confElement.getSource().getSourceValues()) {

            String fieldName = sve.getOriginField().getName();
            TypeName fieldTypeName = TypeName.get(sve.getOriginField().asType());
            FieldSpec.Builder fb = FieldSpec.builder(fieldTypeName, fieldName, Modifier.PRIVATE);
            mirrorBuilder.addField(fb.build());

            MethodSpec.Builder sb = MethodSpec.methodBuilder("set" + StrUtils.firstCharToUpperCase(fieldName));
            sb.addModifiers(Modifier.PUBLIC);
            sb.returns(TypeName.VOID);
            sb.addParameter(fieldTypeName, fieldName);
            sb.addStatement("this.$N = $N", fieldName, fieldName);
            mirrorBuilder.addMethod(sb.build());

            MethodSpec.Builder gb = MethodSpec.methodBuilder("get" + StrUtils.firstCharToUpperCase(fieldName));
            gb.addModifiers(Modifier.PUBLIC);
            gb.returns(fieldTypeName);
            gb.addStatement("return this.$N", fieldName);
            mirrorBuilder.addMethod(gb.build());
        }
    }

    public void generateConfigTank(ConfigElement confElement) {
        String classSimpleName = confElement.getTankClassSimpleName();
        String packageName = confElement.getImplementation().getPackageName();

        TypeSpec.Builder tankBuilder = TypeSpec.classBuilder(classSimpleName);
        tankBuilder.addModifiers(Modifier.PUBLIC);
        tankBuilder.addAnnotation(CodegenUtils.generateGenstamp(TankGenerator.class.getName(), null, null));

        generateTankProperties(tankBuilder,confElement);

        final TypeSpec typeSpec = tankBuilder.build();
        CodegenUtils.createJavaFile(processingEnv, typeSpec, packageName, confElement.getImplementation().unwrap());
    }

    public void generate(List<ConfigElement> configElements) {
        for (ConfigElement confElement : configElements) {
            logger.debug("Generate config tank for: " + confElement.toString());
            if (confElement.getSource()!=null) {
                generateConfigTank(confElement);
            }
        }
    }
}
