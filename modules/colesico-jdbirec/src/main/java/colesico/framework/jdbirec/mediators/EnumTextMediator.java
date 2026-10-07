package colesico.framework.jdbirec.mediators;

import colesico.framework.jdbirec.FieldMediator;
import colesico.framework.jdbirec.RecordKitApi;

import java.sql.ResultSet;
import java.sql.SQLException;

abstract public class EnumTextMediator<E extends Enum<E>> implements FieldMediator<E> {

    abstract protected E valueOf(String name);

    @Override
    public E importField(String columnName, ResultSet rs) throws SQLException {
        String value = rs.getString(columnName);
        return valueOf(value);
    }

    @Override
    public void exportField(E fieldValue, String fieldName, RecordKitApi.FieldReceiver fr) {
        fr.set(fieldName, fieldValue.name());
    }
}
