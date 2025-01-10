package colesico.framework.translation.internal;

import colesico.framework.translation.ResourceBundleControlFactory;

import javax.inject.Singleton;
import java.util.ResourceBundle;

@Singleton
public class ResourceBundleControlFactoryImpl implements ResourceBundleControlFactory {

    private final Control control = new Control();

    @Override
    public ResourceBundle.Control getControl() {
        return control;
    }

    public static class Control extends ResourceBundle.Control {

    }
}
