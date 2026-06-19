package colesico.framework.weblet.teleapi.writer;

import colesico.framework.weblet.WebletException;
import colesico.framework.weblet.teleapi.WebletTeleWriter;
import colesico.framework.weblet.teleapi.WebletWriteOptions;
import jakarta.inject.Singleton;

@Singleton
public class WebletExceptionWriter implements WebletTeleWriter<WebletException> {

    @Override
    public void write(WebletException value, Class<WebletException> valueType, WebletWriteOptions options) {
        throw new UnsupportedOperationException("Not implemented");
    }
    
}
