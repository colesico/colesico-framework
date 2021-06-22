package colesico.framework.telehttp.origin;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpException;
import colesico.framework.telehttp.Origin;
import colesico.framework.telehttp.assist.TeleHttpUtils;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.InputStream;

@Singleton
public class BodyOrigin implements Origin {

    private final Provider<HttpContext> httpContextProv;

    public BodyOrigin(Provider<HttpContext> httpContextProv) {
        this.httpContextProv = httpContextProv;
    }

    @Override
    public String getString(String name) {
        try (InputStream is = httpContextProv.get().getRequest().getInputStream()) {
            String content = TeleHttpUtils.inputStreamToString(is);
            return content;
        } catch (Exception e) {
            throw new HttpException(e, 500);
        }
    }
}
