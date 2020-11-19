package colesico.framework.rpc.codegen.parser;

import colesico.framework.assist.codegen.FrameworkAbstractParser;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.assist.codegen.model.ParameterElement;
import colesico.framework.rpc.codegen.model.RpcApiElement;
import colesico.framework.rpc.codegen.model.RpcApiMethodElement;
import colesico.framework.rpc.codegen.model.RpcApiParamElement;

import javax.annotation.processing.ProcessingEnvironment;
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
        RpcApiElement rpcApiElm = new RpcApiElement(originIface);

        List<MethodElement> methods = originIface.getMethods();
        for (MethodElement method : methods) {
            RpcApiMethodElement me = new RpcApiMethodElement(method);
            rpcApiElm.addMethod(me);
            parseParams(me);
        }

        return rpcApiElm;
    }
}
