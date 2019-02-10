package colesico.framework.dao.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractProcessor;
import colesico.framework.dao.DTO;
import colesico.framework.dao.codegen.generator.DTOHelperGenerator;
import colesico.framework.dao.codegen.model.DTOElement;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.Set;

public class DTOProcessor extends FrameworkAbstractProcessor {

    private DTOParser dtoParser;
    private DTOHelperGenerator dtoHelperGenerator;

    public DTOProcessor() {
        super();
    }

    @Override
    protected Class<? extends Annotation>[] getSupportedAnnotations() {
        return new Class[]{DTO.class};
    }

    @Override
    protected void onInit() {
        dtoParser = new DTOParser(processingEnv);
        dtoHelperGenerator = new DTOHelperGenerator(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element elm : roundEnv.getElementsAnnotatedWith(DTO.class)) {
            if (elm.getKind() != ElementKind.CLASS) {
                continue;
            }
            TypeElement typeElement = null;
            try {
                typeElement = (TypeElement) elm;
                DTOElement dtoElement = dtoParser.parse(typeElement);
                dtoHelperGenerator.generate(dtoElement);
            } catch (CodegenException ce) {
                String message = "Error processing class '" + elm.toString() + "': " + ce.getMessage();
                logger.debug(message);
                ce.print(processingEnv, elm);
            } catch (Exception e) {
                StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));
                String msg = ExceptionUtils.getRootCauseMessage(e);
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
                if (logger.isDebugEnabled()) {
                    e.printStackTrace();
                }
                return false;
            }
        }
        return true;
    }
}


