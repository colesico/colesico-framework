package colesico.framework.jdbirec.mediators;

import colesico.framework.jdbirec.AbstRactrecordKit;
import colesico.framework.jdbirec.FieldMediator;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StringArrayMediator implements FieldMediator<String[]> {

    // Standard ASCII Unit Separator. Never appears in human text, eliminates the need for escaping.
    private static final String SEPARATOR = "\u001F";

    @Override
    public String[] importField(String name, ResultSet rs) throws SQLException {
        String s = rs.getString(name);
        if (s == null || s.strip().isEmpty()) return null;

        // Split by the non-printable character, preserving trailing empty elements
        return s.split(SEPARATOR, -1);
    }

    @Override
    public void exportField(String[] arr, String name, AbstRactrecordKit.FieldReceiver fr) {
        if (arr == null) { fr.set(name, null); return; }

        // Join directly without heavy processing or character checking
        fr.set(name, String.join(SEPARATOR, arr));
    }
}