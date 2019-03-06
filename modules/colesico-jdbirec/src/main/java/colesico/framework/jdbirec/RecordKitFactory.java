package colesico.framework.jdbirec;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.reflect.Constructor;

public class RecordKitFactory {

    public static String KIT_CLASS_SUFFIX = "RecordKit";

    public static <D, H extends RecordKit<D>> H getKit(Class<D> recordClass) {
        String helperClassName = recordClass.getName() + KIT_CLASS_SUFFIX;
        try {
            Class<H> helperClass = (Class<H>) Class.forName(helperClassName);
            Constructor<H> constructor = helperClass.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Helper '" + helperClassName + "' for RECORD class '" + recordClass.getName()+"' instantiation error: "+ ExceptionUtils.getRootCauseMessage(e));
        }
    }
}
