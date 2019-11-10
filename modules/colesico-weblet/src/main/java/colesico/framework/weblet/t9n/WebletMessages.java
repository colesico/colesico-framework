/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.weblet.t9n;

import colesico.framework.translation.Dictionary;
import colesico.framework.translation.assist.Ru;
import colesico.framework.translation.assist.Text;

@Dictionary
public interface WebletMessages {

    @Text("Invalid boolean format")
    @Ru(value = "Неверный формат логического значения")
    String invalidBooleanFormat(String name);

    @Text("Invalid date format")
    @Ru(value = "Неверный формат даты/времени")
    String invalidDateFormat(String name);

    @Text("Invalid number format")
    @Ru(value = "Неверный числовой формат")
    String invalidNumberFormat(String name);
}
