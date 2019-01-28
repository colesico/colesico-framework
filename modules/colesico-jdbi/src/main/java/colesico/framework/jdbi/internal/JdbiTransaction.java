package colesico.framework.jdbi.internal;

import colesico.framework.transaction.Tuning;
import org.jdbi.v3.core.Handle;

public class JdbiTransaction {

    private Handle handle;
    private Tuning<Handle> tuning;

    public Handle getHandle() {
        return handle;
    }

    public JdbiTransaction setHandle(Handle handle) {
        this.handle = handle;
        return this;
    }

    public Tuning<Handle> getTuning() {
        return tuning;
    }

    public JdbiTransaction setTuning(Tuning<Handle> tuning) {
        this.tuning = tuning;
        return this;
    }
}
