/*
 * Copyright © 2014-2020 Vladlen V. Larionov and others as noted.
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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Strings operations helper class
 *
 * @author Vladlen Larionov
 */
public class StrUtils {

    public static List<String> split(String str, String separator) {
        List<String> strParts = new ArrayList<>();
        StringTokenizer strTokenizer = new StringTokenizer(str, separator);
        while (strTokenizer.hasMoreTokens()) {
            strParts.add(StringUtils.trim(strTokenizer.nextToken()));
        }
        return strParts;
    }

    public static String firstCharToLowerCase(String str) {
        char[] chars = str.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    public static String firstCharToUpperCase(String str) {
        char[] chars = str.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    /**
     * Convert CamelCase to cebab-case or snake_case notation
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
     * @param str
     * @param separator
     * @return
     */
    public static String fromSeparatorNotation(String str, char separator) {
        if (StringUtils.isEmpty(str)) {
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
        if (StringUtils.isBlank(prefix)) {
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
            result = result.substring(0,result.length() - 1);
        }

        return result;
    }
}
