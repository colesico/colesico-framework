package colesico.framework.service.codegen.model.teleapi;

import java.util.ArrayList;
import java.util.List;

public class TeleBundlesPackElement {

    public static final String PACK_CLASS_SUFFIX = "ParamBundles";

    private final TeleServiceElement parentTeleFacade;

    private final List<TeleBundleElement> paramBundles = new ArrayList<>();

    public TeleBundlesPackElement(TeleServiceElement parentTeleFacade) {
        this.parentTeleFacade = parentTeleFacade;
    }

    public boolean isEmpty() {
        return paramBundles.isEmpty();
    }

    public void addParamBundle(TeleBundleElement paramBundle) {
        paramBundles.add(paramBundle);
        paramBundle.setParentPack(this);
    }

    public TeleServiceElement parentTeleFacade() {
        return parentTeleFacade;
    }

    public List<TeleBundleElement> paramBundles() {
        return paramBundles;
    }

    public String packClassSimpleName() {
        return parentTeleFacade.parentService().originClass().simpleName() + PACK_CLASS_SUFFIX;
    }
}
