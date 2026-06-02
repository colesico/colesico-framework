/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package colesico.framework.assist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Strings operations helper class
 *
 * @author Vladlen Larionov
 */
public class StringUtils {

    public static boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

    public static boolean endsWith(CharSequence str, CharSequence suffix) {
        if (str == null || suffix == null) {
            return str == null && suffix == null;
        }
        if (suffix.length() > str.length()) {
            return false;
        }
        return str.toString().endsWith(suffix.toString());
    }

    public static List<String> split(String str, String separator) {
        if (str == null) {
            return null;
        }
        List<String> result = new ArrayList<>();
        if (str.isEmpty()) {
            return result;
        }
        if (separator == null || separator.isEmpty()) {
            String trimmed = str.trim();
            if (!trimmed.isEmpty()) {
                result.add(trimmed);
            }
            return result;
        }

        int start = 0;
        int end = str.indexOf(separator, start);
        int sepLength = separator.length();

        while (end != -1) {
            String token = str.substring(start, end).trim();
            if (!token.isEmpty()) {
                result.add(token);
            }
            start = end + sepLength;
            end = str.indexOf(separator, start);
        }

        String lastToken = str.substring(start).trim();
        if (!lastToken.isEmpty()) {
            result.add(lastToken);
        }

        return result;
    }

    public static String join(Collection<?> collection, String separator) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }
        String sep = (separator == null) ? "" : separator;
        StringBuilder sb = new StringBuilder();
        java.util.Iterator<?> iterator = collection.iterator();
        sb.append(java.util.Objects.toString(iterator.next(), ""));
        while (iterator.hasNext()) {
            sb.append(sep);
            sb.append(java.util.Objects.toString(iterator.next(), ""));
        }
        return sb.toString();
    }

    public static String trim(final String str) {
        return str == null ? null : str.trim();
    }

    public static boolean contains(CharSequence seq, CharSequence searchSeq) {
        if (seq == null || searchSeq == null) {
            return false;
        }
        return seq.toString().contains(searchSeq);
    }

    public static String replace(String text, String searchString, String replacement) {
        if (text == null || text.isEmpty() || searchString == null || searchString.isEmpty()) {
            return text;
        }
        if (replacement == null) {
            replacement = "";
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1) {
            return text;
        }
        int replLength = searchString.length();
        int increase = Math.max(replacement.length() - replLength, 0) * 16;
        StringBuilder sb = new StringBuilder(text.length() + increase);
        while (end != -1) {
            sb.append(text, start, end);
            sb.append(replacement);
            start = end + replLength;
            end = text.indexOf(searchString, start);
        }
        sb.append(text, start, text.length());
        return sb.toString();
    }

    public static String substring(String str, int start) {
        if (str == null) {
            return null;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return "";
        }
        return str.substring(start);
    }

    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }
        if (end < 0) {
            end = str.length() + end;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        if (end > str.length()) {
            end = str.length();
        }
        if (start > end) {
            return "";
        }
        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }
        return str.substring(start, end);
    }

    public static String firstCharToLowerCase(String str) {
        if (isBlank(str)) {
            return str;
        }
        char[] chars = str.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    public static String firstCharToUpperCase(String str) {
        if (isBlank(str)) {
            return str;
        }
        char[] chars = str.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    /**
     * Convert CamelCase to cebab-case or snake_case notation
     *
     * @param str
     * @param separator
     * @return
     */
    public static String toSeparatorNotation(String str, char separator) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    result.append(separator);
                }
                c = Character.toLowerCase(c);
            }
            result.append(c);
        }
        return result.toString();
    }

    /**
     * Convert snake_kase or kebab-case to CamelCase notation
     *
     * @param str
     * @param separator
     * @return
     */
    public static String fromSeparatorNotation(String str, char separator) {
        if (isBlank(str)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        boolean toUpper = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == separator) {
                toUpper = true;
                continue;
            }
            if (toUpper) {
                result.append(Character.toUpperCase(c));
                toUpper = false;
                continue;
            }
            result.append(c);
        }
        return result.toString();
    }

    public static String addPrefix(String prefix, String base) {
        if (isBlank(prefix)) {
            return base;
        }
        char first = Character.toUpperCase(base.charAt(0));
        return prefix + first + base.substring(1);
    }

    public static String concatPath(String prefix, String suffix, String separator) {
        if (!prefix.endsWith(separator)) {
            prefix = prefix + separator;
        }

        // remove suffix leading separators
        while (suffix.startsWith(separator)) {
            suffix = suffix.substring(1);
        }

        String result = prefix + suffix;

        // remove result trailing separator
        while (result.endsWith(separator)) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }
}
