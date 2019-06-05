package colesico.framework.asyncjob;

import java.lang.reflect.Type;

public interface PayloadConverter<P> {

    P toObject(Type payloadType, String payloadStr);

    String fromObject(P payload);
}
