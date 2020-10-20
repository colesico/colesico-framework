package colesico.framework.dslvalidator.codegen.generator;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.dslvalidator.codegen.model.ValidatedBeanElement;
import colesico.framework.dslvalidator.codegen.model.ValidatorBuilderPrototypeElement;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

public class ValidatorBuilderPrototypeGenerator {

    private final ProcessingEnvironment processingEnv;
    protected TypeSpec.Builder classBuilder;
    protected ValidatorBuilderPrototypeElement vbpElement;

    public ValidatorBuilderPrototypeGenerator(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    public void generate(ValidatedBeanElement vbElement) {
        for (ValidatorBuilderPrototypeElement vbpElement : vbElement.getBuilderPrototypes()) {

            this.vbpElement = vbpElement;

            this.classBuilder = TypeSpec.classBuilder(vbpElement.getTargetClassName());

            classBuilder.addModifiers(Modifier.PUBLIC);

            classBuilder.superclass(TypeName.get(vbpElement.getExtendsClass().unwrap()));

            CodegenUtils.createJavaFile(processingEnv, classBuilder.build(), vbpElement.getTargetPackageName(), vbpElement.getParentVB().getOriginClass().unwrap());

        }
    }
}
