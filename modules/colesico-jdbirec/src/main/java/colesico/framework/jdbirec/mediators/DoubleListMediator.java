package colesico.framework.jdbirec.mediators;

public class DoubleListMediator extends ListMediator<Double> {

    @Override
    protected Double[] newArray(int size) {
        return new Double[size];
    }
}
