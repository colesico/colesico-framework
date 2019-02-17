package colesico.framework.translation.codegen.generator;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.model.AnnotationElement;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.assist.codegen.model.ParameterElement;
import colesico.framework.assist.codegen.model.ParserElement;
import colesico.framework.translation.AbstractDictionary;
import colesico.framework.translation.TranslationKey;
import colesico.framework.translation.TranslationKit;
import colesico.framework.translation.codegen.model.DictionaryElement;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import java.util.List;

public class DictionaryGenerator {

    public static final String BASE_PATH_FIELD = "BASE_PATH";

    protected Logger logger = LoggerFactory.getLogger(DictionaryGenerator.class);

    protected ProcessingEnvironment processingEnv;

    public DictionaryGenerator(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    protected void generateConstructor(TypeSpec.Builder dictionaryBuilder, DictionaryElement dictionaryElement) {
        MethodSpec.Builder cob = MethodSpec.constructorBuilder();
        cob.addAnnotation(Inject.class);
        cob.addModifiers(Modifier.PUBLIC);
        cob.addParameter(ClassName.get(TranslationKit.class), AbstractDictionary.TRANSLATION_KIT_FIELD);
        cob.addStatement("super($N,$S)", AbstractDictionary.TRANSLATION_KIT_FIELD, dictionaryElement.getBasePath());
        dictionaryBuilder.addMethod(cob.build());
    }

    protected MethodSpec generateProxyMethod(DictionaryElement dictionaryElement, MethodElement keyMethod) {
        logger.debug("Generate dictionary " + dictionaryElement + " proxy method: " + keyMethod);
        MethodSpec.Builder mb = CodegenUtils.createProxyMethodBuilder(keyMethod,null,null,true);


        mb.addModifiers(Modifier.PUBLIC);
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("return $N(", AbstractDictionary.TRANSLATE_OR_KEY_METHOD);

        String t9nKey;
        AnnotationElement<TranslationKey> t9nKeyAnn = keyMethod.getAnnotation(TranslationKey.class);
        if (t9nKeyAnn != null) {
            t9nKey = t9nKeyAnn.unwrap().value();
        } else {
            //t9nKey = StrUtils.firstCharToUpperCase(keyMethod.getSimpleName().toString());
            t9nKey = keyMethod.getName();
        }

        cb.add("$S", t9nKey);
        // translation params
        List<ParameterElement> params = keyMethod.getParameters();
        if (!params.isEmpty()) {
            for (ParameterElement param : params) {
                cb.add(",$N", param.getName());
            }
        }
        cb.add(")");

        mb.addStatement(cb.build());
        return mb.build();
    }

    public void generate(DictionaryElement dictionaryElement) {
        String beanClassName = dictionaryElement.getImplClassSimpleName();
        logger.debug("Generate  dictionary bean: " + beanClassName);
        TypeSpec.Builder dictionaryBuilder = TypeSpec.classBuilder(beanClassName);
        dictionaryBuilder.addSuperinterface(TypeName.get(dictionaryElement.getOriginBean().asType()));
        dictionaryBuilder.superclass(ClassName.get(AbstractDictionary.class));

        AnnotationSpec genstamp = CodegenUtils.generateGenstamp(this.getClass().getName(), null, "Origin: " + dictionaryElement.getOriginBean().asType().toString());
        dictionaryBuilder.addAnnotation(genstamp);

        dictionaryBuilder.addAnnotation(Singleton.class);

        dictionaryBuilder.addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        generateConstructor(dictionaryBuilder, dictionaryElement);

        for (MethodElement keyMethod : dictionaryElement.getKeyMethods()) {
            MethodSpec ms = generateProxyMethod(dictionaryElement, keyMethod);
            dictionaryBuilder.addMethod(ms);
        }

        try {
            final TypeSpec typeSpec = dictionaryBuilder.build();
            // Create class source file
            Element[] linkedElm = new Element[]{dictionaryElement.getOriginBean().unwrap()};
            String packageName = dictionaryElement.getOriginBean().getPackageName();
            CodegenUtils.createJavaFile(processingEnv, typeSpec, packageName, linkedElm);
        } catch (Exception e) {
            logger.debug("Error generating dictionary bean:" + ExceptionUtils.getRootCauseMessage(e));
            throw e;
        }
    }
}
