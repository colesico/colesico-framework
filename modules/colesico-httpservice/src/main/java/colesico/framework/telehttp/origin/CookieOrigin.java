package colesico.framework.telehttp.origin;

import colesico.framework.http.HttpCookie;
import colesico.framework.http.MultiValue;
import colesico.framework.telehttp.HttpTeleReader;
import colesico.framework.telehttp.Origin;

import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
public class CookieOrigin implements Origin {

    @Override
    public Collection<String> getStrings(String name,  HttpTeleReader.Channel channel) {
        final List<String> result = new ArrayList<>();
        MultiValue<HttpCookie> cookies = channel.httpRequest().cookies().getAll(name);
        if (cookies != null) {
            cookies.iterator().forEachRemaining(c -> result.add(c.setValue()));
        }
        return result;
    }
}
