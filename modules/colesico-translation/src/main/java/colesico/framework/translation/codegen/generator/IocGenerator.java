package colesico.framework.translation.codegen.generator;

import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.ioc.codegen.generator.ProducerGenerator;
import colesico.framework.translation.codegen.model.DictionaryElement;
import colesico.framework.translation.codegen.model.DictionaryRegistry;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Map;

public class IocGenerator extends FrameworkAbstractGenerator {

    public static final String T9N_PRODUCER = "T9nProducer";

    public IocGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }


    private void generateIocProduccer(String packageName, String producerClassSimpleName, List<DictionaryElement> dictionaryElements) {
        ProducerGenerator producerGenerator = new ProducerGenerator(packageName, producerClassSimpleName, this.getClass(), processingEnv);
        logger.debug("Generating t9n producer: " + producerGenerator.getProducerClassFilePath());

        int i = 0;
        for (DictionaryElement dbe : dictionaryElements) {
            TypeName implTypeName = ClassName.bestGuess(packageName + '.' + dbe.getImplClassSimpleName());
            producerGenerator.addProduceAnnotation(implTypeName);

            String methodName = "get" + dbe.getOriginBean().getSimpleName() + i;
            TypeName retTypeName = TypeName.get(dbe.getOriginBean().asType());
            producerGenerator.addImplementMethod(methodName, retTypeName, implTypeName);
            i++;
        }

        producerGenerator.generate();
    }

    public void generate(DictionaryRegistry dictionaryRegistry) {
        for (Map.Entry<String, List<DictionaryElement>> entry : dictionaryRegistry.getByPackageMap().entrySet()) {
            String packageName = entry.getKey();
            List<DictionaryElement> dictElements = entry.getValue();
            if (getCodegenMode().isOptimized()) {
                generateIocProduccer(packageName, T9N_PRODUCER, dictElements);
            } else {
                for (DictionaryElement dictElm : dictElements) {
                    String t9nProducerName = dictElm.getOriginBean().getSimpleName() + T9N_PRODUCER;
                    generateIocProduccer(packageName, t9nProducerName, List.of(dictElm));
                }
            }
        }
    }

}
