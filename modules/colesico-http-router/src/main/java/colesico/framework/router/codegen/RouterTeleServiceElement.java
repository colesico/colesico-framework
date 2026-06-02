package colesico.framework.router.codegen;

import colesico.framework.service.codegen.model.teleapi.TeleServiceElement;
import colesico.framework.teleapi.TeleFacade;
import colesico.framework.teleapi.dataport.ReadOptions;
import colesico.framework.teleapi.dataport.WriteOptions;

/**
 * Custom tele facade
 */
public final class RouterTeleServiceElement extends TeleServiceElement {

    private final RoutesBuilder routesBuilder;

    public RouterTeleServiceElement(Class<?> teleType,
                                    Class<? extends TeleFacade.CommandsRegistry> commandsClass,
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

