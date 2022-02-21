package colesico.framework.service.codegen.generator;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.service.codegen.model.TeleBatchElement;
import colesico.framework.service.codegen.model.TeleBatchFieldElement;
import colesico.framework.service.codegen.model.TeleBatchPackElement;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

public class TeleBatchesGenerator extends FrameworkAbstractGenerator {
    public TeleBatchesGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    public void generate(TeleBatchPackElement batchPack) {
        TypeSpec.Builder pb = TypeSpec.classBuilder(batchPack.getBatchPackClassSimpleName());
        pb.addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        for (TeleBatchElement batch : batchPack.getBatches()) {
            TypeSpec.Builder rb = TypeSpec.classBuilder(batch.getBatchClassSimpleName());
            rb.addModifiers(Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC);

            for (TeleBatchFieldElement batchField : batch.getFields()) {
                TypeName filedTypeName = TypeName.get(batchField.getOriginElement().getOriginType());
                String filedName = batchField.getName();
                FieldSpec.Builder fb = FieldSpec.builder(filedTypeName, filedName, Modifier.PRIVATE);
                rb.addField(fb.build());

                MethodSpec.Builder gb = MethodSpec.methodBuilder(batchField.getterName());
                gb.returns(filedTypeName);
                gb.addModifiers(Modifier.PUBLIC);
                gb.addStatement("return this.$N", filedName);
                rb.addMethod(gb.build());

                MethodSpec.Builder sb = MethodSpec.methodBuilder(batchField.setterName());
                sb.returns(TypeName.VOID);
                sb.addModifiers(Modifier.PUBLIC);
                sb.addParameter(filedTypeName, filedName);
                sb.addStatement("this.$N = $N", filedName, filedName);
                rb.addMethod(sb.build());
            }

            pb.addType(rb.build());
        }

        String packageName = batchPack.getOriginTeleFacade().getParentService().getOriginClass().getPackageName();
        CodegenUtils.createJavaFile(processingEnv, pb.build(), packageName, batchPack.getOriginTeleFacade().getParentService().getOriginClass().unwrap());
    }
}
