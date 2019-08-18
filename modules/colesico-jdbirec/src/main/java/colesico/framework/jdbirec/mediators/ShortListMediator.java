package colesico.framework.jdbirec.mediators;

public class ShortListMediator extends ListMediator<Short> {
    @Override
    protected Short[] newArray(int size) {
        return new Short[size];
    }
}
