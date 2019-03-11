package colesico.framework.jdbirec.codegen.generator;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.jdbirec.FieldConverter;
import colesico.framework.jdbirec.RecordKit;
import colesico.framework.jdbirec.RecordKitFactory;
import colesico.framework.jdbirec.codegen.model.ColumnElement;
import colesico.framework.jdbirec.codegen.model.CompositionElement;
import colesico.framework.jdbirec.codegen.model.RecordElement;

import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;

public class RecordKitGenerator {

    private static final Logger logger = LoggerFactory.getLogger(RecordKitGenerator.class);
    private final ProcessingEnvironment processingEnv;

    protected RecordElement recordElement;
    protected TypeSpec.Builder classBuilder;

    // Converter fields
    protected KitFields converterFields;

    public RecordKitGenerator(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    protected String getHelperClassName() {
        return recordElement.getOriginClass().getSimpleName().toString() + RecordKitFactory.KIT_CLASS_SUFFIX;
    }

    protected String toGetterName(FieldElement field) {
        String fieldName = field.getName();
        return "get" + StrUtils.firstCharToUpperCase(fieldName) + "()";
    }

    protected String toSetterName(FieldElement field) {
        String fieldName = field.getName();
        return "set" + StrUtils.firstCharToUpperCase(fieldName);
    }

    public void generateConverterFields() {
        for (Map.Entry<TypeMirror, String> f : converterFields.getFieldsMap().entrySet()) {
            FieldSpec.Builder fb = FieldSpec.builder(TypeName.get(f.getKey()), f.getValue(), Modifier.FINAL);
            classBuilder.addField(fb.build());
        }
    }

    public void generateConstructor() {
        MethodSpec.Builder mb = MethodSpec.constructorBuilder();
        mb.addModifiers(Modifier.PUBLIC);
        for (Map.Entry<TypeMirror, String> f : converterFields.getFieldsMap().entrySet()) {
            mb.addStatement("this.$N= new $T()", f.getValue(), TypeName.get(f.getKey()));
        }
        classBuilder.addMethod(mb.build());
    }

    public void generateNewRecord() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(RecordKit.NEW_RECORD_METHOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);
        mb.returns(TypeName.get(recordElement.getOriginClass().asType()));
        mb.addStatement("return new $T()", TypeName.get(recordElement.getOriginClass().asType()));
        classBuilder.addMethod(mb.build());
    }

    public String generateChain(String rootVarName, CompositionElement composition, ColumnElement column, Function<FieldElement, String> fieldTransformer) {
        Deque<FieldElement> fieldsStack = new ArrayDeque<>();
        if (column != null) {
            fieldsStack.push(column.getOriginField());
        }
        CompositionElement current = composition;
        while (current.getOriginField() != null) {
            fieldsStack.push(current.getOriginField());
            current = current.getParentComposition();
        }
        List<FieldElement> fieldsChain = new ArrayList<>();
        fieldsChain.addAll(fieldsStack);

        List<String> gettersChain = new ArrayList<>();
        if (StringUtils.isNotEmpty(rootVarName)) {
            gettersChain.add(rootVarName);
        }
        for (int i = 0; i < fieldsChain.size(); i++) {
            gettersChain.add(fieldTransformer.apply(fieldsChain.get(i)));
        }

        return StringUtils.join(gettersChain, ".");
    }

