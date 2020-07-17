/*
 * Copyright © 2014-2020 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.jdbirec.codegen.generator;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.ArrayCodegen;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.jdbirec.FieldMediator;
import colesico.framework.jdbirec.RecordKit;
import colesico.framework.jdbirec.RecordKitFactory;
import colesico.framework.jdbirec.RecordView;
import colesico.framework.jdbirec.codegen.model.ColumnElement;
import colesico.framework.jdbirec.codegen.model.CompositionElement;
import colesico.framework.jdbirec.codegen.model.RecordElement;
import colesico.framework.jdbirec.codegen.model.ViewSetElement;
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

    // Mediator fields
    protected KitFields mediatorFields;

    protected VarNames varNames;

    public RecordKitGenerator(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    protected String getHelperClassName() {
        if (RecordView.DEFAULT_VIEW.equals(recordElement.getView())) {
            return recordElement.getOriginClass().getSimpleName() + RecordKitFactory.KIT_CLASS_SUFFIX;
        } else {
            String viewPart = StrUtils.firstCharToUpperCase(recordElement.getView());
            return recordElement.getOriginClass().getSimpleName() + viewPart + RecordKitFactory.KIT_CLASS_SUFFIX;
        }
    }

    protected String toGetterFunction(FieldElement field) {
        return toGetterName(field) + "()";
    }

    protected String toGetterName(FieldElement field) {
        String fieldName = field.getName();
        return "get" + StrUtils.firstCharToUpperCase(fieldName);
    }

    protected String toSetterName(FieldElement field) {
        String fieldName = field.getName();
        return "set" + StrUtils.firstCharToUpperCase(fieldName);
    }

    public void generateMediatorFields() {
        for (Map.Entry<TypeMirror, String> f : mediatorFields.getFieldsMap().entrySet()) {
            FieldSpec.Builder fb = FieldSpec.builder(TypeName.get(f.getKey()), f.getValue(), Modifier.FINAL);
            classBuilder.addField(fb.build());
        }
    }

    public void generateConstructor() {
        MethodSpec.Builder mb = MethodSpec.constructorBuilder();
        mb.addModifiers(Modifier.PUBLIC);
        for (Map.Entry<TypeMirror, String> f : mediatorFields.getFieldsMap().entrySet()) {
            mb.addStatement("this.$N = new $T()", f.getValue(), TypeName.get(f.getKey()));
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
        List<FieldElement> fieldsChain = new ArrayList<>(fieldsStack);

        List<String> gettersChain = new ArrayList<>();
        if (StringUtils.isNotEmpty(rootVarName)) {
            gettersChain.add(rootVarName);
        }
        for (FieldElement fl : fieldsChain) {
            gettersChain.add(fieldTransformer.apply(fl));
        }

        return StringUtils.join(gettersChain, ".");
    }

    protected void generateCompositionToMap(CompositionElement composition, String parentCompositionVar, CodeBlock.Builder cb) {
        String compositionVar;
        if (parentCompositionVar == null) {
            compositionVar = RecordKit.RECORD_PARAM;
        } else {
            compositionVar = varNames.getNextVarName(composition.getOriginField().getName());
            // SubCompositionType comp=parentComp.getSubComposition()
            cb.add("\n");
            cb.add("// Composition: " + composition.getOriginClass().getName() + "\n\n");
            cb.addStatement("$T $N = $N.$N()",
                    TypeName.get(composition.getOriginClass().asType()),
                    compositionVar,
                    parentCompositionVar,
                    toGetterName(composition.getOriginField()));
            cb.add("if ($N == null) { $N = new $T(); }\n", compositionVar, compositionVar, TypeName.get(composition.getOriginClass().asType()));
        }

        for (ColumnElement column : composition.getColumns()) {
            if (!column.isExportable()) {
                continue;
            }
            String paramName = generateChain(null, column.getParentComposition(), column, FieldElement::getName);
            String fieldGetterName = toGetterName(column.getOriginField());
            if (column.getMediator() == null) {
                // fr.receive("column",comp.getField1())
                cb.add("$N.$N($S, ", RecordKit.FIELD_RECEIVER_PARAM, RecordKit.FieldReceiver.SET_METHOD, paramName);
                cb.add("$N.$N()", compositionVar, fieldGetterName);
                cb.add(");\n");
            } else {
                // mediator.exportField(comp.getField1()
                String mediatorField = mediatorFields.addField(column.getMediator().unwrap());
                cb.add("$N.$N(", mediatorField, FieldMediator.EXPORT_METHOD);
                cb.add("$N.$N()", compositionVar, fieldGetterName);
                cb.add(",$S,$N);\n", paramName, RecordKit.FIELD_RECEIVER_PARAM);
            }
        }

        for (CompositionElement subComposition : composition.getSubCompositions()) {
            generateCompositionToMap(subComposition, compositionVar, cb);
        }
    }

    private String getSelectColumnName(ColumnElement column) {
        return column.getName();
        //return StringUtils.isBlank(column.getTableName()) ? column.getName() : column.getTableName() + '.' + column.getName();
    }

    protected void generateGetValue(ColumnElement column, CodeBlock.Builder cb) {

        if (column.getMediator() != null) {
            String mediatorField = mediatorFields.addField(column.getMediator().unwrap());
            cb.add("$N.$N($S,$N)",
                    mediatorField,
                    FieldMediator.IMPORT_METHOD,
                    getSelectColumnName(column),
                    RecordKit.RESULT_SET_PARAM);
            return;
        }

        String fieldType = column.getOriginField().asType().toString();
        switch (fieldType) {
            case "char":
                cb.add("$N.getChar($S)", RecordKit.RESULT_SET_PARAM, getSelectColumnName(column));
                break;
            case "boolean":
                cb.add("$N.getBoolean($S)", RecordKit.RESULT_SET_PARAM, getSelectColumnName(column));
                break;
            case "long":
                cb.add("$N.getLong($S)", RecordKit.RESULT_SET_PARAM, getSelectColumnName(column));
                break;
            case "int":
                cb.add("$N.getInt($S)", RecordKit.RESULT_SET_PARAM, getSelectColumnName(column));
                break;
            case "short":
                cb.add("$N.getShort($S)", RecordKit.RESULT_SET_PARAM, getSelectColumnName(column));
                break;
            case "byte":
                cb.add("$N.getByte($S)", RecordKit.RESULT_SET_PARAM, getSelectColumnName(column));
                break;
            case "double":
                cb.add("$N.getDouble($S)", RecordKit.RESULT_SET_PARAM, getSelectColumnName(column));
                break;
            case "float":
                cb.add("$N.getFloat($S)", RecordKit.RESULT_SET_PARAM, getSelectColumnName(column));
                break;
            case "java.lang.String":
                cb.add("$N.getString($S)", RecordKit.RESULT_SET_PARAM, getSelectColumnName(column));
                break;
            case "java.math.BigDecimal":
                cb.add("$N.getBigDecimal($S)", RecordKit.RESULT_SET_PARAM, getSelectColumnName(column));
                break;
            case "java.time.LocalDateTime":
                cb.add("$N.getTimestamp($S) == null ? null : $N.getTimestamp($S).toLocalDateTime()",
                        RecordKit.RESULT_SET_PARAM, getSelectColumnName(column),
                        RecordKit.RESULT_SET_PARAM, getSelectColumnName(column)
                );
                break;
            case "java.time.Instant":
                cb.add("$N.getTimestamp($S) == null ? null : $N.getTimestamp($S).toInstant()",
                        RecordKit.RESULT_SET_PARAM, getSelectColumnName(column),
                        RecordKit.RESULT_SET_PARAM, getSelectColumnName(column)
                );
                break;

            case "java.lang.Boolean":
            case "java.lang.Long":
            case "java.lang.Integer":
            case "java.lang.Short":
            case "java.lang.Byte":
            case "java.lang.Double":
            case "java.lang.Float":
            case "java.lang.Character":
                cb.add("$N.getObject($S,$T.class)", RecordKit.RESULT_SET_PARAM, getSelectColumnName(column), TypeName.get(column.getOriginField().asType()));
                break;

            default:
                cb.add("$N.getObject($S,$T.class)", RecordKit.RESULT_SET_PARAM, getSelectColumnName(column), TypeName.get(column.getOriginField().asType()));
        }
    }

    protected void generateCompositionFromResultSet(CompositionElement composition, String parentCompositionVar, CodeBlock.Builder cb) {
        CodeBlock.Builder cbo = CodeBlock.builder();
        String compositionVar;
        if (parentCompositionVar == null) {
            compositionVar = RecordKit.RECORD_PARAM;
        } else {
            cbo.add("\n");
            cbo.add("// Composition: " + composition.getOriginClass().getName());
            cbo.add("\n\n");
            compositionVar = varNames.getNextVarName(composition.getOriginField().getName());
            // SubCompositionType comp=parentComp.getSubComposition()
            cbo.addStatement("$T $N = $N.$N()",
                    TypeName.get(composition.getOriginClass().asType()),
                    compositionVar,
                    parentCompositionVar,
                    toGetterName(composition.getOriginField()));
            // if (comp == null )
            cbo.add("if ($N == null) {\n", compositionVar);
            cbo.indent();
            // comp = new SubCompositionType();
            cbo.addStatement("$N = new $T()", compositionVar, TypeName.get(composition.getOriginClass().asType()));
            // parentComp.setSubComposition(comp)
            cbo.addStatement("$N.$N($N)", parentCompositionVar, toSetterName(composition.getOriginField()), compositionVar);
            cbo.unindent();
            cbo.add("}\n");
        }

        for (ColumnElement column : composition.getColumns()) {
            if (!column.isImportable()) {
                continue;
            }
            // comp.setField(
            cbo.add("$N.$N", compositionVar, toSetterName(column.getOriginField()));
            cbo.add("(");
            generateGetValue(column, cbo);
            cbo.add(");\n");
        }

        for (CompositionElement subComposition : composition.getSubCompositions()) {
            generateCompositionFromResultSet(subComposition, compositionVar, cbo);
        }

        if (composition.getKeyColumn() == null) {
            cb.add(cbo.build());
        } else {
            cb.add("if ($N.getObject($S) != null) {\n", RecordKit.RESULT_SET_PARAM, composition.getKeyColumn());
            cb.indent();
            cb.add(cbo.build());
            cb.unindent();
            cb.add("}\n");
        }
    }

    protected void generateExportMethod() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(RecordKit.EXPORT_METOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);
        mb.addParameter(TypeName.get(recordElement.getOriginClass().asType()), RecordKit.RECORD_PARAM, Modifier.FINAL);
        mb.addParameter(TypeName.get(RecordKit.FieldReceiver.class),
                RecordKit.FIELD_RECEIVER_PARAM, Modifier.FINAL);

        CodeBlock.Builder cb = CodeBlock.builder();
        varNames = new VarNames();
        generateCompositionToMap(recordElement.getRootComposition(), null, cb);
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

        varNames = new VarNames();

        CodeBlock.Builder cb = CodeBlock.builder();
        generateCompositionFromResultSet(recordElement.getRootComposition(), null, cb);
        mb.addCode(cb.build());

        mb.addStatement("return $N", RecordKit.RECORD_PARAM);
        classBuilder.addMethod(mb.build());
    }


    protected void generateGetTableName() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(RecordKit.GET_TABLE_NAME_METHOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);
        mb.returns(ClassName.get(String.class));
        mb.addStatement("return $S", getTableName());
        classBuilder.addMethod(mb.build());
    }

    protected void generateGetJoinTables() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(RecordKit.GET_TABLE_ALIASES_METHOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);
        TypeName retTypeName = ParameterizedTypeName.get(ClassName.get(Map.class),
                ClassName.get(String.class), ClassName.get(String.class));
        mb.returns(retTypeName);

        if (!recordElement.getTableAliases().isEmpty()) {
            ArrayCodegen acg = new ArrayCodegen();
            for (Map.Entry<String, String> jt : recordElement.getTableAliases().entrySet()) {
                acg.add("$S,$S", jt.getKey(), jt.getValue());
            }
            CodeBlock.Builder cb = CodeBlock.builder();
            cb.add("return $T.of(", ClassName.get(Map.class));
            cb.add(acg.toFormat(), acg.toValues());
            cb.add(");\n");
            mb.addCode(cb.build());
        } else {
            mb.addStatement("return null");
        }
        classBuilder.addMethod(mb.build());
    }

    protected void generateGetRecordToken() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(RecordKit.GET_RECORD_TOKEN_METHOD);
        mb.addModifiers(Modifier.PROTECTED);
        mb.addAnnotation(Override.class);
        mb.returns(ClassName.get(String.class));

        List<ColumnElement> allColumns = recordElement.getAllColumns();
        List<String> selectItems = new ArrayList<>();

        for (ColumnElement column : allColumns) {
            if (column.getSelectAs() == null) {
                continue;
            }

            String selectAs;
            if (StringUtils.isBlank(column.getTableName())) {
                selectAs = column.getSelectAs().replaceAll("@column\\((.+)\\)", "$1 " + column.getName());
                selectAs = StringUtils.replace(selectAs, "@column", column.getName());
            } else {
                selectAs = column.getSelectAs().replaceAll("@column\\((.+)\\)", column.getTableName() + ".$1 " + column.getName());
                selectAs = StringUtils.replace(selectAs, "@column", column.getTableName() + '.' + column.getName());
            }

            selectItems.add(selectAs);
        }
        String token = StringUtils.join(selectItems, ", ");
        mb.addStatement("return $S", token);
        classBuilder.addMethod(mb.build());
    }

    protected void generateGetColumnsToken() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(RecordKit.GET_COLUMNS_TOKEN_METHOD);
        mb.addModifiers(Modifier.PROTECTED);
        mb.addAnnotation(Override.class);
        mb.returns(ClassName.get(String.class));

        List<ColumnElement> allColumns = recordElement.getAllColumns();
        List<String> columnNames = new ArrayList<>();
        for (ColumnElement column : allColumns) {
            if (column.getInsertAs() == null) {
                continue;
            }
            columnNames.add(column.getName());
        }
        String token = StringUtils.join(columnNames, ", ");
        mb.addStatement("return $S", token);
        classBuilder.addMethod(mb.build());
    }

    protected void generateGetValuesToken() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(RecordKit.GET_VALUES_TOKEN);
        mb.addModifiers(Modifier.PROTECTED);
        mb.addAnnotation(Override.class);
        mb.returns(ClassName.get(String.class));

        List<ColumnElement> allColumns = recordElement.getAllColumns();
        List<String> columnValues = new ArrayList<>();
        for (ColumnElement column : allColumns) {
            if (column.getInsertAs() == null) {
                continue;
            }

            if (column.getInsertAs().equals("@field")) {
                String paramName = generateChain(null, column.getParentComposition(), column, FieldElement::getName);
                columnValues.add(":" + paramName);
            } else {
                columnValues.add(column.getInsertAs());
            }
        }
        String valuesToken = StringUtils.join(columnValues, ", ");

        mb.addStatement("return $S", valuesToken);
        classBuilder.addMethod(mb.build());
    }

    protected void generateGetUpdatesToken() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(RecordKit.GET_UPDATES_TOKEN);
        mb.addModifiers(Modifier.PROTECTED);
        mb.addAnnotation(Override.class);
        mb.returns(ClassName.get(String.class));

        List<ColumnElement> allColumns = recordElement.getAllColumns();
        List<String> assigns = new ArrayList<>();
        for (ColumnElement column : allColumns) {
            if (column.getUpdateAs() == null) {
                continue;
            }

            if (column.getUpdateAs().equals("@field")) {
                String paramName = generateChain(null, column.getParentComposition(), column, FieldElement::getName);
                assigns.add(column.getName() + " = :" + paramName);
            } else {
                assigns.add(column.getName() + " = " + column.getUpdateAs());
            }
        }
        String token = StringUtils.join(assigns, ", ");

        mb.addStatement("return $S", token);
        classBuilder.addMethod(mb.build());
    }


    protected String generateCreateTableSQL() {
        List<ColumnElement> allColumns = recordElement.getAllColumns();
        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        sb.append(getTableName()).append("(");

        List<String> columnNames = new ArrayList<>();
        for (ColumnElement column : allColumns) {
            String definition = column.getDefinition();
            if (definition == null) {
                continue;
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

    protected void generateRecord(RecordElement recordElement) {
        this.mediatorFields = new KitFields("md");
        this.recordElement = recordElement;
        this.classBuilder = TypeSpec.classBuilder(getHelperClassName());

        classBuilder.addModifiers(Modifier.PUBLIC);

        TypeName baseTypeName = ParameterizedTypeName.get(
                ClassName.bestGuess(recordElement.getExtend().asClassElement().getName()),
                TypeName.get(recordElement.getOriginClass().asType()));

        classBuilder.superclass(baseTypeName);

        generateExportMethod();
        generatImportMethod();
        generateGetTableName();
        generateGetJoinTables();
        generateGetRecordToken();
        generateGetColumnsToken();
        generateGetValuesToken();
        generateGetUpdatesToken();

        classBuilder.addJavadoc(generateCreateTableSQL());

        generateNewRecord();
        generateMediatorFields();
        generateConstructor();

        String packageName = recordElement.getOriginClass().getPackageName();
        CodegenUtils.createJavaFile(processingEnv, classBuilder.build(), packageName, recordElement.getOriginClass().unwrap());
    }

    public void generate(ViewSetElement views) {
        for (Map.Entry<String, RecordElement> pr : views.getRecords().entrySet()) {
            generateRecord(pr.getValue());
        }
    }
}
