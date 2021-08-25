package colesico.framework.telescheme;

abstract public class MethodScheme<C,P,R> {

    abstract public C commonScheme();
    abstract public P[] paramsScheme();
    abstract public R resultScheme();

}
