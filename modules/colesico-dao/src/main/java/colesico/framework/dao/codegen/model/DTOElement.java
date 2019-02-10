package colesico.framework.dao.codegen.model;

import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;

public class DTOElement {

    private final TypeElement originClass;
    private final CompositionElement rootComposition;

    public DTOElement(TypeElement originClass) {
        this.originClass = originClass;
        this.rootComposition = new CompositionElement(this, originClass, null);
    }

    public CompositionElement getRootComposition() {
        return rootComposition;
    }

    public TypeElement getOriginClass() {
        return originClass;
    }

    public boolean hasColumn(ColumnElement columnElement) {
        return rootComposition.hasColumn(columnElement);
    }

    public List<ColumnElement> getAllColumns(){
        List<ColumnElement> result = new ArrayList<>();
        rootComposition.collectSubColumns(result);
        return result;
    }

}
