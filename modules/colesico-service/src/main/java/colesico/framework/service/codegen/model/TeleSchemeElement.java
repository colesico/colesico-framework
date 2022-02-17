package colesico.framework.service.codegen.model;

import colesico.framework.assist.StrUtils;
import colesico.framework.teleapi.TeleScheme;
import com.squareup.javapoet.CodeBlock;
import org.apache.commons.lang3.StringUtils;

/**
 * Tele-scheme element
 * @see TeleScheme
 */
public class TeleSchemeElement<S> {

    /**
     * Tele-facade for which the scheme is being built
     */
    private final TeleFacadeElement parentTeleFacade;

    /**
     * Scheme implementation type
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
     * Returns tele-facade scheme builder class simple name
     */
    public String getSchemeBuilderClassSimpleName() {
        String originClassName = parentTeleFacade.getParentService().getOriginClass().getSimpleName();
        String schemeTypeSuffix = StrUtils.firstCharToUpperCase(schemeType.getSimpleName());

        if (StringUtils.endsWith(originClassName, schemeTypeSuffix)) {
            return originClassName + TeleScheme.SCHEME_BUILDER_SUFFIX;
        } else {
            return originClassName + schemeTypeSuffix + TeleScheme.SCHEME_BUILDER_SUFFIX;
        }
    }

    public TeleFacadeElement getParentTeleFacade() {
        return parentTeleFacade;
    }

    public Class<S> getSchemeType() {
        return schemeType;
    }

    public Class<? extends TeleScheme<S>> getBaseClass() {
        return baseClass;
    }

    public CodeBlock getBuildMethodBody() {
        return buildMethodBody;
    }

    public void setBuildMethodBody(CodeBlock buildMethodBody) {
        this.buildMethodBody = buildMethodBody;
    }
}
