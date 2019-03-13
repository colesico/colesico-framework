package colesico.framework.webstatic;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

public class MimeAssist {
    protected static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    public static String getContentType(String resourcePath) {
        try {
            String resourceExt = StringUtils.lowerCase(FilenameUtils.getExtension(resourcePath));
            MimeType mimeType = MimeType.valueOf(resourceExt);
            return mimeType.getContentType();
        } catch (IllegalArgumentException ex) {
            return DEFAULT_CONTENT_TYPE;
        }
    }

}
