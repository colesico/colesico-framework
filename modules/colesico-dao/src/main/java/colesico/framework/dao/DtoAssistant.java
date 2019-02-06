package colesico.framework.dao;

import java.util.Map;

public interface DtoAssistant<T> {

    String TO_MAP_METOD = "toMap";
    String MAP_PARAM = "map";
    String DTO_PARAM = "dto";

    void toMap(Map<String, Object> map, T dto);
}
