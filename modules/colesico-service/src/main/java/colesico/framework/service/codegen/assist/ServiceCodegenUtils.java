package colesico.framework.service.codegen.assist;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.service.codegen.model.teleapi.*;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;

public final class ServiceCodegenUtils {

    public static void generateTeleResultType(TeleMethodElement teleMethod, CodeBlock.Builder cb) {
        TypeMirror returnType = teleMethod.getServiceMethod().getOriginMethod().getReturnType();
        CodegenUtils.generateTypePick(returnType, cb);
    }

    public static void generateTeleEntryType(TeleEntryElement teleEntry, CodeBlock.Builder cb) {
        // Detect param type considering generics
        TypeMirror paramType = teleEntry.getOriginElement().getOriginType();
        // ParamType.class or  for generics: new TypeWrapper<TheType>(){}.unwrap()
        CodegenUtils.generateTypePick(paramType, cb);
    }

    public static void generateTeleBatchType(TeleBatchElement teleBatch, CodeBlock.Builder cb) {
        TypeName batchTypeName = ClassName.bestGuess(teleBatch.getBatchClassName());
        cb.add("$T.class", batchTypeName);
    }
}
