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
