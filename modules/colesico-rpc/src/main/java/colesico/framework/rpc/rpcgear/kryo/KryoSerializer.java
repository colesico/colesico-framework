package colesico.framework.rpc.rpcgear.kryo;

import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;
import com.esotericsoftware.kryo.kryo5.util.Pool;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.InputStream;
import java.io.OutputStream;

@Singleton
public class KryoSerializer {

    protected final Logger logger = LoggerFactory.getLogger(KryoSerializer.class);

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
        } catch (Exception e) {
            logger.error("Serialization error: {}", ExceptionUtils.getRootCauseMessage(e));
            throw new RuntimeException(e);
        } finally {
            kryoPool.free(kryo);
        }
    }

    public <T> T deserialize(InputStream is, Class<T> type) {
        Kryo kryo = kryoPool.obtain();
        try (Input input = new Input(is)) {
            T res = kryo.readObject(input, type);
            return res;
        } catch (Exception e) {
            logger.error("Deserialization error: {}", ExceptionUtils.getRootCauseMessage(e));
            throw new RuntimeException(e);
        } finally {
            kryoPool.free(kryo);
        }
    }

}