    protected void generateCompositionToMap(CompositionElement composition, CodeBlock.Builder cb) {
        for (ColumnElement column : composition.getColumns()) {
            String paramName = generateChain(null, column.getParentComposition(), column, f -> f.getName());
            // receiver.receive("column"
            cb.add("$N.$N($S, ", RecordKit.RECEIVER_PARAM, RecordKit.ValueReceiver.RECEIVE_METHOD, paramName);

            String gettersPath = generateChain(RecordKit.RECORD_PARAM, composition, column, this::toGetterName);
            if (column.getConverter() != null) {
                String convField = converterFields.addField(column.getConverter().unwrap());
                cb.add("$N.$N(",
                        convField,
                        FieldConverter.FROM_FIELD_METHOD);
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
            String convField = converterFields.addField(column.getConverter().unwrap());
            cb.add("$N.$N($S,$N)",
                    convField,
                    FieldConverter.TO_FIELD_METHOD,
                    column.getName(),
                    RecordKit.RESULT_SET_PARAM);
            return;
        }

        String fieldType = column.getOriginField().asType().toString();
        switch (fieldType) {
            case "java.lang.String":
                cb.add("$N.getString($S)", RecordKit.RESULT_SET_PARAM, column.getName());
                break;
            case "java.lang.Character":
            case "char":
                cb.add("$N.getString($S).charAt(0)", RecordKit.RESULT_SET_PARAM, column.getName());
                break;
            case "java.lang.Boolean":
                cb.add("$N.getBoolean($S)", RecordKit.RESULT_SET_PARAM, column.getName());
                break;
            case "java.lang.Long":
            case "long":
                cb.add("$N.getLong($S)", RecordKit.RESULT_SET_PARAM, column.getName());
                break;
            case "java.lang.Integer":
            case "int":
                cb.add("$N.getInt($S)", RecordKit.RESULT_SET_PARAM, column.getName());
                break;
            case "java.lang.Short":
            case "short":
                cb.add("$N.getShort($S)", RecordKit.RESULT_SET_PARAM, column.getName());
                break;
            case "java.lang.Byte":
            case "byte":
                cb.add("$N.getByte($S)", RecordKit.RESULT_SET_PARAM, column.getName());
                break;
            case "java.lang.Double":
            case "double":
                cb.add("$N.getDouble($S)", RecordKit.RESULT_SET_PARAM, column.getName());
                break;
            case "java.lang.Float":
            case "float":
                cb.add("$N.getFloat($S)", RecordKit.RESULT_SET_PARAM, column.getName());
                break;
            case "java.math.BigDecimal":
                cb.add("$N.getBigDecimal($S)", RecordKit.RESULT_SET_PARAM, column.getName());
                break;
            case "java.time.LocalDateTime":
                cb.add("$N.getTimestamp($S) == null ? null : $N.getTimestamp($S).toLocalDateTime()",
                        RecordKit.RESULT_SET_PARAM, column.getName(),
                        RecordKit.RESULT_SET_PARAM, column.getName()
                );
                break;
            default:
                cb.add("$N.getObject($S)", RecordKit.RESULT_SET_PARAM, column.getName());
        }
    }

    protected void generateCompositionFromResultSet(CompositionElement composition, CodeBlock.Builder cb) {
        for (ColumnElement column : composition.getColumns()) {
            cb.add(generateChain(RecordKit.RECORD_PARAM, composition, null, this::toGetterName));
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
            cb.add(generateChain(RecordKit.RECORD_PARAM, subComposition, null, this::toGetterName));
            cb.add("){\n");
            cb.indent();
            cb.add(generateChain(RecordKit.RECORD_PARAM, composition, null, this::toGetterName));
            cb.add("." + toSetterName(subComposition.getOriginField()) + "(new $T());\n", TypeName.get(subComposition.getOriginClass().asType()));
            cb.unindent();
            cb.add("}\n");
            generateInitCompositions(subComposition, cb);
        }
    }

    protected void generateInitCompositionsMethod() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(RecordKit.INIT_COMPOSITION_METHOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);
        mb.addParameter(TypeName.get(recordElement.getOriginClass().asType()), RecordKit.RECORD_PARAM, Modifier.FINAL);

        CodeBlock.Builder cb = CodeBlock.builder();
        generateInitCompositions(recordElement.getRootComposition(), cb);
        mb.addCode(cb.build());

        classBuilder.addMethod(mb.build());
    }


    protected void generateExportMethod() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(RecordKit.EXPORT_METOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);
        mb.addParameter(TypeName.get(recordElement.getOriginClass().asType()), RecordKit.RECORD_PARAM, Modifier.FINAL);
        mb.addParameter(TypeName.get(RecordKit.ValueReceiver.class),
                RecordKit.RECEIVER_PARAM, Modifier.FINAL);

        mb.addStatement("$N($N)", RecordKit.INIT_COMPOSITION_METHOD, RecordKit.RECORD_PARAM);
        CodeBlock.Builder cb = CodeBlock.builder();
        generateCompositionToMap(recordElement.getRootComposition(), cb);
        mb.addCode(cb.build());

