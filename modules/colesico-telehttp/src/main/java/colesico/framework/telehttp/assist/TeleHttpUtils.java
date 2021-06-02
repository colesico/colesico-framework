package colesico.framework.telehttp.assist;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TeleHttpUtils {

    public static String inputStreamToString(InputStream is) throws IOException {

        StringBuilder sb = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
        }
        return sb.toString();
    }

}
