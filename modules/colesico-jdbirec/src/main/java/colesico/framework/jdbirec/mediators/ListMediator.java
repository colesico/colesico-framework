package colesico.framework.jdbirec.mediators;

import colesico.framework.jdbirec.FieldMediator;
import colesico.framework.jdbirec.RecordKit;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

abstract public class ListMediator<T> implements FieldMediator<List<T>> {

    abstract protected T[] newArray(int size);

    @Override
    public List<T> importField(String columnName, ResultSet rs) throws SQLException {
        Array array = rs.getArray(columnName);
        if (array == null) {
            return null;
        }
        T[] values = (T[]) array.getArray();
        List<T> result = Arrays.asList(values);
        return result;
    }

    @Override
    public void exportField(List<T> value, String fieldName, RecordKit.FieldReceiver fr) {
        fr.set(fieldName, value == null ? null : value.toArray(newArray(value.size())));
    }

}
