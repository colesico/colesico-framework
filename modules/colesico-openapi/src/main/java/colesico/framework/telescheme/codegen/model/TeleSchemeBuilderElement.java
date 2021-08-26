package colesico.framework.telescheme.codegen.model;

import colesico.framework.assist.StrUtils;
import colesico.framework.service.codegen.model.TeleFacadeElement;
import colesico.framework.telescheme.TeleSchemeBuilder;
import com.squareup.javapoet.CodeBlock;
import org.apache.commons.lang3.StringUtils;

/**
 * Tele-facade scheme builder
 */
public class TeleSchemeBuilderElement {

    /**
     * Tele-facade for which the scheme is being built
     */
    private final TeleFacadeElement parentTeleFacade;

    /**
     * Tele-facade scheme-type
     */
    private final Class<?> schemeType;

    private final Class<? extends TeleSchemeBuilder> builderBaseClass;

    private CodeBlock buildCode;

    public TeleSchemeBuilderElement(TeleFacadeElement parentTeleFacade, Class<?> schemeType, Class<? extends TeleSchemeBuilder> builderBaseClass) {
        this.parentTeleFacade = parentTeleFacade;
        this.schemeType = schemeType;
        this.builderBaseClass = builderBaseClass;
    }

    /**
     * Returns tele-facade scheme class simple name
     */
    public String getFacadeSchemeClassSimpleName() {
        String originClassName = parentTeleFacade.getParentService().getOriginClass().getSimpleName();
        String schemeTypeSuffix = StrUtils.firstCharToUpperCase(schemeType.getSimpleName());

        if (StringUtils.endsWith(originClassName, schemeTypeSuffix)) {
            return originClassName + TeleSchemeBuilder.SCHEME_BUILDER_SUFFIX;
        } else {
            return originClassName + schemeTypeSuffix + TeleSchemeBuilder.SCHEME_BUILDER_SUFFIX;
        }
    }

    public TeleFacadeElement getParentTeleFacade() {
        return parentTeleFacade;
    }

    public Class<?> getSchemeType() {
        return schemeType;
    }

    public CodeBlock getBuildCode() {
        return buildCode;
    }

    public void setBuildCode(CodeBlock buildCode) {
        this.buildCode = buildCode;
    }

    public Class<? extends TeleSchemeBuilder> getBuilderBaseClass() {
        return builderBaseClass;
    }
}
