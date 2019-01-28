package colesico.framework.translation.codegen.processor;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.ioc.codegen.parser.ProducersProcessor;
import colesico.framework.translation.Dictionary;
import colesico.framework.translation.Translation;
import colesico.framework.translation.codegen.generator.BundleGenerator;
import colesico.framework.translation.codegen.generator.DictionaryGenerator;
import colesico.framework.translation.codegen.generator.IocGenerator;
import colesico.framework.translation.codegen.model.DictionaryElement;
import colesico.framework.translation.codegen.model.DictionaryRegistry;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DictionaryProcessor extends AbstractProcessor {

    protected Logger logger;

    protected ProcessingEnvironment processingEnv;
    protected Elements elementUtils;

    protected DictionaryGenerator beanGenerator;

    protected DictionaryRegistry dictionaryRegistry;
    protected IocGenerator iocGenerator;
    protected BundleGenerator dictionaryGenerator;

    public DictionaryProcessor() {
        try {
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
            System.setProperty("org.slf4j.simpleLogger.log.colesico.framework", "debug");
            logger = LoggerFactory.getLogger(ProducersProcessor.class);
        } catch (Throwable e) {
            System.out.print("Logger creation error: ");
            System.out.println(e);
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> result = new HashSet<>();
        result.add(Dictionary.class.getName());
        return result;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        try {
            this.processingEnv = processingEnv;
            this.elementUtils = processingEnv.getElementUtils();
            this.beanGenerator = new DictionaryGenerator(processingEnv);
            this.iocGenerator = new IocGenerator(processingEnv);
            this.dictionaryGenerator = new BundleGenerator(processingEnv);
            this.dictionaryRegistry = new DictionaryRegistry(processingEnv);
        } catch (Throwable e) {
            System.out.print("Error initializing " + DictionaryProcessor.class.getName() + " ");
            System.out.println(e);
        }
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
