package colesico.framework.jdbirec.mediators;

import colesico.framework.jdbirec.FieldMediator;
import colesico.framework.jdbirec.RecordKit;
import org.apache.commons.lang3.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

public class LocaleMediator implements FieldMediator<Locale> {

    @Override
    public Locale importField(String column, ResultSet rs) throws SQLException {
        String localeStr = rs.getString(column);
        if (StringUtils.isBlank(localeStr)) {
            return null;
        }
        return Locale.forLanguageTag(localeStr);
    }

    @Override
    public void exportField(Locale locale, String column, RecordKit.ColumnAssigner ca) {
        ca.set(column, locale == null ? null : locale.toLanguageTag());
    }

}
