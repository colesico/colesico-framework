package colesico.framework.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public interface DTOHelper<D> {

    String INIT_COMPOSITION_METOD = "initCompositions";
    String TO_MAP_METOD = "toMap";
    String FROM_RESULT_SET_METHOD = "fromResultSet";

    String DTO_PARAM = "dto";
    String MAP_PARAM = "map";
    String RESULT_SET_PARAM = "rs";

    void initCompositions(D dto);

    void toMap(D dto, Map<String, Object> map);

    void fromResultSet(D dto, ResultSet rs) throws SQLException;
}
