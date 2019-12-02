package colesico.framework.example.slf4j;

import colesico.framework.ioc.Contextual;
import colesico.framework.service.Service;
import org.slf4j.Logger;

/**
 * Logger will be injected to this service bean
 */
@Service
public class MainBean {

    private final Logger logger;

    /**
     * Contextual annotation is used to pass injection point information to logger factory
     * to produce specific logger
     * @param logger
     */
    public MainBean(@Contextual Logger logger) {
        this.logger = logger;
    }

    public void logMessage(String message) {
        logger.info(message);
    }
}
