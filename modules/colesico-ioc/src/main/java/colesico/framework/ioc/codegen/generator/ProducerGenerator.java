package colesico.framework.ioc.codegen.generator;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;
import colesico.framework.ioc.Rank;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.util.ArrayList;
import java.util.List;

public class ProducerGenerator {

    public static final String PRODUCER_CLASS_NAME_SUFFIX = "Producer";

    private final Logger logger = LoggerFactory.getLogger(ProducerGenerator.class);

    protected final Class<?> masterGeneratorClass;

    protected final ProcessingEnvironment processingEnv;
    protected final Elements elementUtils;
    protected final Types typeUtils;
    protected final Messager messager;
    protected final Filer filer;

    protected final String producerPackageName;
    protected final String producerClassSimpleName;
    protected final String producerClassName;
    protected final String producerClassFilePath;

    protected String producerRank = Rank.RANK_MINOR;

    protected final List<AnnotationSpec.Builder> producerAnnotations = new ArrayList<>();
    protected final List<MethodSpec.Builder> producerMethods = new ArrayList<>();
    protected final List<FieldSpec.Builder> producerFields = new ArrayList<>();

    public ProducerGenerator(String producerPackageName, String producerClassSimpleName, Class<?> masterGeneratorClass, ProcessingEnvironment processingEnv) {
        logger.debug("Creating IoC producer generator: " + producerPackageName + "." + producerClassSimpleName);
        this.masterGeneratorClass = masterGeneratorClass;
        this.processingEnv = processingEnv;
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();

        this.producerPackageName = producerPackageName;
        if (!producerClassSimpleName.endsWith(PRODUCER_CLASS_NAME_SUFFIX)) {
            producerClassSimpleName = producerClassSimpleName + "Producer";
        }
        this.producerClassSimpleName = producerClassSimpleName;
        this.producerClassName = this.producerPackageName + '.' + this.producerClassSimpleName;
        this.producerClassFilePath = "/" + StringUtils.replace(producerClassName, ".", "/") + ".java";

    }

    public String getProducerClassName() {
        return producerClassName;
    }

    public String getProducerClassFilePath() {
        return producerClassFilePath;
    }

    public void setProducerRank(String producerRank) {
        this.producerRank = producerRank;
    }

    public boolean isProducerExists() {
        try {
            FileObject producerFile = filer.getResource(StandardLocation.SOURCE_OUTPUT, producerPackageName, producerClassSimpleName + ".java");
            producerFile.openInputStream();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public AnnotationSpec.Builder addAnnotation(Class<?> type) {
        AnnotationSpec.Builder ab = AnnotationSpec.builder(type);
        producerAnnotations.add(ab);
        return ab;
    }

    public FieldSpec.Builder addField(String fieldName, TypeName fieldType, Modifier... modifiers) {
        FieldSpec.Builder fb = FieldSpec.builder(fieldType, fieldName, modifiers);
        producerFields.add(fb);
        return fb;
    }

    public MethodSpec.Builder addMethod(String methodName, Modifier... modifiers) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(methodName);
        mb.addModifiers(modifiers);
        producerMethods.add(mb);
        return mb;
    }


    public AnnotationSpec.Builder addProduceAnnotation(TypeName value) {
        AnnotationSpec.Builder produceAnn = AnnotationSpec.builder(Produce.class);
        produceAnn.addMember("value", "$T.class", value);
        producerAnnotations.add(produceAnn);
        return produceAnn;
    }

    public MethodSpec.Builder addProduceMethod(String methodName, TypeName returnType) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(methodName);
        mb.addModifiers(Modifier.PUBLIC);
        mb.returns(returnType);
        producerMethods.add(mb);
        return mb;
    }

    public MethodSpec.Builder addImplementMethod(String methodName, TypeName returnType, TypeName implType) {
        MethodSpec.Builder mb = addProduceMethod(methodName, returnType);
        mb.addParameter(implType, "impl", Modifier.FINAL);
        mb.addStatement("return impl");
        return mb;
    }

    public TypeSpec.Builder typeBuilder() {
        TypeSpec.Builder producerBuilder = TypeSpec.classBuilder(producerClassSimpleName);
        producerBuilder.addModifiers(Modifier.PUBLIC);
        producerBuilder.addAnnotation(CodegenUtils.generateGenstamp(masterGeneratorClass.getName(), null, null));

        AnnotationSpec.Builder b = AnnotationSpec.builder(Producer.class);
        b.addMember("value", "$S", producerRank);
        producerBuilder.addAnnotation(b.build());

        for (AnnotationSpec.Builder annSpec : producerAnnotations) {
            producerBuilder.addAnnotation(annSpec.build());
        }

        for (FieldSpec.Builder fb : producerFields) {
            producerBuilder.addField(fb.build());
        }

        for (MethodSpec.Builder mb : producerMethods) {
            producerBuilder.addMethod(mb.build());
        }

        return producerBuilder;
    }

    public void generate(Element... originatingElements) {
        TypeSpec.Builder tb = typeBuilder();
        final TypeSpec typeSpec = tb.build();
        CodegenUtils.createJavaFile(processingEnv, typeSpec, producerPackageName, originatingElements);
    }

}
