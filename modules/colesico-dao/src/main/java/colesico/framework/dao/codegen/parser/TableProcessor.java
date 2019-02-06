package colesico.framework.dao.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractProcessor;
import colesico.framework.dao.Table;
import colesico.framework.dao.codegen.generator.DtoAssistantGenerator;
import colesico.framework.dao.codegen.generator.PostgresTabeGenerator;
import colesico.framework.dao.codegen.generator.TableGenerator;
import colesico.framework.dao.codegen.model.TableElement;
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

public class TableProcessor extends FrameworkAbstractProcessor {

    private TableParser tableParser;
    private TableGenerator tableGenerator;
    private DtoAssistantGenerator dtoAssistantGenerator;

    public TableProcessor() {
        super();
    }

    @Override
    protected Class<? extends Annotation>[] getSupportedAnnotations() {
        return new Class[]{Table.class};
    }

    @Override
    protected void onInit() {
        tableParser = new TableParser(processingEnv);
        dtoAssistantGenerator = new DtoAssistantGenerator(processingEnv);
        //TODO: should be configurable
        tableGenerator = new PostgresTabeGenerator(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element elm : roundEnv.getElementsAnnotatedWith(Table.class)) {
            if (elm.getKind() != ElementKind.CLASS) {
                continue;
            }
            TypeElement typeElement = null;
            try {
                typeElement = (TypeElement) elm;
                TableElement tableElement = tableParser.parse(typeElement);
                tableGenerator.generate(tableElement);
                dtoAssistantGenerator.generate(tableElement);
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


