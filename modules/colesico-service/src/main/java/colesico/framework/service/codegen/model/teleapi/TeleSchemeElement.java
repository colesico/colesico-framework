package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.StrUtils;
import com.palantir.javapoet.CodeBlock;
import colesico.framework.teleapi.TeleScheme;

/**
 * Tele-scheme element
 *
 * @see TeleScheme
 */
public class TeleSchemeElement<S> {

    /**
     * Tele-facade for which the scheme is being built
     */
    private final TeleFacadeElement parentTeleFacade;

    /**
     * Scheme implementation type
     *
     * @see TeleScheme
     */
    private final Class<S> schemeType;

    /**
     * Tele - scheme base class
     */
    private final Class<? extends TeleScheme<S>> baseClass;

    /**
     * {@link TeleScheme#build()} method code
     */
    private CodeBlock buildMethodBody;

    public TeleSchemeElement(TeleFacadeElement parentTeleFacade, Class<S> schemeType, Class<? extends TeleScheme<S>> baseClass) {
        this.parentTeleFacade = parentTeleFacade;
        this.schemeType = schemeType;
        this.baseClass = baseClass;
    }

    /**
     * Returns tele-scheme implementation class simple name
     */
    public String teleSchemeClassSimpleName() {
        String originClassName = parentTeleFacade.parentService().originClass().simpleName();
        String schemeTypeSuffix = StrUtils.firstCharToUpperCase(schemeType.getSimpleName());
        return originClassName + schemeTypeSuffix+TeleScheme.SCHEME_IMPL_SUFFIX;
    }

    public TeleFacadeElement parentTeleFacade() {
        return parentTeleFacade;
    }

    public Class<S> schemeType() {
        return schemeType;
    }

    public Class<? extends TeleScheme<S>> baseClass() {
        return baseClass;
    }

    public CodeBlock buildMethodBody() {
        return buildMethodBody;
    }

    public void setBuildMethodBody(CodeBlock buildMethodBody) {
        this.buildMethodBody = buildMethodBody;
    }
}
