package colesico.framework.restlet.codegen;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.restlet.codegen.model.JsonFieldElement;
import colesico.framework.restlet.codegen.model.JsonRequestElement;
import colesico.framework.restlet.codegen.model.JsonPackElement;
import colesico.framework.restlet.teleapi.jsonrequest.JsonRequest;
import com.squareup.javapoet.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

public class JsonPackGenerator extends FrameworkAbstractGenerator {
    public JsonPackGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    public void generate(JsonPackElement jsonPack) {
        TypeSpec.Builder pb = TypeSpec.classBuilder(jsonPack.getJsonPackClassSimpleName());
        pb.addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        for (JsonRequestElement jsonRequest : jsonPack.getRequests()) {
            TypeSpec.Builder rb = TypeSpec.classBuilder(jsonRequest.getJsonRequestClassSimpleName());
            rb.addModifiers(Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC);
            rb.addSuperinterface(ClassName.get(JsonRequest.class));

            for (JsonFieldElement jsonField : jsonRequest.getFields()) {
                TypeName filedTypeName = TypeName.get(jsonField.getOriginParam().getOriginParam().getOriginType());
                String filedName = jsonField.getName();
                FieldSpec.Builder fb = FieldSpec.builder(filedTypeName, filedName, Modifier.PRIVATE);
                rb.addField(fb.build());

                MethodSpec.Builder gb = MethodSpec.methodBuilder(jsonField.getterName());
                gb.returns(filedTypeName);
                gb.addModifiers(Modifier.PUBLIC);
                gb.addStatement("return this.$N", filedName);
                rb.addMethod(gb.build());

                MethodSpec.Builder sb = MethodSpec.methodBuilder(jsonField.setterName());
                sb.returns(TypeName.VOID);
                sb.addModifiers(Modifier.PUBLIC);
                sb.addParameter(filedTypeName, filedName);
                sb.addStatement("this.$N = $N", filedName, filedName);
                rb.addMethod(sb.build());
            }

            pb.addType(rb.build());
        }

        String packageName = jsonPack.getOriginTeleFacade().getParentService().getOriginClass().getPackageName();
        CodegenUtils.createJavaFile(processingEnv, pb.build(), packageName, jsonPack.getOriginTeleFacade().getParentService().getOriginClass().unwrap());
    }
}
