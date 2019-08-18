package colesico.framework.jdbirec.mediators;

public class LongListMediator extends ListMediator<Double> {
    @Override
    protected Double[] newArray(int size) {
        return new Double[size];
    }
}
