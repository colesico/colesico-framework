package colesico.framework.rpc.codegen.model;

import colesico.framework.service.codegen.model.ServiceElement;

import java.util.List;

public class RpcServiceElement {

    /**
     * Service element
     */
    private final ServiceElement parentService;

    /**
     * RPC API implemented by service
     */
    private final List<RpcApiElement> rpcApiList;

    public RpcServiceElement(ServiceElement parentService, List<RpcApiElement> rpcApiList) {
        this.parentService = parentService;
        this.rpcApiList = rpcApiList;
    }


    public ServiceElement getParentService() {
        return parentService;
    }

    public List<RpcApiElement> getAllRpcApi() {
        return rpcApiList;
    }
}
