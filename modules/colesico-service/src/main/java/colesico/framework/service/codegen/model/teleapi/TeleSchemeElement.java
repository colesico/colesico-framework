package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.StringUtils;
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
    private final TeleServiceElement parentTeleService;

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

    public TeleSchemeElement(TeleServiceElement parentTeleService, Class<S> schemeType, Class<? extends TeleScheme<S>> baseClass) {
        this.parentTeleService = parentTeleService;
        this.schemeType = schemeType;
        this.baseClass = baseClass;
    }

    /**
     * Returns tele-scheme implementation class simple name
     */
    public String teleSchemeClassSimpleName() {
        String originClassName = parentTeleService.parentService().originClass().simpleName();
        String schemeTypeSuffix = StringUtils.firstCharToUpperCase(schemeType.getSimpleName());
        return originClassName + schemeTypeSuffix+TeleScheme.SCHEME_IMPL_SUFFIX;
    }

    public TeleServiceElement parentTeleService() {
        return parentTeleService;
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
