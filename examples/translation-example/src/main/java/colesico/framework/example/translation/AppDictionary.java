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

package colesico.framework.example.translation;

import colesico.framework.translation.Dictionary;
import colesico.framework.translation.TranslationDictionary;
import colesico.framework.translation.assist.lang.En;
import colesico.framework.translation.assist.lang.Ru;
import colesico.framework.translation.assist.lang.Text;

/**
 * An appropriate properties file will be generated for this interface.
 * Extends TranslationDictionary - is an optional, to ge access to dictionary API.
 */
@Dictionary
public interface AppDictionary extends TranslationDictionary {

    @En("Hello")  // For en language
    @Ru("Привет") // For ru lang
    @Text("HI")   // Default translation for any lang
    String hello();

    @Text("Bye {0}")
    String bye(String user);
}
