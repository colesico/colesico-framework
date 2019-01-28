package colesico.framework.resource.internal;

import org.apache.commons.lang3.StringUtils;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class EvaluationTool {

    public static final char PROPERTY_PREFIX = '$';
    public static final char PATH_SEPARATOR = '/';

    private final Map<String, String> propertiesMap = new HashMap<>();

    public void addProperty(String name, String value) {
        if (name == null || "".equals(name)) {
            throw new RuntimeException("Property name is empty or null");
        }

        if (value == null || "".equals(value)) {
            throw new RuntimeException("Value is empty or null");
        }

        if (name.charAt(0) != PROPERTY_PREFIX) {
            throw new RuntimeException("Property name '" + name + "' must starts with '" + PROPERTY_PREFIX + "'");
        }

        /*
        if ((value.charAt(0) == PATH_SEPARATOR) || (value.charAt(value.length() - 1) == PATH_SEPARATOR)) {
            throw new RuntimeException("Value '" + value + "' must not starts or ends with '" + PATH_SEPARATOR + "'");
        }
        */

        String oldPath = propertiesMap.put(name, value);
        if (oldPath != null) {
            throw new RuntimeException("Duplicate property: " + name + "=" + value + " (" + oldPath + ")");
        }
    }

    public String getValue(String name) {
        String path = propertiesMap.get(name);
        if (path == null) {
            throw new RuntimeException("Undefined parameter: " + name);
        }
        return path;
    }

    private List<String> splitPath(String path) {
        List<String> res = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);
            if (c == PATH_SEPARATOR) {
                res.add(sb.toString());
                sb = new StringBuilder();
            } else {
                sb.append(c);
            }
        }
        res.add(sb.toString());
        return res;
    }

    public String evaluate(String path) {
        List<String> pathItems = splitPath(path);
        int n = pathItems.size();
        for (int i = 0; i < n; i++) {
            String pathItem = pathItems.get(i);
            if ((!"".equals(pathItem)) && pathItem.charAt(0) == PROPERTY_PREFIX) {
                pathItem = getValue(pathItem);
            }
            pathItems.set(i, pathItem);
        }
        return StringUtils.join(pathItems, PATH_SEPARATOR);
    }

}