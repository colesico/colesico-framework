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

}
