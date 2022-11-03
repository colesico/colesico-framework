package colesico.framework.example.rpc.api;

import colesico.framework.rpc.RpcApi;
import colesico.framework.rpc.RpcMethod;

@RpcApi(namespace = "myrpc", rpcName = "hc")
public interface HelloServiceRemote {

    String HELLO_MESSAGE = "Hello";

    String getMessage(Integer id, DataBean dataBean, NameBean nameBean);

    @RpcMethod(rpcName = "getDB")
    DataBean getDataBean();

}
