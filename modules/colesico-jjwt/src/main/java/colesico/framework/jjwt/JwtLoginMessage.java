package colesico.framework.jjwt;

import java.util.Map;

public record JwtLoginMessage(String subject,
                              Map<String, Object> claims) implements JwtMessage {

}
