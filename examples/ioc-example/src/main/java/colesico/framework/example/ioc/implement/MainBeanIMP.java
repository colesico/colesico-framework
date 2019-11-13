package colesico.framework.example.ioc.implement;

public class MainBeanIMP {
    private final BeanInterface bean;

    public MainBeanIMP(BeanInterface bean) {
        this.bean = bean;
    }

    public String getValue(){
        return bean.getValue();
    }
}
