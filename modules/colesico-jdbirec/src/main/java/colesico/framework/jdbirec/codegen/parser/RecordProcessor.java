package colesico.framework.jdbirec.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractProcessor;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.jdbirec.Record;
import colesico.framework.jdbirec.codegen.generator.RecordKitGenerator;
import colesico.framework.jdbirec.codegen.model.ProfileSetElement;
import colesico.framework.jdbirec.codegen.model.RecordElement;
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

public class RecordProcessor extends FrameworkAbstractProcessor {

    private RecordParser recordParser;
    private RecordKitGenerator recordHelperGenerator;

    public RecordProcessor() {
        super();
    }

    @Override
    protected Class<? extends Annotation>[] getSupportedAnnotations() {
        return new Class[]{Record.class};
    }

    @Override
    protected void onInit() {
        recordParser = new RecordParser(processingEnv);
        recordHelperGenerator = new RecordKitGenerator(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element elm : roundEnv.getElementsAnnotatedWith(Record.class)) {
            if (elm.getKind() != ElementKind.CLASS) {
                continue;
            }
            TypeElement typeElement = null;
            try {
                typeElement = (TypeElement) elm;
                logger.debug("Process DB record class: "+typeElement.getSimpleName());
                ProfileSetElement profiles = recordParser.parse(new ClassElement(processingEnv, typeElement));
                recordHelperGenerator.generate(profiles);
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


