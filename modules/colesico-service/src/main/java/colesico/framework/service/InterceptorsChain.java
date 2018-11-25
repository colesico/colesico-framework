package colesico.framework.service;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 *
 */
public final class InterceptorsChain<T, R> {

    private Queue<Interception> queue = new ArrayDeque();

    public <P> void add(Interceptor<T, R> interceptor, P parameters) {
        queue.add(new Interception(interceptor, parameters));
    }

    public Interception next() {
        return queue.poll();
    }

    public static final class Interception<T, R, P> {
        private final Interceptor<T, R> interceptor;
        private final P parameters;

        public Interception(Interceptor<T, R> interceptor, P parameters) {
            this.interceptor = interceptor;
            this.parameters = parameters;
        }

        public Interceptor getInterceptor() {
            return interceptor;
        }

        public P getParameters() {
            return parameters;
        }
    }
}
