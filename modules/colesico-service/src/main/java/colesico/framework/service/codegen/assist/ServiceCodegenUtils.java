package colesico.framework.service.codegen.assist;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.service.codegen.model.teleapi.*;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;

public final class ServiceCodegenUtils {

    public static void generateTeleResultType(TeleCommandElement teleCommand, CodeBlock.Builder cb) {
        TypeMirror returnType = teleCommand.serviceMethod().originMethod().returnType();
        CodegenUtils.generateTypePick(returnType, cb);
    }

    public static void generateTeleParamType(TeleParameterElement teleParam, CodeBlock.Builder cb) {
        // Detect param type considering generics
        TypeMirror paramType = teleParam.originVariable().originType();
        // ParamType.class or  for generics: new TypeWrapper<TheType>(){}.unwrap()
        CodegenUtils.generateTypePick(paramType, cb);
    }

    public static void generateTeleParamBeanType(TeleBeanElement teleParamBean, CodeBlock.Builder cb) {
        TypeName paramBeanTypeName = ClassName.bestGuess(teleParamBean.paramBeanClassName());
        cb.add("$T.class", paramBeanTypeName);
    }
}
