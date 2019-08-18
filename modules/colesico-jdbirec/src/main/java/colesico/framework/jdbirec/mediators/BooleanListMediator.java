package colesico.framework.jdbirec.mediators;

public class BooleanListMediator extends ListMediator<Boolean> {
    @Override
    protected Boolean[] newArray(int size) {
        return new Boolean[size];
    }
}
