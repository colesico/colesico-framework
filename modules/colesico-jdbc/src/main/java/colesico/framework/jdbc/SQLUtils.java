package colesico.framework.jdbc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SQLUtils {
    public static String loadQueryText(String filePath) {
        if (!filePath.endsWith(".sql")) {
            filePath = filePath + ".sql";
        }
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classloader.getResourceAsStream(filePath)) {
            InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder text = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                text.append(line).append("\n");
            }
            return text.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
