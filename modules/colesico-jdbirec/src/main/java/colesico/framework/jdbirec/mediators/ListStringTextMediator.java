package colesico.framework.jdbirec.mediators;

import colesico.framework.jdbirec.FieldMediator;
import colesico.framework.jdbirec.RecordKitApi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ListStringTextMediator implements FieldMediator<List<String>> {

    private static final String SPLIT_SEPARATOR = "\\|";
    private static final String JOIN_SEPARATOR = "|";

    @Override
    public List<String> importField(String name, ResultSet rs) throws SQLException {
        String s = rs.getString(name);
        if (s == null) return null;
        return Arrays.asList(s.split(SPLIT_SEPARATOR, -1));
    }

    @Override
    public void exportField(List<String> list, String name, RecordKitApi.FieldReceiver fr) {
        if (list == null) {
            fr.set(name, null);
            return;
        }
        fr.set(name, String.join(JOIN_SEPARATOR, list));
    }
}