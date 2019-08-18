package colesico.framework.jdbirec.mediators;

public class FloatListMediator extends ListMediator<Float> {

    @Override
    protected Float[] newArray(int size) {
        return new Float[size];
    }
}
