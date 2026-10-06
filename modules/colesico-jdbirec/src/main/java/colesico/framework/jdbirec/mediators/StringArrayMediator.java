package colesico.framework.jdbirec.mediators;

import colesico.framework.jdbirec.FieldMediator;
import colesico.framework.jdbirec.RecordKitApi;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StringArrayMediator implements FieldMediator<String[]> {

    private static final String SPLIT_SEPARATOR = "\\|";
    private static final String JOIN_SEPARATOR = "|";

    @Override
    public String[] importField(String name, ResultSet rs) throws SQLException {
        String s = rs.getString(name);
        if (s == null || s.isBlank()) return null;
        return s.split(SPLIT_SEPARATOR, -1);
    }

    @Override
    public void exportField(String[] arr, String name, RecordKitApi.FieldReceiver fr) {
        if (arr == null) {
            fr.set(name, null);
            return;
        }
        fr.set(name, String.join(JOIN_SEPARATOR, arr));
    }
}