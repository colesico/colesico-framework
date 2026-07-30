package colesico.framework.service.codegen.generator;


import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.service.codegen.model.teleapi.TeleBeanElement;
import colesico.framework.service.codegen.model.teleapi.TeleBeanFieldElement;
import colesico.framework.service.codegen.model.teleapi.TeleBeansPackElement;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

public class TeleBeansGenerator extends FrameworkAbstractGenerator {
    public TeleBeansGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    public void generate(TeleBeansPackElement paramBeanPack) {
        if (paramBeanPack.isEmpty()) {
            return;
        }

        TypeSpec.Builder pb = TypeSpec.classBuilder(paramBeanPack.packClassSimpleName());
        pb.addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        for (TeleBeanElement paramBean : paramBeanPack.paramBeans()) {
            TypeSpec.Builder rb = TypeSpec.classBuilder(paramBean.paramBeanClassSimpleName());
            rb.addModifiers(Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC);

            for (TeleBeanFieldElement paramBeanField : paramBean.fields()) {
                TypeName filedTypeName = TypeName.get(paramBeanField.originElement().originType());
                String filedName = paramBeanField.name();
                FieldSpec.Builder fb = FieldSpec.builder(filedTypeName, filedName, Modifier.PRIVATE);
                rb.addField(fb.build());

                MethodSpec.Builder gb = MethodSpec.methodBuilder(paramBeanField.getterName());
                gb.returns(filedTypeName);
                gb.addModifiers(Modifier.PUBLIC);
                gb.addStatement("return this.$N", filedName);
                rb.addMethod(gb.build());

                MethodSpec.Builder sb = MethodSpec.methodBuilder(paramBeanField.setterName());
                sb.returns(TypeName.VOID);
                sb.addModifiers(Modifier.PUBLIC);
                sb.addParameter(filedTypeName, filedName);
                sb.addStatement("this.$N = $N", filedName, filedName);
                rb.addMethod(sb.build());
            }

            pb.addType(rb.build());
        }

        String packageName = paramBeanPack.parentTeleFacade().parentService().originClass().packageName();
        CodegenUtils.createJavaFile(processingEnv, pb.build(), packageName, paramBeanPack.parentTeleFacade().parentService().originClass().unwrap());
    }
}
