package colesico.framework.security;

public interface ActionPermitter<C> {
    void apply(C resource);
}
