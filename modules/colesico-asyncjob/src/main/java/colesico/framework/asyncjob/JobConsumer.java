package colesico.framework.asyncjob;


@FunctionalInterface
public interface JobConsumer<P> {
    void consume(P jobPayload);
}
