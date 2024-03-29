package colesico.framework.rpc.codegen.generator;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.rpc.codegen.model.RpcApiElement;
import colesico.framework.rpc.codegen.model.RpcApiMethodElement;
import colesico.framework.rpc.codegen.model.RpcApiParamElement;
import colesico.framework.rpc.clientapi.RpcClient;
import colesico.framework.rpc.teleapi.RpcResponse;
import com.squareup.javapoet.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;

public class ClientGenerator extends FrameworkAbstractGenerator {

    public static final String RPC_CLIENT_FIELD = "rpcClient";
    public static final String RESPONSE_VAR = "response";

    public ClientGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    protected void generateParamAssignment(MethodSpec.Builder mb, RpcApiMethodElement methodElm) {
        for (RpcApiParamElement param : methodElm.getParameters()) {
            mb.addStatement("$N.$N($N)",
                    RpcClient.REQUEST_PARAM,
                    param.setterName(),
                    param.getOriginParam().getName()
            );
        }
    }

    protected void generateMethods(TypeSpec.Builder clientBuilder, RpcApiElement rpcApiElm) {
        for (RpcApiMethodElement methodElm : rpcApiElm.getRpcMethods()) {
            MethodSpec.Builder mb = CodegenUtils.createProxyMethodBuilder(methodElm.getOriginMethod(), null, null, true);

            TypeName requestTypeName = ClassName.bestGuess(methodElm.getRequestClassName());

            // RpcEnvelope.RpcRequest request = new RpcEnvelope.RpcRequest()
            mb.addStatement("$T $N = new $T()",
                    requestTypeName,
                    RpcClient.REQUEST_PARAM,
                    requestTypeName
            );

            generateParamAssignment(mb, methodElm);

            TypeName returnTypeName;
            boolean notVoidReturn = methodElm.getOriginMethod().getReturnType().getKind() != TypeKind.VOID;
            if (notVoidReturn) {
                returnTypeName = ParameterizedTypeName.get(ClassName.get(RpcResponse.class), TypeName.get(methodElm.getOriginMethod().getReturnType()));
            } else {
                returnTypeName = ParameterizedTypeName.get(ClassName.get(RpcResponse.class), ClassName.get(Object.class));
            }
            // RpcResponse<T> response = rpcClient.call("namespace","api","method", request, RpcResponse.class)
            mb.addStatement("$T $N = $N.$N($S, $S, $S, $N, $T.class)",
                    returnTypeName,
                    RESPONSE_VAR,
                    RPC_CLIENT_FIELD,
                    RpcClient.CALL_METHOD,
                    methodElm.getParentApi().getRpcNamespace(),
                    methodElm.getParentApi().rpcName(),
                    methodElm.rpcMethodName(),
                    RpcClient.REQUEST_PARAM,
                    ClassName.bestGuess(methodElm.getResponseClassName())
            );

            if (notVoidReturn) {
                mb.addStatement("return $N.$N()",
                        RESPONSE_VAR,
                        RpcResponse.GET_RESULT_METHOD
                );
            }
            clientBuilder.addMethod(mb.build());
        }
    }

    protected void generateDependencies(TypeSpec.Builder clientBuilder, RpcApiElement rpcApiElm) {
        // Fields
        FieldSpec.Builder fb = FieldSpec.builder(TypeName.get(RpcClient.class), RPC_CLIENT_FIELD, Modifier.PRIVATE, Modifier.FINAL);
        clientBuilder.addField(fb.build());

        // Constructor
        MethodSpec.Builder cb = MethodSpec.constructorBuilder();
        cb.addAnnotation(Inject.class);
        cb.addModifiers(Modifier.PUBLIC);
        cb.addParameter(TypeName.get(RpcClient.class), RPC_CLIENT_FIELD);

        cb.addStatement("this.$N = $N", RPC_CLIENT_FIELD, RPC_CLIENT_FIELD);
        clientBuilder.addMethod(cb.build());
    }

    public void generate(RpcApiElement rpcApiElm) {
        String classSimpleName = rpcApiElm.getClientClassSimpleName();
        String packageName = rpcApiElm.getOriginInterface().getPackageName();

        TypeSpec.Builder clientBuilder = TypeSpec.classBuilder(classSimpleName);
        clientBuilder.addAnnotation(Singleton.class);
        clientBuilder.addModifiers(Modifier.PUBLIC);
        clientBuilder.addSuperinterface(TypeName.get(rpcApiElm.getOriginInterface().unwrap().asType()));


        AnnotationSpec genstamp = CodegenUtils.generateGenstamp(this.getClass().getName(), null, "RPC interface: " + rpcApiElm.getOriginInterface().getName());
        clientBuilder.addAnnotation(genstamp);

        generateDependencies(clientBuilder, rpcApiElm);
        generateMethods(clientBuilder, rpcApiElm);

        final TypeSpec typeSpec = clientBuilder.build();
        CodegenUtils.createJavaFile(processingEnv, typeSpec, packageName, rpcApiElm.getOriginInterface().unwrap());

    }
}
