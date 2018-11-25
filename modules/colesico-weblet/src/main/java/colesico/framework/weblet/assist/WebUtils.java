/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */

package colesico.framework.weblet.assist;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;
import java.util.StringTokenizer;

/**
 * @author Vladlen Larionov
 */
public class WebUtils {



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
        } catch (Exception e){
            return null;
        }
    }


}
