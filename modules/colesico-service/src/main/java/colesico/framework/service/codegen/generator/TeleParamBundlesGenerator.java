package colesico.framework.service.codegen.generator;


import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.service.codegen.model.teleapi.TeleParamBundleElement;
import colesico.framework.service.codegen.model.teleapi.TeleFieldParamElement;
import colesico.framework.service.codegen.model.teleapi.TeleParamBundlesPackElement;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

public class TeleParamBundlesGenerator extends FrameworkAbstractGenerator {
    public TeleParamBundlesGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    public void generate(TeleParamBundlesPackElement paramBundlePack) {
        if (paramBundlePack.isEmpty()) {
            return;
        }

        TypeSpec.Builder pb = TypeSpec.classBuilder(paramBundlePack.packClassSimpleName());
        pb.addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        for (TeleParamBundleElement paramBundle : paramBundlePack.paramBundles()) {
            TypeSpec.Builder rb = TypeSpec.classBuilder(paramBundle.paramBundleClassSimpleName());
            rb.addModifiers(Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC);

            for (TeleFieldParamElement paramBundleField : paramBundle.fields()) {
                TypeName filedTypeName = TypeName.get(paramBundleField.originElement().originType());
                String filedName = paramBundleField.name();
                FieldSpec.Builder fb = FieldSpec.builder(filedTypeName, filedName, Modifier.PRIVATE);
                rb.addField(fb.build());

                MethodSpec.Builder gb = MethodSpec.methodBuilder(paramBundleField.getterName());
                gb.returns(filedTypeName);
                gb.addModifiers(Modifier.PUBLIC);
                gb.addStatement("return this.$N", filedName);
                rb.addMethod(gb.build());

                MethodSpec.Builder sb = MethodSpec.methodBuilder(paramBundleField.setterName());
                sb.returns(TypeName.VOID);
                sb.addModifiers(Modifier.PUBLIC);
                sb.addParameter(filedTypeName, filedName);
                sb.addStatement("this.$N = $N", filedName, filedName);
                rb.addMethod(sb.build());
            }

            pb.addType(rb.build());
        }

        String packageName = paramBundlePack.parentTeleFacade().parentService().originClass().packageName();
        CodegenUtils.createJavaFile(processingEnv, pb.build(), packageName, paramBundlePack.parentTeleFacade().parentService().originClass().unwrap());
    }
}
