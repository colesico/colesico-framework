package colesico.framework.telehttp.origin;

import colesico.framework.http.HttpException;
import colesico.framework.telehttp.HttpTeleReader;
import colesico.framework.telehttp.Origin;
import colesico.framework.telehttp.assist.TeleHttpUtils;

import jakarta.inject.Singleton;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

@Singleton
public class BodyOrigin implements Origin {

    @Override
    public Collection<String> getStrings(String name,  HttpTeleReader.Channel channel) {
        try (InputStream is = channel.httpRequest().inputStream()) {
            String content = TeleHttpUtils.inputStreamToString(is);
            return List.of(content);
        } catch (Exception e) {
            throw new HttpException(e, 500);
        }
    }

}
