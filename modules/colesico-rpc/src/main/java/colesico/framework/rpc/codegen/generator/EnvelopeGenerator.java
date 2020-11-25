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

public class EnvelopeGenerator extends FrameworkAbstractGenerator {

    private final EnvelopeGeneratorPluginKit pluginKit;

    public EnvelopeGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
        this.pluginKit = new EnvelopeGeneratorPluginKit();
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

    private void generateRequestEnvelope(TypeSpec.Builder envelopeBuilder, RpcApiMethodElement method) {
        TypeSpec.Builder requestBuilder = TypeSpec.classBuilder(method.getRequestClassSimpleName());
        requestBuilder.addJavadoc("RPC request for method " + method.getParentApi().getOriginInterface().getName() +
                "->" + method.getOriginMethod().getName());
        requestBuilder.addModifiers(Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC);
        requestBuilder.superclass(ClassName.get(RpcRequest.class));
        generateParams(requestBuilder, method);
        pluginKit.notifyGenerateRequestEnvelope(requestBuilder, method);
        envelopeBuilder.addType(requestBuilder.build());
    }

    private void generateResponseEnvelope(TypeSpec.Builder envelopeBuilder, RpcApiMethodElement method) {
        TypeSpec.Builder responseBuilder = TypeSpec.classBuilder(method.getResponseClassSimpleName());
        responseBuilder.addModifiers(Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC);

        //TODO: handle void type
        ParameterizedTypeName responseType = ParameterizedTypeName.get(ClassName.get(RpcResponse.class),
                TypeName.get(method.getOriginMethod().getReturnType()));
        responseBuilder.superclass(responseType);

        pluginKit.notifyGenerateResponseEnvelope(responseBuilder, method);
        envelopeBuilder.addType(responseBuilder.build());
    }

    private void generateMethodsSchemas(TypeSpec.Builder envelopeBuilder, RpcApiElement rpcApiElm) {
        for (RpcApiMethodElement method : rpcApiElm.getRpcMethods()) {
            generateRequestEnvelope(envelopeBuilder, method);
            generateResponseEnvelope(envelopeBuilder, method);
        }
    }

    public void generate(RpcApiElement rpcApiElm) {
        String classSimpleName = rpcApiElm.getEnvelopePackClassName();
        String packageName = rpcApiElm.getOriginInterface().getPackageName();

        TypeSpec.Builder envelopeBuilder = TypeSpec.classBuilder(classSimpleName);
        envelopeBuilder.addModifiers(Modifier.PUBLIC);

        AnnotationSpec genstamp = CodegenUtils.generateGenstamp(this.getClass().getName(), null, "RPC interface: " + rpcApiElm.getOriginInterface().getName());
        envelopeBuilder.addAnnotation(genstamp);


        generateMethodsSchemas(envelopeBuilder, rpcApiElm);

        final TypeSpec typeSpec = envelopeBuilder.build();
        CodegenUtils.createJavaFile(processingEnv, typeSpec, packageName, rpcApiElm.getOriginInterface().unwrap());

    }
}
