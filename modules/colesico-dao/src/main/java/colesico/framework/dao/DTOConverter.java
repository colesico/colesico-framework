package colesico.framework.dao;

public interface DTOConverter<F> {

    String TO_FIELD_MATHOD="toFiled";
    String FROM_FIELD_MATHOD="fromFiled";

    F toFiled(Object sqlObject);

    Object fromFiled(F value);
}
