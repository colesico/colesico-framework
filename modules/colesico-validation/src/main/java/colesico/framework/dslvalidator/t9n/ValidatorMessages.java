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

package colesico.framework.dslvalidator.t9n;

import colesico.framework.translation.Dictionary;
import colesico.framework.translation.assist.Ru;
import colesico.framework.translation.assist.Text;

@Dictionary
public interface ValidatorMessages {

    @Text("Value required")
    @Ru("Требуется значение")
    String valueRequired();

    @Text("Invalid date format")
    @Ru("Неверный формат даты")
    String invalidDateFormat();

    //
    // LengthVerifier
    //

    @Text("Allowable number of characters from {0} to {1}")
    @Ru("Допустимое количество символов от {0} до {1}")
    String allowableNumberOfCharactersBetween(Integer lo, Integer hi);

    @Text("Minimally allowable {0} characters")
    @Ru("Минимально допустимо {0} символов")
    String minimallyAllowableCharacters(Integer lo);

    @Text(value = "Maximum allowable {0} characters")
    @Ru(value = "Максимально допустимо {0} символов")
    String maximumAllowableCharacters(Integer hi);

    //
    // IntervalVerifier
    //

    @Text("Value should be between {0} and {1}")
    @Ru(value = "Значение должно быть между {0} и {1}")
    String valueShouldBeBetween(Number min, Number max);

    @Text("Value should be greater than {0}")
    @Ru(value = "Значение должно быть больше {0}")
    String valueShouldBeGreaterThan(Number val);

    @Text(value = "Value should be less than {0}")
    @Ru(value = "Значение должно быть меньше {0}")
    String valueShouldBeLessThan(Number val);

    //
    // SizeVerifier
    //

    @Text("Number of elements should be between {0} and {1}")
    @Ru(value = "Количество элементов должно быть между {0} и {1}")
    String sizeShouldBeBetween(Number min, Number max);

    @Text("Number of elements should be greater than {0}")
    @Ru(value = "Количество элементов должно быть больше {0}")
    String sizeShouldBeGreaterThan(Number val);

    @Text(value = "Number of elements should be less than {0}")
    @Ru(value = "Количество элементов должно быть меньше {0}")
    String sizeShouldBeLessThan(Number val);


}
