package colesico.framework.beanvalidator.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractProcessor;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.beanvalidator.ValidatorBuilderPrototype;
import colesico.framework.beanvalidator.codegen.generator.ValidatorBuilderGenerator;
import colesico.framework.beanvalidator.codegen.model.ValidatedBeanElement;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.Set;

public class BeanValidationProcessor extends FrameworkAbstractProcessor {

    private BeanValidationParser vbParser;
    private ValidatorBuilderGenerator vbpGenerator;

    @Override
    protected Class<? extends Annotation>[] getSupportedAnnotations() {
        return new Class[]{ValidatorBuilderPrototype.class};
    }

    @Override
    protected void onInit() {
        vbParser = new BeanValidationParser(processingEnv);
        vbpGenerator = new ValidatorBuilderGenerator(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        for (Element elm : roundEnv.getElementsAnnotatedWith(ValidatorBuilderPrototype.class)) {
            if (!(elm.getKind() == ElementKind.CLASS)) {
                throw CodegenException.of().element(elm).message("Validate bean is not a Class").build();
            }
            TypeElement beanClass;
            try {
                beanClass = (TypeElement) elm;
                logger.debug("Processing validated bean class: " + beanClass.getSimpleName());
                ValidatedBeanElement vbElement = vbParser.parse(ClassElement.fromElement(processingEnv, beanClass));
                vbpGenerator.generate(vbElement);
            } catch (CodegenException ce) {
                String message = "Error processing validated bean class '" + elm.toString() + "': " + ce.getMessage();
                logger.debug(message);
                ce.print(processingEnv, elm);
            } catch (Exception e) {
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
