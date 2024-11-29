package colesico.framework.jdbirec.codegen.generator;

import colesico.framework.assist.StrUtils;
import colesico.framework.jdbirec.RecordKitFactory;
import colesico.framework.jdbirec.codegen.model.RecordElement;

public class RecordKitGeneratorUtils {
    public static String buildRecordKitInstanceClassName(RecordElement record) {
        if (record.isDefaultView()) {
            return record.getRecordKit().getOriginClass().getSimpleName() + RecordKitFactory.KIT_IMPL_CLASS_SUFFIX;
        } else {
            String namePart = StrUtils.firstCharToUpperCase(record.getView());
            return record.getRecordKit().getOriginClass().getSimpleName() + namePart + RecordKitFactory.KIT_IMPL_CLASS_SUFFIX;
        }
    }
}
