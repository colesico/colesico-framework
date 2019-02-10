package colesico.framework.dao.codegen.generator;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.dao.DTOConverter;
import colesico.framework.dao.DTOHelper;
import colesico.framework.dao.DTOHelperFactory;
import colesico.framework.dao.codegen.model.ColumnElement;
import colesico.framework.dao.codegen.model.CompositionElement;
import colesico.framework.dao.codegen.model.DTOElement;

import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DTOHelperGenerator {

    private static final Logger logger = LoggerFactory.getLogger(DTOHelperGenerator.class);
    private final ProcessingEnvironment processingEnv;

    protected DTOElement dtoElement;
    protected TypeSpec.Builder classBuilder;

    public DTOHelperGenerator(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    protected String getHelperClassName() {
        return dtoElement.getOriginClass().getSimpleName().toString() + DTOHelperFactory.HELPER_CLASS_SUFFIX;
    }

    protected String toGetterName(VariableElement field) {
        String fieldName = field.getSimpleName().toString();
        return "get" + StrUtils.firstCharToUpperCase(fieldName) + "()";
    }

    protected String toSetterName(VariableElement field) {
        String fieldName = field.getSimpleName().toString();
        return "set" + StrUtils.firstCharToUpperCase(fieldName);
    }

    public String generateGettersChain(String rootVarName, CompositionElement composition, ColumnElement column) {
        Deque<VariableElement> fieldsStack = new ArrayDeque<>();
        if (column != null) {
            fieldsStack.push(column.getOriginField());
        }
        CompositionElement current = composition;
        while (current.getOriginalField() != null) {
            fieldsStack.push(current.getOriginalField());
            current = current.getParentComposition();
        }
        List<VariableElement> fieldsChain = new ArrayList<>();
        fieldsChain.addAll(fieldsStack);

        List<String> gettersChain = new ArrayList<>();
        if (StringUtils.isNotEmpty(rootVarName)) {
            gettersChain.add(rootVarName);
        }
        for (int i = 0; i < fieldsChain.size(); i++) {
            gettersChain.add(toGetterName(fieldsChain.get(i)));
        }

        return StringUtils.join(gettersChain, ".");
    }

    public String generateChackedGettersChain(String rootVarName, CompositionElement composition, ColumnElement column) {
        Deque<VariableElement> fieldsStack = new ArrayDeque<>();
        fieldsStack.push(column.getOriginField());
        CompositionElement current = composition;
        while (current.getOriginalField() != null) {
            fieldsStack.push(current.getOriginalField());
            current = current.getParentComposition();
        }
        List<VariableElement> fieldsChain = new ArrayList<>();
        fieldsChain.addAll(fieldsStack);

        String result = "";
        for (int n = 1; n <= fieldsChain.size(); n++) {
            List<String> gettersChain = new ArrayList<>();
            gettersChain.add(rootVarName);
            for (int i = 0; i < n; i++) {
                gettersChain.add(toGetterName(fieldsChain.get(i)));
            }
            result = result + StringUtils.join(gettersChain, ".");
            if (n < fieldsChain.size()) {
                result = result + " == null ? null : ";
            }
        }
        return result;
    }

    protected void generateCompositionToMap(CompositionElement composition, CodeBlock.Builder cb) {
        for (ColumnElement column : composition.getColumns()) {
            cb.add("$N.put($S, ", DTOHelper.MAP_PARAM, column.getName());

            String gettersPath = generateGettersChain(DTOHelper.DTO_PARAM, composition, column);
            if (column.getConverter() != null) {
                cb.add("new $T().$N(",
                        TypeName.get(column.getConverter()),
                        DTOConverter.FROM_FIELD_MATHOD);
                cb.add(gettersPath);
                cb.add(")");
            } else {
                cb.add(gettersPath);
            }
            cb.add(");\n");
        }

        for (CompositionElement subComposition : composition.getSubCompositions()) {
            generateCompositionToMap(subComposition, cb);
        }
    }

    protected void generateGetValue(ColumnElement column, CodeBlock.Builder cb) {

        if (column.getConverter() != null) {
            cb.add("new $T().$N($N.getObject($S))",
                    TypeName.get(column.getConverter()),
                    DTOConverter.TO_FIELD_MATHOD,
                    DTOHelper.RESULT_SET_PARAM, column.getName());
            return;
        }

        String fieldType = column.getOriginField().asType().toString();
        switch (fieldType) {
            case "java.lang.String":
                cb.add("$N.getString($S)", DTOHelper.RESULT_SET_PARAM, column.getName());
                break;
            case "java.lang.Character":
            case "char":
                cb.add("$N.getString($S).charAt(0)", DTOHelper.RESULT_SET_PARAM, column.getName());
                break;
            case "java.lang.Boolean":
                cb.add("$N.getBoolean($S)", DTOHelper.RESULT_SET_PARAM, column.getName());
                break;
            case "java.lang.Long":
            case "long":
                cb.add("$N.getLong($S)", DTOHelper.RESULT_SET_PARAM, column.getName());
                break;
            case "java.lang.Integer":
            case "int":
                cb.add("$N.getInt($S)", DTOHelper.RESULT_SET_PARAM, column.getName());
                break;
            case "java.lang.Short":
            case "short":
                cb.add("$N.getShort($S)", DTOHelper.RESULT_SET_PARAM, column.getName());
                break;
            case "java.lang.Byte":
            case "byte":
                cb.add("$N.getByte($S)", DTOHelper.RESULT_SET_PARAM, column.getName());
                break;
            case "java.lang.Double":
            case "double":
                cb.add("$N.getDouble($S)", DTOHelper.RESULT_SET_PARAM, column.getName());
                break;
            case "java.lang.Float":
            case "float":
                cb.add("$N.getFloat($S)", DTOHelper.RESULT_SET_PARAM, column.getName());
                break;
            case "java.math.BigDecimal":
                cb.add("$N.getBigDecimal($S)", DTOHelper.RESULT_SET_PARAM, column.getName());
                break;
            case "java.time.LocalDateTime":
                cb.add("$N.getTimestamp($S).toLocalDateTime()", DTOHelper.RESULT_SET_PARAM, column.getName());
                break;
            default:
                cb.add("$N.getObject($S)", DTOHelper.RESULT_SET_PARAM, column.getName());
        }
    }

    protected void generateCompositionFromResultSet(CompositionElement composition, CodeBlock.Builder cb) {
        for (ColumnElement column : composition.getColumns()) {
            cb.add(generateGettersChain(DTOHelper.DTO_PARAM, composition, null));
            cb.add('.' + toSetterName(column.getOriginField()));
            cb.add("(");
            generateGetValue(column, cb);
            cb.add(");\n");
        }

        for (CompositionElement subComposition : composition.getSubCompositions()) {
            generateCompositionFromResultSet(subComposition, cb);
        }
    }

    protected void generateInitCompositions(CompositionElement composition, CodeBlock.Builder cb) {
        for (CompositionElement subComposition : composition.getSubCompositions()) {
            cb.add("if (null == ");
            cb.add(generateGettersChain(DTOHelper.DTO_PARAM, subComposition, null));
            cb.add("){\n");
            cb.indent();
            cb.add(generateGettersChain(DTOHelper.DTO_PARAM, composition, null));
            cb.add("." + toSetterName(subComposition.getOriginalField()) + "(new $T());\n", TypeName.get(subComposition.getOriginType().asType()));
            cb.unindent();
            cb.add("}\n");
            generateInitCompositions(subComposition, cb);
        }
    }

    protected void generateInitCompositionsMethod() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(DTOHelper.INIT_COMPOSITION_METOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);
        mb.addParameter(TypeName.get(dtoElement.getOriginClass().asType()), DTOHelper.DTO_PARAM, Modifier.FINAL);

        CodeBlock.Builder cb = CodeBlock.builder();
        generateInitCompositions(dtoElement.getRootComposition(), cb);
        mb.addCode(cb.build());

        classBuilder.addMethod(mb.build());
    }


    protected void generateToMapMethod() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(DTOHelper.TO_MAP_METOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);
        mb.addParameter(TypeName.get(dtoElement.getOriginClass().asType()), DTOHelper.DTO_PARAM, Modifier.FINAL);
        mb.addParameter(ParameterizedTypeName.get(ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(Object.class)),
                DTOHelper.MAP_PARAM, Modifier.FINAL);

        mb.addStatement("$N($N)", DTOHelper.INIT_COMPOSITION_METOD, DTOHelper.DTO_PARAM);
        CodeBlock.Builder cb = CodeBlock.builder();
        generateCompositionToMap(dtoElement.getRootComposition(), cb);
        mb.addCode(cb.build());

        classBuilder.addMethod(mb.build());
    }


    protected void generatFromResultSetMethod() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(DTOHelper.FROM_RESULT_SET_METHOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);
        mb.addParameter(TypeName.get(dtoElement.getOriginClass().asType()), DTOHelper.DTO_PARAM, Modifier.FINAL);
        mb.addParameter(ClassName.get(ResultSet.class), DTOHelper.RESULT_SET_PARAM, Modifier.FINAL);
        mb.addException(ClassName.get(SQLException.class));
        mb.addStatement("$N($N)", DTOHelper.INIT_COMPOSITION_METOD, DTOHelper.DTO_PARAM);
        CodeBlock.Builder cb = CodeBlock.builder();
        generateCompositionFromResultSet(dtoElement.getRootComposition(), cb);
        mb.addCode(cb.build());

        classBuilder.addMethod(mb.build());
    }


    protected String generateInsertSQL(){
        List<ColumnElement> allColumns = dtoElement.getAllColumns();
        StringBuilder sb = new StringBuilder("INSERT INTO [TABLE](");
        List<String> columnNames = new ArrayList<>();
        List<String> columnValues = new ArrayList<>();
        for (ColumnElement column:allColumns){
            columnNames.add(column.getName());
            columnValues.add(":"+column.getName());
        }
        sb.append(StringUtils.join(columnNames,", ")).append(')');
        sb.append(" VALUES (").append(StringUtils.join(columnValues,',')).append(")\n");
        return sb.toString();
    }

    protected String generateUpdateSQL(){
        List<ColumnElement> allColumns = dtoElement.getAllColumns();
        StringBuilder sb = new StringBuilder("UPDATE [TABLE] SET ");
        List<String> assigns = new ArrayList<>();
        for (ColumnElement column:allColumns){
            assigns.add(column.getName()+" = :"+column.getName());
        }
        sb.append(StringUtils.join(assigns,", ")).append("\n");
        return sb.toString();
    }

    protected String generateSelectSQL(){
        List<ColumnElement> allColumns = dtoElement.getAllColumns();
        StringBuilder sb = new StringBuilder("SELECT ");
        List<String> columnNames = new ArrayList<>();
        for (ColumnElement column:allColumns){
            columnNames.add(column.getName());
        }
        sb.append(StringUtils.join(columnNames,", "));
        sb.append(" FROM [TABLE] WHERE [CONDITION]\n");
        return sb.toString();
    }

    public void generate(DTOElement dtoElement) {
        this.dtoElement = dtoElement;
        this.classBuilder = TypeSpec.classBuilder(getHelperClassName());

        classBuilder.addModifiers(Modifier.PUBLIC);
        TypeName interfaceTypeName = ParameterizedTypeName.get(ClassName.get(DTOHelper.class), TypeName.get(dtoElement.getOriginClass().asType()));
        classBuilder.addSuperinterface(interfaceTypeName);

        generateInitCompositionsMethod();
        generateToMapMethod();
        generatFromResultSetMethod();
        generateInsertSQL();

        classBuilder.addJavadoc( generateInsertSQL()+generateUpdateSQL()+generateSelectSQL());

        String packageName = CodegenUtils.getPackageName(dtoElement.getOriginClass());
        CodegenUtils.createJavaFile(processingEnv, classBuilder.build(), packageName, dtoElement.getOriginClass());
    }
}
