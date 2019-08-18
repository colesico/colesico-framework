package colesico.framework.jdbirec.mediators;

public class StringListMediator extends ListMediator<String> {
    @Override
    protected String[] newArray(int size) {
        return new String[size];
    }
}
