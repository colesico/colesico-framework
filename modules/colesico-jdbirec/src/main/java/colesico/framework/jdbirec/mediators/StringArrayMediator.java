package colesico.framework.jdbirec.mediators;

import colesico.framework.jdbirec.FieldMediator;
import colesico.framework.jdbirec.RecordKitApi;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StringArrayMediator implements FieldMediator<String[]> {

    private static final String SEPARATOR = "|";

    @Override
    public String[] importField(String name, ResultSet rs) throws SQLException {
        String s = rs.getString(name);
        if (s == null || s.strip().isEmpty()) return null;
        return s.split(SEPARATOR, -1);
    }

    @Override
    public void exportField(String[] arr, String name, RecordKitApi.FieldReceiver fr) {
        if (arr == null) {
            fr.set(name, null);
            return;
        }
        fr.set(name, String.join(SEPARATOR, arr));
    }
}