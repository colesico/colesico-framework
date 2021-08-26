package colesico.framework.telescheme.codegen.model;

import colesico.framework.assist.StrUtils;
import colesico.framework.service.codegen.model.TeleFacadeElement;
import colesico.framework.telescheme.TeleScheme;
import com.squareup.javapoet.CodeBlock;
import org.apache.commons.lang3.StringUtils;

/**
 * Tele-facade scheme element
 */
public class TeleSchemeElement {

    /**
     * Tele-facade for which the scheme is being built
     */
    private final TeleFacadeElement parentFacade;

    /**
     * Tele-facade scheme-type id
     */
    private final Class<?> schemeType;

    private CodeBlock schemeCode;

    public TeleSchemeElement(TeleFacadeElement parentFacade, Class<?> schemeType) {
        this.parentFacade = parentFacade;
        this.schemeType = schemeType;
    }

    /**
     * Returns tele-facade scheme class simple name
     */
    public String getFacadeSchemeClassSimpleName() {
        String originClassName = parentFacade.getParentService().getOriginClass().getSimpleName();
        String schemeTypeSuffix = StrUtils.firstCharToUpperCase(schemeType.getSimpleName());

        if (StringUtils.endsWith(originClassName, schemeTypeSuffix)) {
            return originClassName + TeleScheme.TELE_SCHEME_SUFFIX;
        } else {
            return originClassName + schemeTypeSuffix + TeleScheme.TELE_SCHEME_SUFFIX;
        }
    }

    public TeleFacadeElement getParentFacade() {
        return parentFacade;
    }

    public Class<?> getSchemeType() {
        return schemeType;
    }

    public CodeBlock getSchemeCode() {
        return schemeCode;
    }

    public void setSchemeCode(CodeBlock schemeCode) {
        this.schemeCode = schemeCode;
    }
}
