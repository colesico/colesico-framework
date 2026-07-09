package colesico.framework.beanvalidation.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractProcessor;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.beanvalidation.ValidatorBuilder;
import colesico.framework.beanvalidation.ValidatorBuilders;
import colesico.framework.beanvalidation.codegen.generator.IocGenerator;
import colesico.framework.beanvalidation.codegen.generator.ValidatorBuilderGenerator;
import colesico.framework.beanvalidation.codegen.model.BeanElement;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.Set;

import static colesico.framework.assist.ExceptionUtils.getRootCauseMessage;

public class ValidatorBuilderProcessor extends FrameworkAbstractProcessor {

    private ValidatedBeanParser parser;
    private ValidatorBuilderGenerator builderGenerator;
    protected IocGenerator iocGenerator;

    @Override
    protected Class<? extends Annotation>[] supportedAnnotations() {
        return new Class[]{ValidatorBuilders.class, ValidatorBuilder.class};
    }

    @Override
    protected void onInit() {
        parser = new ValidatedBeanParser(processingEnv);
        builderGenerator = new ValidatorBuilderGenerator(processingEnv);
        iocGenerator = new IocGenerator(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {

        for (Element elm : roundEnv.getElementsAnnotatedWithAny(toAnnotationsSet(ValidatorBuilder.class, ValidatorBuilders.class))) {
            if (!(elm.getKind() == ElementKind.CLASS)) {
                throw CodegenException.of().element(elm).message("Validatable bean is not a Class").build();
            }
            TypeElement beanClass;
            try {
                beanClass = (TypeElement) elm;
                logger.debug("Processing validated bean class: " + beanClass.getSimpleName());
                BeanElement validatedBean = parser.parse(ClassElement.of(processingEnv, beanClass));
                builderGenerator.generate(validatedBean);
                iocGenerator.generate(validatedBean);
            } catch (CodegenException ce) {
                String message = "Error processing validated bean class '" + elm + "': " + ce.getMessage();
                logger.debug(message);
                ce.print(processingEnv, elm);
            } catch (Exception e) {
                String msg = getRootCauseMessage(e);
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
