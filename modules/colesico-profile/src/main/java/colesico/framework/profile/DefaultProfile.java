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

package colesico.framework.profile;


import java.util.Locale;

/**
 * Default profile implementation
 */
public class DefaultProfile implements Profile {

    private Locale locale;
    private String[] qualifiers;

    public DefaultProfile(Locale locale) {
        this.locale = locale;
        createQualifiers();
    }

    protected void createQualifiers() {
        this.qualifiers = new String[]{locale.getLanguage(), locale.getCountry()};
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        createQualifiers();
    }

    @Override
    public String[] getQualifiers() {
        return qualifiers;
    }
}
