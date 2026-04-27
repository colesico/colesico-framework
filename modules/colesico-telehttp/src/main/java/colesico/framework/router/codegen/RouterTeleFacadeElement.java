package colesico.framework.router.codegen;

import colesico.framework.service.codegen.model.teleapi.TeleFacadeElement;
import colesico.framework.teleapi.dataport.TRContext;
import colesico.framework.teleapi.dataport.TWContext;

/**
 * Custom tele facade
 */
public final class RouterTeleFacadeElement extends TeleFacadeElement {

    private final RoutesBuilder routesBuilder;

    public RouterTeleFacadeElement(Class<?> teleType,
                                   Class<?> commandsClass,
                                   Class<? extends TRContext> readContextClass,
                                   Class<? extends TWContext> writeContextClass,
                                   IocQualifier iocQualifier,
                                   RoutesBuilder routesBuilder) {
        super(teleType, commandsClass, readContextClass, writeContextClass, iocQualifier);
        this.routesBuilder = routesBuilder;
    }

    public RoutesBuilder routesBuilder() {
        return routesBuilder;
    }
}

