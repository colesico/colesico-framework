package colesico.framework.translation.codegen.processor;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractProcessor;
import colesico.framework.translation.Dictionary;
import colesico.framework.translation.Translation;
import colesico.framework.translation.codegen.generator.BundleGenerator;
import colesico.framework.translation.codegen.generator.DictionaryGenerator;
import colesico.framework.translation.codegen.generator.IocGenerator;
import colesico.framework.translation.codegen.model.DictionaryElement;
import colesico.framework.translation.codegen.model.DictionaryRegistry;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DictionaryProcessor extends FrameworkAbstractProcessor {


    protected DictionaryGenerator beanGenerator;

    protected DictionaryRegistry dictionaryRegistry;
    protected IocGenerator iocGenerator;
    protected BundleGenerator dictionaryGenerator;

    public DictionaryProcessor() {
        super();
    }

    @Override
    protected Class<? extends Annotation>[] getSupportedAnnotations() {
        return new Class[]{Dictionary.class};
    }

    @Override
    protected void onInit() {
        this.beanGenerator = new DictionaryGenerator(processingEnv);
        this.iocGenerator = new IocGenerator(processingEnv);
        this.dictionaryGenerator = new BundleGenerator(processingEnv);
        this.dictionaryRegistry = new DictionaryRegistry(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        dictionaryRegistry.clear();
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Dictionary.class);
        for (Element elm : elements) {
            if (elm.getKind() != ElementKind.INTERFACE) {
                continue;
            }
            TypeElement beanDefinitionElement;
            try {
                beanDefinitionElement = (TypeElement) elm;
                logger.debug("Processing dictionary bean: " + beanDefinitionElement.asType().toString());
                DictionaryElement dictionaryBeanElement = parseDictionaryFacade(beanDefinitionElement);
                dictionaryRegistry.register(dictionaryBeanElement);
                beanGenerator.generate(dictionaryBeanElement);
            } catch (CodegenException ce) {
                String message = "Error processing dictionary bean '" + elm.toString() + "': " + ce.getMessage();
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
                return true;
            }
        }

        if (!dictionaryRegistry.isEmpty()) {
            for (Map.Entry<String, List<DictionaryElement>> entry : dictionaryRegistry.getByPackageMap().entrySet()) {
                String packageName = entry.getKey();
                List<DictionaryElement> dictElms = entry.getValue();
                iocGenerator.generateIocProduccer(packageName, dictElms);
                dictionaryGenerator.generate(packageName, dictElms);
            }
        }

        return true;
    }

    protected DictionaryElement parseDictionaryFacade(TypeElement dictionaryBeanInterface) {
        DictionaryElement dictionaryBeanElement = new DictionaryElement(dictionaryBeanInterface);

        Elements utils = processingEnv.getElementUtils();
        List<? extends Element> members = utils.getAllMembers(dictionaryBeanInterface);
        List<ExecutableElement> methods = ElementFilter.methodsIn(members);


        for (ExecutableElement method : methods) {

            TypeElement methodClass = (TypeElement) method.getEnclosingElement();
            String methodClassName = methodClass.asType().toString();

            if (method.getModifiers().contains(Modifier.DEFAULT) || methodClassName.equals(Object.class.getName())) {
                continue;
            }

            if (!method.getReturnType().toString().equals(String.class.getName())) {
                continue;
            }

            dictionaryBeanElement.addTranslationMethod(method);

            // Find translations
            List<? extends AnnotationMirror> annList = method.getAnnotationMirrors();
            for (AnnotationMirror ann : annList) {
                TypeElement annTypeElm = (TypeElement) ann.getAnnotationType().asElement();
                Translation translationAnn = annTypeElm.getAnnotation(Translation.class);
                if (translationAnn == null) {
                    continue;
                }
                String localeKey = translationAnn.value();
                AnnotationValue value = CodegenUtils.getAnnotationValue(ann, "value");
                dictionaryBeanElement.addTranslation(method, localeKey, value.getValue().toString());
            }

        }
        return dictionaryBeanElement;
    }

}
