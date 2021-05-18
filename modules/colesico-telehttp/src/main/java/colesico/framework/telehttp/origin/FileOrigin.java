package colesico.framework.telehttp.origin;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpFile;
import colesico.framework.telehttp.Origin;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class FileOrigin implements Origin<String, HttpFile> {

    private final Provider<HttpContext> httpContextProv;

    public FileOrigin(Provider<HttpContext> httpContextProv) {
        this.httpContextProv = httpContextProv;
    }

    @Override
    public HttpFile getValue(String key) {
        return httpContextProv.get().getRequest().getPostFiles().get(key);
    }
}
