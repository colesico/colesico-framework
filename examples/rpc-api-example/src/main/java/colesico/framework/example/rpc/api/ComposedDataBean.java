package colesico.framework.example.rpc.api;


import colesico.framework.rpc.RpcName;
import colesico.framework.service.Compound;

public class ComposedDataBean {
    private String name;

    @Compound
    @RpcName("bean")
    private DataBean data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }
}
