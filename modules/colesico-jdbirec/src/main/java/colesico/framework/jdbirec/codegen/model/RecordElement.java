package colesico.framework.jdbirec.codegen.model;

import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.jdbirec.Record;

public class RecordElement extends ContainerElement{

    /**
     * @see Record#view()
     */
    private final String view;

    public RecordElement( RecordKitElement recordKit, ClassType type,String view) {
        super(recordKit,type);
        this.view = view;
    }

    public String getView() {
        return view;
    }

    @Override
    public String toString() {
        return "RecordElement{" +
                "originType=" + type +
                ", view='" + view + '\'' +
                '}';
    }
}
