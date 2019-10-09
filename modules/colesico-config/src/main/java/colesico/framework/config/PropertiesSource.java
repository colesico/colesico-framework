package colesico.framework.config;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

@Singleton
public class PropertiesSource implements ConfigSourceDriver {

    @Override
    public Connection connect(String uri) {
        //TODO
        return null;
    }

    @PostConstruct
    public void init() {

    }

}
