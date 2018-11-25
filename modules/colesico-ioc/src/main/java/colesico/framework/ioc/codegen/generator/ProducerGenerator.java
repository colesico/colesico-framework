package colesico.framework.ioc.codegen.generator;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;
import colesico.framework.ioc.Rank;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
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

    private Logger logger = LoggerFactory.getLogger(ProducerGenerator.class);

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

    protected final List<AnnotationSpec.Builder> produceAnnotations = new ArrayList<>();
    protected final List<MethodSpec.Builder> produceMethods = new ArrayList<>();

    public ProducerGenerator(String packageName, String classSimpleName, Class<?> masterGeneratorClass, ProcessingEnvironment processingEnv) {
        this.masterGeneratorClass = masterGeneratorClass;
        this.processingEnv = processingEnv;
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();

        this.producerPackageName = packageName;
        this.producerClassSimpleName = classSimpleName;
        this.producerClassName = producerPackageName + '.' + producerClassSimpleName;
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

    public AnnotationSpec.Builder addProduceAnnotation(TypeName value) {
        AnnotationSpec.Builder produceAnn = AnnotationSpec.builder(Produce.class);
        produceAnn.addMember("value", "$T.class", value);
        produceAnnotations.add(produceAnn);
        return produceAnn;
    }

    public MethodSpec.Builder addProduceMethod(String methodName, TypeName returnType) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(methodName);
        mb.addModifiers(Modifier.PUBLIC);
        mb.returns(returnType);
        produceMethods.add(mb);
        return mb;
    }

    public MethodSpec.Builder addImplementationMethod(String methodName, TypeName returnType, TypeName implType) {
        MethodSpec.Builder mb = addProduceMethod(methodName, returnType);
        mb.addParameter(implType, "impl", Modifier.FINAL);
        mb.addStatement("return impl");
        return mb;
    }

    public TypeSpec.Builder typeBuilder() {
        TypeSpec.Builder producerBuilder = TypeSpec.classBuilder(producerClassSimpleName);
        producerBuilder.addModifiers(Modifier.PUBLIC);
        producerBuilder.addAnnotation(CodegenUtils.buildGenstampAnnotation(masterGeneratorClass.getName(), null,null));

        AnnotationSpec.Builder b = AnnotationSpec.builder(Producer.class);
        b.addMember("value", "$S", producerRank);
        producerBuilder.addAnnotation(b.build());

        for (AnnotationSpec.Builder annSpec : produceAnnotations) {
            producerBuilder.addAnnotation(annSpec.build());
        }

        for (MethodSpec.Builder mb : produceMethods) {
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
