package colesico.framework.assist.codegen.model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

abstract public class Toolbox {

    protected final ProcessingEnvironment processingEnv;

    public Toolbox(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    protected ProcessingEnvironment getProcessingEnv() {
        return processingEnv;
    }

    protected Elements getElementUtils() {
        return processingEnv.getElementUtils();
    }

    protected Types getTypeUtils() {
        return processingEnv.getTypeUtils();
    }

}
