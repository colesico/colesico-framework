package colesico.framework.service.codegen.model.teleapi;

import java.util.ArrayList;
import java.util.List;

public class TeleBeansPackElement {

    public static final String PACK_CLASS_SUFFIX = "ParamBeans";

    private final TeleServiceElement parentTeleFacade;

    private final List<TeleBeanElement> paramBeans = new ArrayList<>();

    public TeleBeansPackElement(TeleServiceElement parentTeleFacade) {
        this.parentTeleFacade = parentTeleFacade;
    }

    public boolean isEmpty() {
        return paramBeans.isEmpty();
    }

    public void addParamBean(TeleBeanElement paramBean) {
        paramBeans.add(paramBean);
        paramBean.setParentPack(this);
    }

    public TeleServiceElement parentTeleFacade() {
        return parentTeleFacade;
    }

    public List<TeleBeanElement> paramBeans() {
        return paramBeans;
    }

    public String packClassSimpleName() {
        return parentTeleFacade.parentService().originClass().simpleName() + PACK_CLASS_SUFFIX;
    }
}
