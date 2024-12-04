package colesico.framework.jdbirec.codegen.generator;

import colesico.framework.assist.StrUtils;
import colesico.framework.jdbirec.RecordKitFactory;
import colesico.framework.jdbirec.codegen.model.RecordViewElement;

public class RecordKitGeneratorUtils {
    public static String buildRecordKitInstanceClassName(RecordViewElement view) {
        if (view.isDefaultView()) {
            return view.getRecord().getRecordKit().getOriginClass().getSimpleName() + RecordKitFactory.KIT_IMPL_CLASS_SUFFIX;
        } else {
            String namePart = StrUtils.firstCharToUpperCase(view.getName());
            return view.getRecord().getRecordKit().getOriginClass().getSimpleName() + namePart + RecordKitFactory.KIT_IMPL_CLASS_SUFFIX;
        }
    }
}
