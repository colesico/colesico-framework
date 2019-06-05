package colesico.framework.asyncjob.internal;

/**
 * Consistently distributes "non captured" queues on workers for processing
 */
public final class QueuePool {

    private final QueueRef queueRefs[];
    private final int queuesNum;
    private final int maxPointer;
    private volatile int pointer = 0;

    public QueuePool(QueueRef[] queueRefs) {
        this.queueRefs = queueRefs;
        this.queuesNum = queueRefs.length;
        this.maxPointer = queueRefs.length - 1;
    }

    private QueueRef nextRef() {
        QueueRef queueRef = queueRefs[pointer];
        if (pointer < maxPointer) {
            pointer++;
        } else {
            pointer = 0;
        }

        return queueRef;
    }

    /**
     * Return next non captured queue reference.
     *
     * @param duration capture interval in milliseconds
     * @return non captured queue ref if exists, null otherwise
     */
    public synchronized QueueRef capture(long duration) {
        final long curTime = System.currentTimeMillis();
        for (int i = 0; i < queuesNum; i++) {
            QueueRef queueRef = nextRef();
            if (queueRef.getCapturedTill() < curTime) {
                queueRef.setCapturedTill(curTime + duration);
                return queueRef;
            }
        }
        return null;
    }

    /**
     * Release captured queue ref.
     *
     * @param queueRef
     */
    public synchronized void release(QueueRef queueRef) {
        queueRef.setCapturedTill(0);
    }
}
