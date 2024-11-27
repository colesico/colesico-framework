package colesico.framework.jdbirec.codegen.model;

import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.jdbirec.Record;

public class RecordElement extends ContainerElement{

    /**
     * @see Record#view()
     */
    private final String view;

    public RecordElement(ClassType originType, RecordKitElement parentRecordKit, String view) {
        super(originType, parentRecordKit);
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
