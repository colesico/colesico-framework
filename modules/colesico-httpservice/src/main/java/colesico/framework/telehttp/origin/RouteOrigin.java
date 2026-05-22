package colesico.framework.telehttp.origin;

import colesico.framework.telehttp.HttpTeleReader;
import colesico.framework.telehttp.Origin;

import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
public class RouteOrigin implements Origin {

    @Override
    public Collection<String> getStrings(String name, HttpTeleReader.Channel  channel) {
        List<String> result = new ArrayList<>();
        var value = channel.routerContext().parameters().get(name);
        if (value != null) {
            result.add(value);
        }
        return result;
    }
}
