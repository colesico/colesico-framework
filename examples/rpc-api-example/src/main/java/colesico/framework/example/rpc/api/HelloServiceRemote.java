package colesico.framework.example.rpc.api;

import colesico.framework.rpc.RpcApi;
import colesico.framework.rpc.RpcName;

@RpcApi
public interface HelloServiceRemote {

    String HELLO_MESSAGE = "Hello";

    String getMessage(Integer id, DataBean dataBean, NameBean nameBean);

    @RpcName("getDB")
    DataBean getDataBean();

}
