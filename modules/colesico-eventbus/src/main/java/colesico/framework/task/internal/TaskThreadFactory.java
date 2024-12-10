package colesico.framework.task.internal;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class TaskThreadFactory implements ThreadFactory {

    private final String namePrefix;

    final AtomicLong count = new AtomicLong(0);

    public TaskThreadFactory(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName(namePrefix + "-" + count.getAndIncrement());
        thread.setDaemon(true);
        thread.setPriority(1);
        return thread;
    }

}
