package colesico.framework.router.codegen;

import colesico.framework.service.codegen.model.teleapi.TeleFacadeElement;
import colesico.framework.teleapi.TeleMethod;
import colesico.framework.teleapi.dataport.DataPort;
import colesico.framework.teleapi.TeleController;

/**
 * Custom tele facade
 */
public final class RouterTeleFacadeElement extends TeleFacadeElement {

    private final RoutesBuilder routesBuilder;

    public RouterTeleFacadeElement(Class<?> teleType,
                                   Class<?> descriptorsClass,
                                   Class<? extends TeleMethod<?,?>> teleMethodClass,
                                   IocQualifier iocQualifier,
                                   RoutesBuilder routesBuilder) {

        super(teleType, teleMethodClass, descriptorsClass, iocQualifier);
        this.routesBuilder = routesBuilder;
    }

    public RoutesBuilder getRoutesBuilder() {
        return routesBuilder;
    }
}

