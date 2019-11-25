package colesico.framework.example.ioc.lifecycle;

import colesico.framework.ioc.PostConstruct;

public class MainBeanLFC {

    private String value;

    @PostConstruct
    public void init(){
        value = value + "Suffix";
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
