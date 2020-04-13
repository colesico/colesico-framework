package colesico.framework.example.rpc.api;

import colesico.framework.rpc.Remote;

@Remote
public interface HelloServiceRemote {

    String HELLO_MESSAGE = "Hello";

    String getMessage(Integer id, DataBean dataBean, ComposedDataBean compound);
}
