package colesico.framework.beanvalidation.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractProcessor;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.beanvalidation.ValidatorBuilder;
import colesico.framework.beanvalidation.codegen.generator.ValidatorBuilderGenerator;
import colesico.framework.beanvalidation.codegen.model.ValidatedBeanElement;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BeanValidationProcessor extends FrameworkAbstractProcessor {

    private BeanValidationParser parser;
    private ValidatorBuilderGenerator generator;

    private List<ValidatedBeanElement> validatedBeans;

    @Override
    protected Class<? extends Annotation>[] getSupportedAnnotations() {
        return new Class[]{ValidatorBuilder.class};
    }

    @Override
    protected void onInit() {
        parser = new BeanValidationParser(processingEnv);
        generator = new ValidatorBuilderGenerator(processingEnv);
        validatedBeans = new ArrayList<>();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        for (Element elm : roundEnv.getElementsAnnotatedWith(ValidatorBuilder.class)) {
            if (!(elm.getKind() == ElementKind.CLASS)) {
                throw CodegenException.of().element(elm).message("Validatable bean is not a Class").build();
            }
            TypeElement beanClass;
            try {
                beanClass = (TypeElement) elm;
                logger.debug("Processing validatale bean class: " + beanClass.getSimpleName());
                ValidatedBeanElement validatedBean = parser.parse(ClassElement.fromElement(processingEnv, beanClass));
                validatedBeans.add(validatedBean);
                generator.generate(validatedBean);
            } catch (CodegenException ce) {
                String message = "Error processing validatale bean class '" + elm + "': " + ce.getMessage();
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
