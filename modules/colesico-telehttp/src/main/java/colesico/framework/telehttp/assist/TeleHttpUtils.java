package colesico.framework.telehttp.assist;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

public class TeleHttpUtils {

    public static Locale getAcceptedLanguage(String acceptLanguage) {
        if (StringUtils.isBlank(acceptLanguage)) {
            return null;
        }

        try {
            StringTokenizer langTokenizer = new StringTokenizer(acceptLanguage, ",");

            double maxQ = Double.MIN_VALUE;
            String maxTag = null;
            while (langTokenizer.hasMoreTokens()) {
                String langItem = langTokenizer.nextToken();
                int semiIndex = langItem.indexOf(';');

                double q;
                String tag;
                if (semiIndex > -1) {
                    int eqIndex = langItem.indexOf('=');
                    String qStr = StringUtils.trim(langItem.substring(eqIndex + 1));
                    q = Double.parseDouble(qStr);
                    tag = langItem.substring(0, semiIndex);
                } else {
                    q = 1;
                    tag = langItem;
                }

                if (q > maxQ) {
                    maxQ = q;
                    maxTag = tag;
                }
            }

            maxTag = StringUtils.trim(maxTag);
            maxTag = StringUtils.replace(maxTag, "-", "_");
            return Locale.forLanguageTag(maxTag);
        } catch (Exception e) {
            return null;
        }
    }

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

    public static String stringifyProperties(Map<String, String> properties) {
        if (properties == null || properties.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        try {
            for (Map.Entry<String, String> e : properties.entrySet()) {
                if (!sb.isEmpty()) {
                    sb.append("&");
                }
                String name = e.getKey();
                String value = URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8);
                sb.append(name).append("=").append(value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return sb.toString();
    }

    public static Map<String, String> parseProperties(String properties) {
        if (StringUtils.isBlank(properties)) {
            return Map.of();
        }

        Map<String, String> result = new HashMap<>();

        StringTokenizer st = new StringTokenizer(properties, "&");
        while (st.hasMoreTokens()) {
            String property = st.nextToken();
            String[] keyVal = StringUtils.split(property, "=");
            String key = keyVal[0];
            String val = URLDecoder.decode(keyVal[1], StandardCharsets.UTF_8);
            result.put(key, val);
        }

        return result;
    }

}
