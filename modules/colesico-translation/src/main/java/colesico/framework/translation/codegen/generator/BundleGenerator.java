package colesico.framework.translation.codegen.generator;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.resource.assist.FileParser;
import colesico.framework.translation.codegen.model.DictionaryElement;
import colesico.framework.translation.codegen.model.BundleElement;
import colesico.framework.translation.codegen.model.TranslationElement;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public class BundleGenerator {

    private Logger logger = LoggerFactory.getLogger(IocGenerator.class);

    protected final ProcessingEnvironment processingEnv;

    public BundleGenerator(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    protected void generateDictionary(String packageName, BundleElement dictionaryElement) {
        FileParser fp = new FileParser(dictionaryElement.getParentDictionary().getBasePath());
        String filePath = fp.path();

        String fileName;
        if (StringUtils.isEmpty(dictionaryElement.getLocaleKey())) {
            fileName = fp.fileName() + ".properties";
        } else {
            fileName = fp.fileName() + '_' + dictionaryElement.getLocaleKey() + ".properties";
        }

        String fullPath = filePath + '/' + fileName;
        String pkgName = filePath.replace('/','.');
        try {
            final FileObject fileObj = processingEnv.getFiler().createResource(
                    StandardLocation.SOURCE_OUTPUT,
                    pkgName,
                    fileName);

            try (final Writer writer = new BufferedWriter(fileObj.openWriter())) {
                writer.write("# This is automatically generated dictionary from "+dictionaryElement.getParentDictionary().getOriginBean().asType().toString()+"\n");
                for (TranslationElement te : dictionaryElement.getTranslations()) {
                    String val = te.getTranslation();
                    val = StringUtils.replaceAll(val,"\\n","\\\\n\\\\\n");
                    String line = te.getTranslationKey() + '=' + val + "\n";
                    writer.append(line);
                }
            }
        } catch (IOException ex) {
            String errMsg = MessageFormat.format("Error creating properties file: {0}; Cause message: {1}", fullPath, ExceptionUtils.getRootCauseMessage(ex));
            logger.error(errMsg);
            throw CodegenException.of().message(errMsg).build();
        }
    }

    public void generate(String packageName, List<DictionaryElement> dictionaryElements) {
        for (DictionaryElement dbe : dictionaryElements) {
            for (Map.Entry<String, BundleElement> dictEntry : dbe.getBundlesByLocale().entrySet()) {
                BundleElement dictElm = dictEntry.getValue();
                generateDictionary(packageName, dictElm);
            }
        }
    }
}
