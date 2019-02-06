package colesico.framework.dao.codegen.generator;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.dao.DtoAssistant;
import colesico.framework.dao.codegen.model.ColumnElement;
import colesico.framework.dao.codegen.model.FieldColumnElement;
import colesico.framework.dao.codegen.model.TableElement;
import com.squareup.javapoet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import java.util.Map;

public class DtoAssistantGenerator {

    private static final Logger logger = LoggerFactory.getLogger(DtoAssistantGenerator.class);
    private final ProcessingEnvironment processingEnv;

    protected TableElement tableElement;
    protected TypeSpec.Builder classBuilder;

    public DtoAssistantGenerator(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    protected String getMapperClassName() {
        return tableElement.getOriginClass().getSimpleName().toString() + "Mapper";
    }

    protected void generateToMapMethod() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(DtoAssistant.TO_MAP_METOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);
        mb.addParameter(ParameterizedTypeName.get(ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(Object.class)),
                DtoAssistant.MAP_PARAM, Modifier.FINAL);
        mb.addParameter(TypeName.get(tableElement.getOriginClass().asType()), DtoAssistant.DTO_PARAM, Modifier.FINAL);

        for (ColumnElement columnElm : tableElement.getColumns()) {

            if (!(columnElm instanceof FieldColumnElement)){
                continue;
            }

            FieldColumnElement fieldColumnElm = (FieldColumnElement) columnElm;

            CodeBlock.Builder cb = CodeBlock.builder();
            cb.add("$N.put($S,$N", DtoAssistant.MAP_PARAM, columnElm.getName(), DtoAssistant.DTO_PARAM);

            cb.add(")");
            mb.addStatement(cb.build());
        }

        classBuilder.addMethod(mb.build());
    }

    public void generate(TableElement tableElement) {
        this.tableElement = tableElement;
        this.classBuilder = TypeSpec.classBuilder(getMapperClassName());

        TypeName interfaceTypeName = ParameterizedTypeName.get(ClassName.get(DtoAssistant.class),TypeName.get(tableElement.getOriginClass().asType()));
        classBuilder.addSuperinterface(interfaceTypeName);

        generateToMapMethod();

        String packageName = CodegenUtils.getPackageName(tableElement.getOriginClass());
        CodegenUtils.createJavaFile(processingEnv, classBuilder.build(), packageName, tableElement.getOriginClass());
    }
}
