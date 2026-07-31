package colesico.framework.service.codegen.generator;


import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.service.codegen.model.teleapi.TeleCompositionElement;
import colesico.framework.service.codegen.model.teleapi.TeleCompositionFieldElement;
import colesico.framework.service.codegen.model.teleapi.TeleCompositionsPackElement;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

public class TeleCompositionsGenerator extends FrameworkAbstractGenerator {
    public TeleCompositionsGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    public void generate(TeleCompositionsPackElement compositionsPack) {
        if (compositionsPack.isEmpty()) {
            return;
        }

        TypeSpec.Builder pb = TypeSpec.classBuilder(compositionsPack.packClassSimpleName());
        pb.addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        for (TeleCompositionElement composition : compositionsPack.compositions()) {
            TypeSpec.Builder rb = TypeSpec.classBuilder(composition.compositionClassSimpleName());
            rb.addModifiers(Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC);

            for (TeleCompositionFieldElement compositionField : composition.fields()) {
                TypeName filedTypeName = TypeName.get(compositionField.originVariable().originType());
                String filedName = compositionField.name();
                FieldSpec.Builder fb = FieldSpec.builder(filedTypeName, filedName, Modifier.PRIVATE);
                rb.addField(fb.build());

                MethodSpec.Builder gb = MethodSpec.methodBuilder(compositionField.getterName());
                gb.returns(filedTypeName);
                gb.addModifiers(Modifier.PUBLIC);
                gb.addStatement("return this.$N", filedName);
                rb.addMethod(gb.build());

                MethodSpec.Builder sb = MethodSpec.methodBuilder(compositionField.setterName());
                sb.returns(TypeName.VOID);
                sb.addModifiers(Modifier.PUBLIC);
                sb.addParameter(filedTypeName, filedName);
                sb.addStatement("this.$N = $N", filedName, filedName);
                rb.addMethod(sb.build());
            }

            pb.addType(rb.build());
        }

        String packageName = compositionsPack.parentTeleFacade().parentService().originClass().packageName();
        CodegenUtils.createJavaFile(processingEnv, pb.build(), packageName, compositionsPack.parentTeleFacade().parentService().originClass().unwrap());
    }
}
