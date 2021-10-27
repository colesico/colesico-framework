package colesico.framework.telehttp.assist;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
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

}
