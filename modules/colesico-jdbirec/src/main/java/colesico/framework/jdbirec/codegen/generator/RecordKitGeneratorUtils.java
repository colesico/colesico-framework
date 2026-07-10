package colesico.framework.jdbirec.codegen.generator;

import colesico.framework.assist.StringUtils;
import colesico.framework.jdbirec.RecordKitFactory;
import colesico.framework.jdbirec.codegen.model.RecordViewElement;

public class RecordKitGeneratorUtils {
    public static String buildRecordKitInstanceClassName(RecordViewElement view) {
        if (view.isDefaultView()) {
            return view.record().recordKit().originClass().simpleName() + RecordKitFactory.KIT_IMPL_CLASS_SUFFIX;
        } else {
            String namePart = StringUtils.firstCharToUpperCase(view.name());
            return view.record().recordKit().originClass().simpleName() + namePart + RecordKitFactory.KIT_IMPL_CLASS_SUFFIX;
        }
    }
}
