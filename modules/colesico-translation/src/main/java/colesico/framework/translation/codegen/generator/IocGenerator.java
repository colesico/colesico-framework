package colesico.framework.translation.codegen.generator;

import colesico.framework.ioc.codegen.generator.ProducerGenerator;
import colesico.framework.translation.codegen.model.DictionaryElement;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import java.util.List;

public class IocGenerator {

    private Logger logger = LoggerFactory.getLogger(IocGenerator.class);
    protected final ProcessingEnvironment processingEnv;

    public IocGenerator(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    protected String getProducerClassSimpleName() {
        return "TranslationProducer";
    }

    public void generateIocProduccer(String packageName, List<DictionaryElement> dictionaryElements) {
        ProducerGenerator producerGenerator = new ProducerGenerator(packageName, getProducerClassSimpleName(), this.getClass(), processingEnv);
        logger.debug("Generating translation producer: " + producerGenerator.getProducerClassFilePath());

        if (producerGenerator.isProducerExists()) {
            logger.debug("Translation IoC producer file exists: " + producerGenerator.getProducerClassFilePath() + ". Producer file will not be generated");
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Translation IoC producers class already exists: " + producerGenerator.getProducerClassName() + ". Clean the project to rebuild this class.");
            return;
        } else {
            logger.debug("IoC producer file is not exists, will be created: " + producerGenerator.getProducerClassFilePath());
        }

        int i = 0;
        for (DictionaryElement dbe : dictionaryElements) {
            TypeName implTypeName = ClassName.bestGuess(packageName + '.' + dbe.getImplClassSimpleName());
            producerGenerator.addProduceAnnotation(implTypeName);

            String methodName = "get" + dbe.getOriginBean().getSimpleName().toString() + i;
            TypeName retTypeName = TypeName.get(dbe.getOriginBean().asType());
            producerGenerator.addImplementMethod(methodName, retTypeName,implTypeName);
            i++;
        }

        producerGenerator.generate();
    }

}
