package colesico.framework.example.rpc.api;

public final class DataBean {
    public String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DataBean() {
    }

    public DataBean(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
