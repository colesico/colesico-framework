package colesico.framework.rpc.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractParser;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.assist.codegen.model.ParameterElement;
import colesico.framework.rpc.RpcApi;
import colesico.framework.rpc.RpcMethod;
import colesico.framework.rpc.codegen.model.RpcApiElement;
import colesico.framework.rpc.codegen.model.RpcApiMethodElement;
import colesico.framework.rpc.codegen.model.RpcApiParamElement;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeKind;
import java.util.List;

public class RpcApiParser extends FrameworkAbstractParser {

    public RpcApiParser(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    protected void parseParams(RpcApiMethodElement method) {
        List<ParameterElement> params = method.getOriginMethod().getParameters();
        int ind = 0;
        for (ParameterElement param : params) {
            RpcApiParamElement p = new RpcApiParamElement(param, ind++);
            method.addParameter(p);
        }
    }

    public RpcApiElement parse(ClassElement originIface) {
        RpcApi rpcApi = originIface.unwrap().getAnnotation(RpcApi.class);
        String rpcApiNamespace = rpcApi.namespace();
        String rpcApiName = rpcApi.name();
        RpcApiElement rpcApiElm = new RpcApiElement(originIface, rpcApiNamespace, rpcApiName);

        List<MethodElement> methods = originIface.getMethods();
        for (MethodElement method : methods) {
            TypeKind retTypeKind = method.unwrap().getReturnType().getKind();
            if (!(retTypeKind == TypeKind.DECLARED || retTypeKind == TypeKind.ARRAY)) {
                throw CodegenException.of().message("Unsupported return type: " + method.unwrap().getReturnType() + ". Declared or array types support only")
                        .element(method.unwrap()).build();
            }
            RpcMethod rpcMethodAnn = method.unwrap().getAnnotation(RpcMethod.class);
            RpcApiMethodElement me = new RpcApiMethodElement(method, rpcMethodAnn == null ? null : rpcMethodAnn.name());
            rpcApiElm.addMethod(me);
            parseParams(me);
        }

        return rpcApiElm;
    }
}
