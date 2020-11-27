package colesico.framework.example.rpc.api;


public final class NameBean {

    private String name;

    private DataBean data;

    public NameBean() {
    }

    public NameBean(String name, DataBean data) {
        this.name = name;
        this.data = data;
    }

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

    @Override
    public String toString() {
        return name + "-" + data.toString();
    }
}
