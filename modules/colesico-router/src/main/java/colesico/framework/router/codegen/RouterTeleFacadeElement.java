package colesico.framework.router.codegen;

import colesico.framework.service.codegen.model.TeleFacadeElement;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.TeleDriver;

/**
 * Custom tele facade
 */
public final class RouterTeleFacadeElement extends TeleFacadeElement {

    private final RoutesBuilder routesBuilder;

    public RouterTeleFacadeElement(Class<?> teleType,
                                   Class<? extends TeleDriver> teleDriverClass,
                                   Class<? extends DataPort> dataPortClass,
                                   Class<?> ligatureClass,
                                   IocQualifier iocQualifier,
                                   RoutesBuilder routesBuilder) {

        super(teleType, teleDriverClass, dataPortClass, ligatureClass, iocQualifier);
        this.routesBuilder = routesBuilder;
        // Enable compound support
        this.setCompoundSupport(true);
    }

    public RoutesBuilder getRoutesBuilder() {
        return routesBuilder;
    }
}

