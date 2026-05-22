package colesico.framework.telehttp.origin;

import colesico.framework.http.HttpRequest;
import colesico.framework.http.MultiValue;
import colesico.framework.telehttp.Origin;

import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
public class HeaderOrigin implements Origin {

    @Override
    public Collection<String> getStrings(String name, Context context) {
        final List<String> result = new ArrayList<>();
        MultiValue<String> headers = context.httpRequest().headers().getAll(name);
        if (headers != null) {
            headers.iterator().forEachRemaining(result::add);
        }
        return result;
    }
}
