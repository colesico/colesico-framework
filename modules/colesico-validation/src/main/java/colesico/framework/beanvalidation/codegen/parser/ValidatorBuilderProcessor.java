package colesico.framework.beanvalidation.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractProcessor;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.beanvalidation.ValidatorBuilder;
import colesico.framework.beanvalidation.ValidatorBuilderPrototype;
import colesico.framework.beanvalidation.ValidatorBuilderPrototypes;
import colesico.framework.beanvalidation.codegen.generator.IocGenerator;
import colesico.framework.beanvalidation.codegen.model.ValidatorBuilderElement;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.Set;

public class ValidatorBuilderProcessor extends FrameworkAbstractProcessor {

    private ValidatorBuilderParser parser;
    private IocGenerator generator;

    @Override
    protected Class<? extends Annotation>[] getSupportedAnnotations() {
        return new Class[]{ValidatorBuilder.class};
    }

    @Override
    protected void onInit() {
        parser = new ValidatorBuilderParser(processingEnv);
        generator = new IocGenerator(processingEnv);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {

        for (Element elm : roundEnv.getElementsAnnotatedWithAny(toAnnotationsSet(ValidatorBuilderPrototype.class, ValidatorBuilderPrototypes.class))) {
            if (!(elm.getKind() == ElementKind.CLASS)) {
                throw CodegenException.of().element(elm).message("Validator builder bean is not a Class").build();
            }
            TypeElement beanClass;
            try {
                beanClass = (TypeElement) elm;
                logger.debug("Processing validator builder bean class: " + beanClass.getSimpleName());
                ValidatorBuilderElement validatorBuilder = parser.parse(ClassElement.of(processingEnv, beanClass));
                generator.generate(validatorBuilder);
            } catch (CodegenException ce) {
                String message = "Error processing validator builder class '" + elm + "': " + ce.getMessage();
                logger.debug(message);
                ce.print(processingEnv, elm);
            } catch (Exception e) {
                String msg = ExceptionUtils.getRootCauseMessage(e);
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
                if (logger.isDebugEnabled()) {
                    e.printStackTrace();
                }
                // annotations are unclaimed and subsequent processors may be asked to  process them again
                return false;
            }
        }

        // annotations are claimed and subsequent processors will not be asked to  process them again
        return true;
    }
}
