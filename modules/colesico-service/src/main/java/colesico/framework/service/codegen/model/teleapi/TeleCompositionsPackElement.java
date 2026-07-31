package colesico.framework.service.codegen.model.teleapi;

import java.util.ArrayList;
import java.util.List;

public class TeleCompositionsPackElement {

    public static final String PACK_CLASS_SUFFIX = "ParamBeans";

    private final TeleServiceElement parentTeleFacade;

    private final List<TeleCompositionElement> compositions = new ArrayList<>();

    public TeleCompositionsPackElement(TeleServiceElement parentTeleFacade) {
        this.parentTeleFacade = parentTeleFacade;
    }

    public boolean isEmpty() {
        return compositions.isEmpty();
    }

    public void addComposition(TeleCompositionElement composition) {
        compositions.add(composition);
        composition.setParentPack(this);
    }

    public TeleServiceElement parentTeleFacade() {
        return parentTeleFacade;
    }

    public List<TeleCompositionElement> compositions() {
        return compositions;
    }

    public String packClassSimpleName() {
        return parentTeleFacade.parentService().originClass().simpleName() + PACK_CLASS_SUFFIX;
    }
}
