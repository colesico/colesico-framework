package colesico.framework.rpc.internal;

import colesico.framework.rpc.teleapi.RpcSerializer;
import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;
import com.esotericsoftware.kryo.kryo5.util.Pool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Base64;

public class KryoSerializer implements RpcSerializer {

    Pool<Kryo> kryoPool = new Pool<Kryo>(true, false, 8) {
        protected Kryo create() {
            Kryo kryo = new Kryo();
            // Configure the Kryo instance.
            return kryo;
        }
    };

    @Override
    public <T> String serialize(T obj) {
        Kryo kryo = kryoPool.obtain();
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (Output output = new Output(bos)) {
                kryo.writeClassAndObject(output, obj);
                byte[] objBytes = bos.toByteArray();
                Base64.Encoder encoder = Base64.getEncoder();
                return encoder.encodeToString(objBytes);
            }
        } finally {
            kryoPool.free(kryo);
        }
    }

    @Override
    public <T> T deserialize(Reader reader, Type valueType) {
        Kryo kryo = kryoPool.obtain();
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] objBytes = decoder.decode(readToString(reader));
            ByteArrayInputStream bis = new ByteArrayInputStream(objBytes);

            try (Input input = new Input(bis)) {
                Object res = kryo.readClassAndObject(input);
                return (T) res;
            }
        } finally {
            kryoPool.free(kryo);
        }
    }

    protected String readToString(Reader reader) {
        try {
            char[] arr = new char[8 * 1024];
            StringBuilder buffer = new StringBuilder();
            int numCharsRead;
            while ((numCharsRead = reader.read(arr, 0, arr.length)) != -1) {
                buffer.append(arr, 0, numCharsRead);
            }
            reader.close();
            return buffer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
