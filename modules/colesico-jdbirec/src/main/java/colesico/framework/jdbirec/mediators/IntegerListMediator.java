package colesico.framework.jdbirec.mediators;

public class IntegerListMediator extends ListMediator<Integer> {

    @Override
    protected Integer[] newArray(int size) {
        return new Integer[size];
    }
}
