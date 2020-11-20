package colesico.framework.router.codegen;

import colesico.framework.service.codegen.model.TeleFacadeElement;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.TeleDriver;

public class RouterTeleFacadeElement extends TeleFacadeElement {

    private final RoutegenContext context;

    public RouterTeleFacadeElement(String teleType,
                                   Class<? extends TeleDriver> teleDriverClass,
                                   Class<? extends DataPort> dataPortClass,
                                   Class<?> ligatureClass,
                                   IocQualifier iocQualifier,
                                   RoutegenContext context) {

        super(teleType, teleDriverClass, dataPortClass, ligatureClass, iocQualifier);
        this.context = context;
    }

    public RoutegenContext getContext() {
        return context;
    }
}

