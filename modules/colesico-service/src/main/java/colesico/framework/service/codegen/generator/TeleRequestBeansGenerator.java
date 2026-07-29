package colesico.framework.service.codegen.generator;


import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.service.codegen.model.teleapi.TeleRequestBeanElement;
import colesico.framework.service.codegen.model.teleapi.TeleFieldParamElement;
import colesico.framework.service.codegen.model.teleapi.TeleRequestBeanPackElement;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

public class TeleRequestBeansGenerator extends FrameworkAbstractGenerator {
    public TeleRequestBeansGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    public void generate(TeleRequestBeanPackElement requestBeanPack) {
        if (requestBeanPack.isEmpty()) {
            return;
        }

        TypeSpec.Builder pb = TypeSpec.classBuilder(requestBeanPack.packClassSimpleName());
        pb.addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        for (TeleRequestBeanElement requestBean : requestBeanPack.requestBeans()) {
            TypeSpec.Builder rb = TypeSpec.classBuilder(requestBean.requestBeanClassSimpleName());
            rb.addModifiers(Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC);

            for (TeleFieldParamElement requestBeanField : requestBean.fields()) {
                TypeName filedTypeName = TypeName.get(requestBeanField.originElement().originType());
                String filedName = requestBeanField.name();
                FieldSpec.Builder fb = FieldSpec.builder(filedTypeName, filedName, Modifier.PRIVATE);
                rb.addField(fb.build());

                MethodSpec.Builder gb = MethodSpec.methodBuilder(requestBeanField.getterName());
                gb.returns(filedTypeName);
                gb.addModifiers(Modifier.PUBLIC);
                gb.addStatement("return this.$N", filedName);
                rb.addMethod(gb.build());

                MethodSpec.Builder sb = MethodSpec.methodBuilder(requestBeanField.setterName());
                sb.returns(TypeName.VOID);
                sb.addModifiers(Modifier.PUBLIC);
                sb.addParameter(filedTypeName, filedName);
                sb.addStatement("this.$N = $N", filedName, filedName);
                rb.addMethod(sb.build());
            }

            pb.addType(rb.build());
        }

        String packageName = requestBeanPack.parentTeleFacade().parentService().originClass().packageName();
        CodegenUtils.createJavaFile(processingEnv, pb.build(), packageName, requestBeanPack.parentTeleFacade().parentService().originClass().unwrap());
    }
}
