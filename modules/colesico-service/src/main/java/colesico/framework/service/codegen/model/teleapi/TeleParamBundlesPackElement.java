package colesico.framework.service.codegen.model.teleapi;

import java.util.ArrayList;
import java.util.List;

public class TeleParamBundlesPackElement {

    public static final String PACK_CLASS_SUFFIX = "ParamBundles";

    private final TeleServiceElement parentTeleFacade;

    private final List<TeleParamBundleElement> paramBundles = new ArrayList<>();

    public TeleParamBundlesPackElement(TeleServiceElement parentTeleFacade) {
        this.parentTeleFacade = parentTeleFacade;
    }

    public boolean isEmpty() {
        return paramBundles.isEmpty();
    }

    public void addParamBundle(TeleParamBundleElement paramBundle) {
        paramBundles.add(paramBundle);
        paramBundle.setParentPack(this);
    }

    public TeleServiceElement parentTeleFacade() {
        return parentTeleFacade;
    }

    public List<TeleParamBundleElement> paramBundles() {
        return paramBundles;
    }

    public String packClassSimpleName() {
        return parentTeleFacade.parentService().originClass().simpleName() + PACK_CLASS_SUFFIX;
    }
}
