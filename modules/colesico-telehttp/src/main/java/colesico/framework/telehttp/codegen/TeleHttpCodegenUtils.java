package colesico.framework.telehttp.codegen;

import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.service.codegen.model.teleapi.TeleOrdinaryParamElement;
import colesico.framework.service.codegen.model.teleapi.TeleParameterElement;
import colesico.framework.service.codegen.model.teleapi.TeleCommandElement;
import colesico.framework.telehttp.ParamName;
import colesico.framework.telehttp.ParamOrigin;
import colesico.framework.telehttp.UseReader;
import colesico.framework.telehttp.UseWriter;

import javax.lang.model.type.TypeMirror;

public class TeleHttpCodegenUtils {

    public static String paramName(TeleParameterElement teleParam) {
        AnnotationAssist<ParamName> nameAnn = teleParam.originElement().annotation(ParamName.class);
        if (nameAnn != null) {
            return nameAnn.unwrap().value();
        } else {
            return teleParam.originElement().name();
        }
    }

    public static String originName(TeleParameterElement teleParam, String defaultOrigin) {
        TeleCommandElement teleCommand = teleParam.parentTeleCommand();
        String originName = defaultOrigin;
        AnnotationAssist<ParamOrigin> originAnn = teleParam.originElement().annotation(ParamOrigin.class);
        if (originAnn == null) {
            originAnn = teleCommand.serviceMethod().originMethod().annotation(ParamOrigin.class);
        }

        if (originAnn != null) {
            originName = originAnn.unwrap().value();
        }
        return originName;
    }

    public static TypeMirror customReaderClass(TeleOrdinaryParamElement teleParam) {
        var rdAnn = teleParam.originElement().annotation(UseReader.class);

        if (rdAnn == null) {
            rdAnn = teleParam.parentTeleCommand().serviceMethod().originMethod().annotation(UseReader.class);
        }

        if (rdAnn != null) {
            return rdAnn.valueTypeMirror(a -> a.value());
        }

        return null;
    }

    public static TypeMirror customWriterClass(TeleCommandElement teleCommand) {
        var wrAnn = teleCommand.serviceMethod().originMethod().annotation(UseWriter.class);
        if (wrAnn == null) {
            wrAnn = teleCommand.parentTeleService().parentService().originClass().annotation(UseWriter.class);
        }
        if (wrAnn == null) {
            return null;
        }
        return wrAnn.valueTypeMirror(a -> a.value());
    }

}
