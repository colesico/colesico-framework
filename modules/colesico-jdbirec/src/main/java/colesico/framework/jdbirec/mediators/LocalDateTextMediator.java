package colesico.framework.jdbirec.mediators;

import colesico.framework.jdbirec.AbstRactrecordKit;
import colesico.framework.jdbirec.FieldMediator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateTextMediator implements FieldMediator<LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public LocalDate importField(String columnName, ResultSet rs) throws SQLException {
        String dateStr = rs.getString(columnName);
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateStr.trim(), FORMATTER);
    }

    @Override
    public void exportField(LocalDate date, String fieldName, AbstRactrecordKit.FieldReceiver fr) {
        fr.set(fieldName, date == null ? null : date.format(FORMATTER));
    }
}
