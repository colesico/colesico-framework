package colesico.framework.telehttp.origin;

import colesico.framework.http.MultiValue;
import colesico.framework.telehttp.HttpTeleReader;
import colesico.framework.telehttp.Origin;

import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
public class PostOrigin implements Origin {

    @Override
    public Collection<String> getStrings(String name,  HttpTeleReader.Channel channel) {
        List<String> result = new ArrayList<>();
        MultiValue<String> headers = channel.httpRequest().postParameters().getAll(name);
        if (headers != null) {
            headers.iterator().forEachRemaining(result::add);
        }
        return result;
    }
}
