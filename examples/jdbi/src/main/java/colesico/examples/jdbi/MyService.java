package colesico.examples.jdbi;

import colesico.framework.service.Service;
import colesico.framework.transaction.Transactional;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.statement.Query;

import javax.inject.Inject;
import javax.inject.Provider;

@Service
public class MyService {

    private final Provider<Handle> handleProv;

    @Inject
    public MyService(Provider<Handle> handleProv) {
        this.handleProv = handleProv;
    }

    @Transactional
    public String readValue(Integer key) {

        Handle handle = handleProv.get();
        String val = handle.createQuery("select avalue from avalues where akey=:key")
                .bind("key", key)
                .mapTo(String.class)
                .findOnly();

        return val;
    }
}
