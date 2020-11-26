package colesico.framework.rpc.internal.kryo;

import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;
import com.esotericsoftware.kryo.kryo5.util.Pool;

import javax.inject.Singleton;
import java.io.InputStream;
import java.io.OutputStream;

@Singleton
public class KryoSerializer {

    private Pool<Kryo> kryoPool = new Pool<>(true, false, 8) {
        protected Kryo create() {
            Kryo kryo = new Kryo();
            kryo.setRegistrationRequired(false);
            return kryo;
        }
    };

    public <T> void serialize(T obj, OutputStream os) {
        Kryo kryo = kryoPool.obtain();
        try (Output output = new Output(os)) {
            kryo.writeObject(output, obj);
        } finally {
            kryoPool.free(kryo);
        }
    }

    public <T> T deserialize(InputStream is, Class<T> type) {
        Kryo kryo = kryoPool.obtain();
        try (Input input = new Input(is)) {
            T res = kryo.readObject(input, type);
            return res;
        } finally {
            kryoPool.free(kryo);
        }
    }

}
