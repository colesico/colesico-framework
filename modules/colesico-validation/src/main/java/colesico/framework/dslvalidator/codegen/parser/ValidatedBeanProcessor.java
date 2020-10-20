package colesico.framework.dslvalidator.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractProcessor;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.dslvalidator.beanassist.ValidatorBuilderPrototype;
import colesico.framework.dslvalidator.codegen.generator.ValidatorBuilderPrototypeGenerator;
import colesico.framework.dslvalidator.codegen.model.ValidatedBeanElement;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.Set;

public class ValidatedBeanProcessor extends FrameworkAbstractProcessor {

    private ValidatedBeanParser vbParser;
    private ValidatorBuilderPrototypeGenerator vbpGenerator;

    @Override
    protected Class<? extends Annotation>[] getSupportedAnnotations() {
        return new Class[]{ValidatorBuilderPrototype.class};
    }

    @Override
    protected void onInit() {
        vbParser = new ValidatedBeanParser(processingEnv);
        vbpGenerator = new ValidatorBuilderPrototypeGenerator(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        for (Element elm : roundEnv.getElementsAnnotatedWith(ValidatorBuilderPrototype.class)) {
            if (!(elm.getKind() == ElementKind.INTERFACE)) {
                throw CodegenException.of().element(elm).message("Not an interface").build();
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
