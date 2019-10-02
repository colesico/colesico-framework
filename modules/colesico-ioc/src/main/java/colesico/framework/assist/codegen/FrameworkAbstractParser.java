package colesico.framework.assist.codegen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Map;

abstract public class FrameworkAbstractParser {
    protected final Logger logger;
    protected final ProcessingEnvironment processingEnv;

    public FrameworkAbstractParser(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        logger = LoggerFactory.getLogger(this.getClass());
    }

    protected ProcessingEnvironment getProcessingEnv() {
        return processingEnv;
    }

    protected Elements getElementUtils() {
        return processingEnv.getElementUtils();
    }

    protected Messager getMessager() {
        return processingEnv.getMessager();
    }

    protected Filer getFiler() {
        return processingEnv.getFiler();
    }

    protected Types getTypeUtils() {
        return processingEnv.getTypeUtils();
    }

    protected Map<String, String> getOptions() {
        return processingEnv.getOptions();
    }

    protected CodegenMode getCodegenMode() {
        String codegenModeKey = processingEnv.getOptions().get(CodegenUtils.OPTION_CODEGEN);
        return CodegenMode.fromKey(codegenModeKey);
    }
}

