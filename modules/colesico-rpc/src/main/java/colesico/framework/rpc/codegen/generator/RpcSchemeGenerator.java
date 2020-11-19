package colesico.framework.rpc.codegen.generator;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.rpc.codegen.model.RpcApiElement;
import colesico.framework.rpc.codegen.model.RpcApiMethodElement;
import colesico.framework.rpc.codegen.model.RpcApiParamElement;
import colesico.framework.rpc.teleapi.RpcRequest;
import colesico.framework.rpc.teleapi.RpcResponse;
import com.squareup.javapoet.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

public class RpcSchemeGenerator extends FrameworkAbstractGenerator {


    public RpcSchemeGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    private void generateParams(TypeSpec.Builder requestBuilder, RpcApiMethodElement method) {
        for (RpcApiParamElement param : method.getParameters()) {
            FieldSpec.Builder field = FieldSpec.builder(TypeName.get(param.getParamType()), param.fieldName(), Modifier.PRIVATE);

            field.addJavadoc(param.getOriginParam().getName() + " method parameter");
            requestBuilder.addField(field.build());

            MethodSpec.Builder getter = MethodSpec.methodBuilder(param.getterName());
            getter.addModifiers(Modifier.PUBLIC);
            getter.returns(TypeName.get(param.getParamType()));
            getter.addStatement("return $N", param.fieldName());
            requestBuilder.addMethod(getter.build());

            MethodSpec.Builder setter = MethodSpec.methodBuilder(param.setterName());
            setter.addModifiers(Modifier.PUBLIC);
            setter.returns(TypeName.VOID);
            setter.addParameter(TypeName.get(param.getParamType()), param.fieldName());
            setter.addStatement("this.$N = $N", param.fieldName(), param.fieldName());
            requestBuilder.addMethod(setter.build());
        }
    }

    private void generateRequestScheme(TypeSpec.Builder schemeBuilder, RpcApiMethodElement method) {
        TypeSpec.Builder requestBuilder = TypeSpec.classBuilder(method.getRequestClassName());
        requestBuilder.addJavadoc("RPC request for method " + method.getParentApi().getOriginInterface().getName() +
                "->" + method.getOriginMethod().getName());
        requestBuilder.addModifiers(Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC);
        requestBuilder.superclass(ClassName.get(RpcRequest.class));
        generateParams(requestBuilder, method);
        schemeBuilder.addType(requestBuilder.build());
    }

    private void generateResponseScheme(TypeSpec.Builder schemeBuilder, RpcApiMethodElement method) {
        TypeSpec.Builder responseBuilder = TypeSpec.classBuilder(method.getResponseClassName());
        responseBuilder.addModifiers(Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC);
        responseBuilder.superclass(ClassName.get(RpcResponse.class));

        schemeBuilder.addType(responseBuilder.build());
    }

    private void generateMethodsSchemas(TypeSpec.Builder schemeBuilder, RpcApiElement rpcApiElm) {
        for (RpcApiMethodElement method : rpcApiElm.getRpcMethods()) {
            generateRequestScheme(schemeBuilder, method);
            generateResponseScheme(schemeBuilder, method);
        }
    }

    public void generate(RpcApiElement rpcApiElm) {
        String classSimpleName = rpcApiElm.getSchemeClassName();
        String packageName = rpcApiElm.getOriginInterface().getPackageName();

        TypeSpec.Builder schemeBuilder = TypeSpec.classBuilder(classSimpleName);
        schemeBuilder.addModifiers(Modifier.PUBLIC);

        generateMethodsSchemas(schemeBuilder, rpcApiElm);

        final TypeSpec typeSpec = schemeBuilder.build();
        CodegenUtils.createJavaFile(processingEnv, typeSpec, packageName, rpcApiElm.getOriginInterface().unwrap());

    }
}
