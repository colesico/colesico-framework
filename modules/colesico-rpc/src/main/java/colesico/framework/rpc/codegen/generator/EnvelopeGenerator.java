package colesico.framework.rpc.codegen.generator;

import colesico.framework.assist.StrUtils;
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
import java.lang.reflect.Method;
import java.util.List;

public class EnvelopeGenerator extends FrameworkAbstractGenerator {

    private final EnvelopeExtensionKit extKit;

    public EnvelopeGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
        this.extKit = new EnvelopeExtensionKit();
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

    private void generateEnvelopeExtensions(TypeSpec.Builder requestBuilder, List<Class<?>> extensions) {
        for (Class<?> extension : extensions) {
            requestBuilder.addSuperinterface(ClassName.get(extension));
            for (Method method : extension.getDeclaredMethods()) {
                if (method.getName().startsWith("get") && method.getParameterTypes().length == 0) {
                    String fieldName = StrUtils.firstCharToLowerCase(method.getName().substring(3));
                    FieldSpec.Builder fb = FieldSpec.builder(TypeName.get(method.getReturnType()), fieldName, Modifier.PRIVATE);
                    fb.addJavadoc("Envelope extension " + extension.getCanonicalName());
                    requestBuilder.addField(fb.build());

                    MethodSpec.Builder gb = MethodSpec.methodBuilder(method.getName());
                    gb.addAnnotation(Override.class);
                    gb.addModifiers(Modifier.PUBLIC);
                    gb.returns(TypeName.get(method.getReturnType()));
                    gb.addStatement("return $N", fieldName);
                    requestBuilder.addMethod(gb.build());
                    continue;
                }

                if (method.getName().startsWith("set")
                        && method.getParameterTypes().length == 1
                        && method.getReturnType() == void.class) {
                    String fieldName = StrUtils.firstCharToLowerCase(method.getName().substring(3));
                    MethodSpec.Builder sb = MethodSpec.methodBuilder(method.getName());
                    sb.addAnnotation(Override.class);
                    sb.addModifiers(Modifier.PUBLIC);
                    sb.returns(TypeName.VOID);
                    sb.addParameter(TypeName.get(method.getParameterTypes()[0]), fieldName);
                    sb.addStatement("this.$N = $N", fieldName, fieldName);
                    requestBuilder.addMethod(sb.build());
                }
            }
        }
    }

    private void generateRequestEnvelope(TypeSpec.Builder envelopeBuilder, RpcApiMethodElement method) {
        TypeSpec.Builder requestBuilder = TypeSpec.classBuilder(method.getRequestClassSimpleName());
        requestBuilder.addJavadoc("RPC request for method " + method.getParentApi().getOriginInterface().getName() +
                "->" + method.getOriginMethod().getName());
        requestBuilder.addModifiers(Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC);
        requestBuilder.superclass(ClassName.get(RpcRequest.class));
        generateParams(requestBuilder, method);

        generateEnvelopeExtensions(requestBuilder, extKit.getRequestExtensions());

        envelopeBuilder.addType(requestBuilder.build());
    }

    private void generateResponseEnvelope(TypeSpec.Builder envelopeBuilder, RpcApiMethodElement method) {
        TypeSpec.Builder responseBuilder = TypeSpec.classBuilder(method.getResponseClassSimpleName());
        responseBuilder.addModifiers(Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC);

        ParameterizedTypeName responseType = ParameterizedTypeName.get(ClassName.get(RpcResponse.class),
                TypeName.get(method.getOriginMethod().getReturnType()));
        responseBuilder.superclass(responseType);

        generateEnvelopeExtensions(responseBuilder, extKit.getRequestExtensions());

        envelopeBuilder.addType(responseBuilder.build());
    }

    private void generateMethodsSchemas(TypeSpec.Builder envelopeBuilder, RpcApiElement rpcApiElm) {
        for (RpcApiMethodElement method : rpcApiElm.getRpcMethods()) {
            generateRequestEnvelope(envelopeBuilder, method);
            generateResponseEnvelope(envelopeBuilder, method);
        }
    }

    public void generate(RpcApiElement rpcApiElm) {
        String classSimpleName = rpcApiElm.getEnvelopePackClassSimpleName();
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
