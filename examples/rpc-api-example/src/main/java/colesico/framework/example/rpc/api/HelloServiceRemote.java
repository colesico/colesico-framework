package colesico.framework.example.rpc.api;

import colesico.framework.rpc.RpcApi;
import colesico.framework.rpc.RpcName;

@RpcApi
public interface HelloServiceRemote {

    String HELLO_MESSAGE = "Hello";

    String getMessage(@RpcName("id") Integer id, DataBean dataBean, ComposedDataBean compound);

    @RpcName("getMsg")
    String getMessage2();
}
