package colesico.framework.telehttp.origin;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpException;
import colesico.framework.telehttp.Origin;
import colesico.framework.telehttp.assist.TeleHttpUtils;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

@Singleton
public class BodyOrigin implements Origin {

    private final Provider<HttpContext> httpContextProv;

    public BodyOrigin(Provider<HttpContext> httpContextProv) {
        this.httpContextProv = httpContextProv;
    }

    @Override
    public Collection<String> getStrings(String name) {
        try (InputStream is = httpContextProv.get().request().inputStream()) {
            String content = TeleHttpUtils.inputStreamToString(is);
            return List.of(content);
        } catch (Exception e) {
            throw new HttpException(e, 500);
        }
    }
}
