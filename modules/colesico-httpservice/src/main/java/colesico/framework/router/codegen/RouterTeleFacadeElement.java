package colesico.framework.router.codegen;

import colesico.framework.service.codegen.model.teleapi.TeleFacadeElement;
import colesico.framework.teleapi.TeleFacade;
import colesico.framework.teleapi.dataport.ReadOptions;
import colesico.framework.teleapi.dataport.WriteOptions;

/**
 * Custom tele facade
 */
public final class RouterTeleFacadeElement extends TeleFacadeElement {

    private final RoutesBuilder routesBuilder;

    public RouterTeleFacadeElement(Class<?> teleType,
                                   Class<? extends TeleFacade.Commands> commandsClass,
                                   Class<? extends ReadOptions> readOptionsClass,
                                   Class<? extends WriteOptions> writeOptionsClass,
                                   IocQualifier iocQualifier,
                                   RoutesBuilder routesBuilder) {
        super(teleType, commandsClass, readOptionsClass, writeOptionsClass, iocQualifier);
        this.routesBuilder = routesBuilder;
    }

    public RoutesBuilder routesBuilder() {
        return routesBuilder;
    }
}

