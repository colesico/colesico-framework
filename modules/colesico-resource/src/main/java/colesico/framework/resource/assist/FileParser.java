package colesico.framework.resource.assist;

import org.apache.commons.lang3.StringUtils;

public class FileParser {

    public static final char PATH_SEP = '/';
    public static final char EXT_SEP = '.';
    private final int pathPos;
    private final int extPos;

    private final String path;

    public FileParser(String path) {
        pathPos = path.lastIndexOf(PATH_SEP);
        int ep = path.lastIndexOf(EXT_SEP);
        if (ep > pathPos) {
            extPos = ep;
        } else {
            extPos = path.length();
        }

        this.path = path;
    }

    public String extension() {
        return StringUtils.substring(path, extPos + 1);
    }

    public String fileName() {
        return StringUtils.substring(path, pathPos + 1, extPos);
    }

    public String path() {
        return StringUtils.substring(path, 0, pathPos);
    }
}