        classBuilder.addMethod(mb.build());
    }


    protected void generatImportMethod() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(RecordKit.IMPORT_METHOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);
        mb.returns(TypeName.get(recordElement.getOriginClass().asType()));
        mb.addParameter(TypeName.get(recordElement.getOriginClass().asType()), RecordKit.RECORD_PARAM, Modifier.FINAL);
        mb.addParameter(ClassName.get(ResultSet.class), RecordKit.RESULT_SET_PARAM, Modifier.FINAL);
        mb.addException(ClassName.get(SQLException.class));
        mb.addStatement("$N($N)", RecordKit.INIT_COMPOSITION_METHOD, RecordKit.RECORD_PARAM);
        CodeBlock.Builder cb = CodeBlock.builder();
        generateCompositionFromResultSet(recordElement.getRootComposition(), cb);
        mb.addCode(cb.build());

        mb.addStatement("return $N", RecordKit.RECORD_PARAM);
        classBuilder.addMethod(mb.build());
    }


    protected void generateInsertSQL() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(RecordKit.SQL_INSERT_METHOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);
        mb.returns(ClassName.get(String.class));

        List<ColumnElement> allColumns = recordElement.getAllColumns();
        StringBuilder sb = new StringBuilder("INSERT INTO " + getTableName() + " (");
        List<String> columnNames = new ArrayList<>();
        List<String> columnValues = new ArrayList<>();
        for (ColumnElement column : allColumns) {
            if (column.getInsertAs()==null){
                continue;
            }

            columnNames.add(column.getName());
            if (column.getInsertAs().equals("@field")){
                String paramName = generateChain(null, column.getParentComposition(), column, f -> f.getName());
                columnValues.add(":" + paramName);
            } else {
                columnValues.add(column.getInsertAs());
            }
        }
        sb.append(StringUtils.join(columnNames, ", ")).append(')');
        sb.append(" VALUES (").append(StringUtils.join(columnValues, ',')).append(")");

        mb.addStatement("return $S", sb.toString());
        classBuilder.addMethod(mb.build());
    }

    protected void generateUpdateSQL() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(RecordKit.SQL_UPDATE_METHOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);
        mb.returns(ClassName.get(String.class));
        mb.addParameter(ClassName.get(String.class), RecordKit.CONDITIONS_PARAM, Modifier.FINAL);

        List<ColumnElement> allColumns = recordElement.getAllColumns();
        StringBuilder sb = new StringBuilder("UPDATE " + getTableName() + " SET ");
        List<String> assigns = new ArrayList<>();
        for (ColumnElement column : allColumns) {
            if (column.getUpdateAs() == null) {
                continue;
            }

            if (column.getUpdateAs().equals("@field")){
                String paramName = generateChain(null, column.getParentComposition(), column, f -> f.getName());
                assigns.add(column.getName() + " = :" + paramName);
            } else {
                assigns.add(column.getName() + " = " + column.getUpdateAs());
            }
        }
        sb.append(StringUtils.join(assigns, ", "));

        mb.addStatement("return $S + ($T.isBlank($N)?\"\" : \" \"+$N)", sb.toString(),
                ClassName.get(StringUtils.class), RecordKit.CONDITIONS_PARAM, RecordKit.CONDITIONS_PARAM);
        classBuilder.addMethod(mb.build());
    }

    protected String generateSelectSQL() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(RecordKit.SQL_SELECT_METHOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);
        mb.returns(ClassName.get(String.class));
        mb.addParameter(ClassName.get(String.class), RecordKit.CONDITIONS_PARAM, Modifier.FINAL);


        List<ColumnElement> allColumns = recordElement.getAllColumns();
        StringBuilder sb = new StringBuilder("SELECT ");
        List<String> columnNames = new ArrayList<>();
        for (ColumnElement column : allColumns) {
            if (column.getSelectAs() == null){
                continue;
            }
            if (column.getSelectAs().equals("@column")) {
                columnNames.add(column.getName());
            } else {
                columnNames.add(column.getSelectAs());
            }
        }
        sb.append(StringUtils.join(columnNames, ", "));
        sb.append(" FROM " + getTableName());

        mb.addStatement("return $S + ($T.isBlank($N)?\"\" : \" \"+$N)", sb.toString(),
                ClassName.get(StringUtils.class), RecordKit.CONDITIONS_PARAM, RecordKit.CONDITIONS_PARAM);
        classBuilder.addMethod(mb.build());
        return sb.toString();
    }

    protected String generateCreateTableSQL() {
        List<ColumnElement> allColumns = recordElement.getAllColumns();
        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        sb.append(getTableName()).append("(");

        List<String> columnNames = new ArrayList<>();
        for (ColumnElement column : allColumns) {
            String definition = column.getDefinition();
            if (definition == null) {
                definition = "[DEFINITION]";
            }
            columnNames.add(column.getName() + " " + definition);
        }
        sb.append(StringUtils.join(columnNames, ", "));
        sb.append(")\n");
        return sb.toString();
    }

    protected String getTableName() {
        if (StringUtils.isEmpty(recordElement.getTableName())) {
            return "[TABLE]";
        } else {
            return recordElement.getTableName();
        }
    }

    public void generate(RecordElement recordElement) {
        this.converterFields = new KitFields("conv");
        this.recordElement = recordElement;
        this.classBuilder = TypeSpec.classBuilder(getHelperClassName());

        classBuilder.addModifiers(Modifier.PUBLIC);

        TypeName baseTypeName = ParameterizedTypeName.get(
                ClassName.bestGuess(recordElement.getExtend().asClassElement().getName()),
                TypeName.get(recordElement.getOriginClass().asType()));

        classBuilder.superclass(baseTypeName);

        generateInitCompositionsMethod();
        generateExportMethod();
        generatImportMethod();
        generateInsertSQL();
        generateUpdateSQL();
        generateSelectSQL();

        classBuilder.addJavadoc(generateCreateTableSQL());

        generateNewRecord();
        generateConverterFields();
        generateConstructor();

        String packageName = recordElement.getOriginClass().getPackageName();
        CodegenUtils.createJavaFile(processingEnv, classBuilder.build(), packageName, recordElement.getOriginClass().unwrap());
    }
}
