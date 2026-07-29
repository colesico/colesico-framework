package colesico.framework.service.codegen.model.teleapi;

import java.util.ArrayList;
import java.util.List;

public class TeleRequestBeanPackElement {

    public static final String PACK_CLASS_SUFFIX = "RequestBeans";

    private final TeleServiceElement parentTeleFacade;

    private final List<TeleRequestBeanElement> requestBeans = new ArrayList<>();

    public TeleRequestBeanPackElement(TeleServiceElement parentTeleFacade) {
        this.parentTeleFacade = parentTeleFacade;
    }

    public boolean isEmpty() {
        return requestBeans.isEmpty();
    }

    public void addRequestBean(TeleRequestBeanElement batch) {
        requestBeans.add(batch);
        batch.setParentPack(this);
    }

    public TeleServiceElement parentTeleFacade() {
        return parentTeleFacade;
    }

    public List<TeleRequestBeanElement> requestBeans() {
        return requestBeans;
    }

    public String packClassSimpleName() {
        return parentTeleFacade.parentService().originClass().simpleName() + PACK_CLASS_SUFFIX;
    }
}
