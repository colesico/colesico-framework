package colesico.framework.ioc.codegen.model;

import colesico.framework.assist.codegen.model.ClassType;

/**
 * Post produce listener element
 * @see colesico.framework.ioc.PostProduce
 */
public class PPLDefinitionElement {

    private final String withNamed;

    private final ClassType withClassed;

    public PPLDefinitionElement(String withNamed, ClassType withClassed) {
        this.withNamed = withNamed;
        this.withClassed = withClassed;
    }

    public String getWithNamed() {
        return withNamed;
    }

    public ClassType getWithClassed() {
        return withClassed;
    }
}
