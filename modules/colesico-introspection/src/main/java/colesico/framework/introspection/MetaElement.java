package colesico.framework.introspection;

/**
 * Introspection basic element
 */
public interface MetaElement {

    /**
     * Element kind
     */
    Kind getKind();

    /**
     * Container directly containing this element or null
     */
    MetaElement getContainer();

    /**
     * Element name or null
     * This can be a class simple name, method name, field name, parameter name
     */
    String getName();

    /**
     * Modifiers of this element or null
     */
    Modifier[] getModifiers();

    /**
     * Annotations that are (directly or indirectly) declared on this element or null
     */
    MetaAnnotation[] getAnnotations();

}

