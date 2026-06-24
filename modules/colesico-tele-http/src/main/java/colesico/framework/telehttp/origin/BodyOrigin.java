package colesico.framework.telehttp.origin;

import colesico.framework.http.HttpException;
import colesico.framework.http.HttpRequest;
import colesico.framework.telehttp.assist.HttpTeleUtils;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

@Singleton
public class BodyOrigin implements Origin {

    protected final Provider<HttpRequest> httpRequest;

    public BodyOrigin(Provider<HttpRequest> httpRequest) {
        this.httpRequest = httpRequest;
    }

    @Override
    public Collection<String> getStrings(String name) {
        try (InputStream is = httpRequest.get().inputStream()) {
            String content = HttpTeleUtils.inputStreamToString(is);
            return List.of(content);
        } catch (Exception e) {
            throw new HttpException(e, 500);
        }
    }

}
