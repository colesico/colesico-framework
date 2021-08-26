package colesico.framework.telescheme;

abstract public class MethodScheme<H,P,R> {

    abstract public H headerScheme();
    abstract public P[] paramsScheme();
    abstract public R resultScheme();

}
