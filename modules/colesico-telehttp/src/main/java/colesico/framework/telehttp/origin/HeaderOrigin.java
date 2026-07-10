package colesico.framework.telehttp.origin;

import colesico.framework.http.HttpRequest;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
public class HeaderOrigin implements Origin {

    protected final Provider<HttpRequest> httpRequest;

    public HeaderOrigin(Provider<HttpRequest> httpRequest) {
        this.httpRequest = httpRequest;
    }

    @Override
    public Collection<String> getStrings(String name) {
        final List<String> result = new ArrayList<>();
        var headers = httpRequest.get().headers().getAll(name);
        if (headers != null) {
            headers.iterator().forEachRemaining(result::add);
        }
        return result;
    }
}
