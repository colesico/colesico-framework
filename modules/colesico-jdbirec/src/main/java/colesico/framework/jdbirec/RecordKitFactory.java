package colesico.framework.jdbirec;

import colesico.framework.assist.StrUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.reflect.Constructor;

public class RecordKitFactory {

    public static String KIT_CLASS_SUFFIX = "RecordKit";

    private static <R, K extends RecordKit<R>> K getKit(String kitClassName, Class<R> recordClass, String profile) {
        try {
            Class<K> kitClass = (Class<K>) Class.forName(kitClassName);
            Constructor<K> constructor = kitClass.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Record kit '" + kitClassName + "' for record class '" + recordClass.getName()
                + "' (profile=" + profile + ')'
                + " instantiation error: " + ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public static <R, K extends RecordKit<R>> K getKit(Class<R> recordClass) {
        String kitClassName = recordClass.getName() + KIT_CLASS_SUFFIX;
        return getKit(kitClassName, recordClass, RecordKitProfile.DEFAULT);
    }

    public static <R, K extends RecordKit<R>> K getKit(Class<R> recordClass, String profile) {

        if (profile == null || RecordKitProfile.DEFAULT.equals(profile)) {
            return getKit(recordClass);
        }
        
        String kitClassName = recordClass.getName() + StrUtils.firstCharToUpperCase(profile) + KIT_CLASS_SUFFIX;
        return getKit(kitClassName, recordClass, profile);
    }
}
