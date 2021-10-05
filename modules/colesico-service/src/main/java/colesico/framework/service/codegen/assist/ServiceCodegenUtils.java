package colesico.framework.service.codegen.assist;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.service.codegen.model.TeleArgumentElement;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.service.codegen.model.TeleParameterElement;
import com.squareup.javapoet.CodeBlock;

import javax.lang.model.type.TypeMirror;

public final class ServiceCodegenUtils {

    public static void generateTeleResultType(TeleMethodElement teleMethod, CodeBlock.Builder cb) {
        TypeMirror returnType = teleMethod.getServiceMethod().getOriginMethod().getReturnType();
        CodegenUtils.generateTypePick(returnType, cb);
    }

    public static void generateTeleArgumentType(TeleArgumentElement teleArg, CodeBlock.Builder cb) {
        // Detect param type considering generics
        TypeMirror paramType = teleArg.getOriginElement().getOriginType();
        // ParamType.class or  for generics: new TypeWrapper<TheType>(){}.unwrap()
        CodegenUtils.generateTypePick(paramType, cb);
    }
}
