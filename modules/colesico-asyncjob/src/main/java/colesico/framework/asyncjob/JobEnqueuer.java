package colesico.framework.asyncjob;

import java.time.Duration;

public interface JobEnqueuer {
    <P> void enqueue(P jobPayload, Duration delay);
}
