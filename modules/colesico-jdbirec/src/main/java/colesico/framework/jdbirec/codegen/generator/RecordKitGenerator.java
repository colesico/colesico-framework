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
import colesico.framework.jdbirec.AbstRactrecordKit;
import colesico.framework.jdbirec.Column;
import colesico.framework.jdbirec.FieldMediator;
import colesico.framework.jdbirec.codegen.model.*;
import com.palantir.javapoet.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Singleton;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;

public class RecordKitGenerator {

    private static final Logger logger = LoggerFactory.getLogger(RecordKitGenerator.class);
    private final ProcessingEnvironment processingEnv;

    protected RecordKitElement recordKitElement;
    protected RecordElement recordElement;
    protected TypeSpec.Builder classBuilder;

    // Mediator fields
    protected KitFields mediatorFields;

    protected VarNames varNames;

    public RecordKitGenerator(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
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
        MethodSpec.Builder mb = MethodSpec.methodBuilder(AbstRactrecordKit.NEW_RECORD_METHOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);
        mb.returns(TypeName.get(recordKitElement.getRecordType().unwrap()));
        mb.addStatement("return new $T()", TypeName.get(recordKitElement.getRecordType().unwrap()));
        classBuilder.addMethod(mb.build());
    }

    public String generateVarChain(String rootVarName, ContainerElement container, ColumnElement column, Function<FieldElement, String> fieldTransformer) {
        Deque<FieldElement> fieldsStack = new ArrayDeque<>();
        if (column != null) {
            fieldsStack.push(column.getField());
        }
        ContainerElement c = container;
        while (c instanceof CompositionElement comp) {
            fieldsStack.push(comp.getField());
            c = comp.getContainer();
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

    protected void generateContainerToMap(ContainerElement container, String parentContainerVar, CodeBlock.Builder cb) {
        String compositionVar;
        if (container instanceof CompositionElement composition) {
            compositionVar = varNames.getNextVarName(composition.getField().getName());
            cb.add("\n");
            cb.add("// Composition: " + composition.getType().unwrap() + "\n\n");
            // SubCompositionType comp=parentComp.getSubComposition()
            cb.addStatement("$T $N = $N.$N()",
                    TypeName.get(composition.getType().unwrap()),
                    compositionVar,
                    parentContainerVar,
                    toGetterName(composition.getField()));
            // if (comp == null) { comp = new SubCompositionType()}
            cb.add("if ($N == null) { $N = new $T(); }\n", compositionVar, compositionVar, TypeName.get(container.getType().unwrap()));
        } else {
            compositionVar = AbstRactrecordKit.RECORD_PARAM;
        }

        for (ColumnElement column : container.getColumns()) {
            if (!column.isExportable()) {
                continue;
            }
            String paramName = generateVarChain(null, column.getContainer(), column, FieldElement::getName);
            String fieldGetterName = toGetterName(column.getField());
            if (column.getMediator() == null) {
                // fr.receive("column",comp.getField1())
                cb.add("$N.$N($S, ", AbstRactrecordKit.FIELD_RECEIVER_PARAM, AbstRactrecordKit.FieldReceiver.SET_METHOD, paramName);
                cb.add("$N.$N()", compositionVar, fieldGetterName);
                cb.add(");\n");
            } else {
                // mediator.exportField(comp.getField1(),"param1",fr)
                String mediatorField = mediatorFields.addField(column.getMediator().unwrap());
                cb.add("$N.$N(", mediatorField, FieldMediator.EXPORT_METHOD);
                cb.add("$N.$N()", compositionVar, fieldGetterName);
                cb.add(",$S,$N);\n", paramName, AbstRactrecordKit.FIELD_RECEIVER_PARAM);
            }
        }

        for (CompositionElement subComposition : container.getCompositions()) {
            generateContainerToMap(subComposition, compositionVar, cb);
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
                    AbstRactrecordKit.RESULT_SET_PARAM);
            return;
        }

        String fieldType = column.getField().getOriginType().toString();
        switch (fieldType) {
            case "char":
                cb.add("$N.getChar($S)", AbstRactrecordKit.RESULT_SET_PARAM, getSelectColumnName(column));
                break;
            case "boolean":
                cb.add("$N.getBoolean($S)", AbstRactrecordKit.RESULT_SET_PARAM, getSelectColumnName(column));
                break;
            case "long":
                cb.add("$N.getLong($S)", AbstRactrecordKit.RESULT_SET_PARAM, getSelectColumnName(column));
                break;
            case "int":
                cb.add("$N.getInt($S)", AbstRactrecordKit.RESULT_SET_PARAM, getSelectColumnName(column));
                break;
            case "short":
                cb.add("$N.getShort($S)", AbstRactrecordKit.RESULT_SET_PARAM, getSelectColumnName(column));
                break;
            case "byte":
                cb.add("$N.getByte($S)", AbstRactrecordKit.RESULT_SET_PARAM, getSelectColumnName(column));
                break;
            case "double":
                cb.add("$N.getDouble($S)", AbstRactrecordKit.RESULT_SET_PARAM, getSelectColumnName(column));
                break;
            case "float":
                cb.add("$N.getFloat($S)", AbstRactrecordKit.RESULT_SET_PARAM, getSelectColumnName(column));
                break;
            case "java.lang.String":
                cb.add("$N.getString($S)", AbstRactrecordKit.RESULT_SET_PARAM, getSelectColumnName(column));
                break;
            case "java.math.BigDecimal":
                cb.add("$N.getBigDecimal($S)", AbstRactrecordKit.RESULT_SET_PARAM, getSelectColumnName(column));
                break;
            case "java.time.LocalDateTime":
                cb.add("$N.getTimestamp($S) == null ? null : $N.getTimestamp($S).toLocalDateTime()",
                        AbstRactrecordKit.RESULT_SET_PARAM, getSelectColumnName(column),
                        AbstRactrecordKit.RESULT_SET_PARAM, getSelectColumnName(column)
                );
                break;
            case "java.time.Instant":
                cb.add("$N.getTimestamp($S) == null ? null : $N.getTimestamp($S).toInstant()",
                        AbstRactrecordKit.RESULT_SET_PARAM, getSelectColumnName(column),
                        AbstRactrecordKit.RESULT_SET_PARAM, getSelectColumnName(column)
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
                cb.add("$N.getObject($S,$T.class)", AbstRactrecordKit.RESULT_SET_PARAM, getSelectColumnName(column), TypeName.get(column.getField().getOriginType()));
                break;

            default:
                cb.add("$N.getObject($S,$T.class)", AbstRactrecordKit.RESULT_SET_PARAM, getSelectColumnName(column), TypeName.get(column.getField().getOriginType()));
        }
    }

    protected void generateContainerFromResultSet(ContainerElement container, String parentContainerVar, CodeBlock.Builder cb) {
        CodeBlock.Builder cbo = CodeBlock.builder();
        String compositionVar;
        if (container instanceof CompositionElement composition) {
            cbo.add("\n");
            cbo.add("// Container: " + container.getType().unwrap());
            cbo.add("\n\n");
            compositionVar = varNames.getNextVarName(composition.getField().getName());
            // SubCompositionType comp=parentComp.getSubComposition()
            cbo.addStatement("$T $N = $N.$N()",
                    TypeName.get(composition.getType().unwrap()),
                    compositionVar,
                    parentContainerVar,
                    toGetterName(composition.getField()));
            // if (comp == null )
            cbo.add("if ($N == null) {\n", compositionVar);
            cbo.indent();
            // comp = new SubCompositionType();
            cbo.addStatement("$N = new $T()", compositionVar, TypeName.get(composition.getType().unwrap()));
            // parentComp.setSubComposition(comp)
            cbo.addStatement("$N.$N($N)", parentContainerVar, toSetterName(composition.getField()), compositionVar);
            cbo.unindent();
            cbo.add("}\n");
        } else {
            compositionVar = AbstRactrecordKit.RECORD_PARAM;
        }

        for (ColumnElement column : container.getColumns()) {
            if (!column.isImportable()) {
                continue;
            }
            // comp.setField(
            cbo.add("$N.$N", compositionVar, toSetterName(column.getField()));
            cbo.add("(");
            generateGetValue(column, cbo);
            cbo.add(");\n");
        }

        for (CompositionElement subComposition : container.getCompositions()) {
            generateContainerFromResultSet(subComposition, compositionVar, cbo);
        }

        if (container instanceof CompositionElement comp && !comp.isNullInstance()) {

            for (ColumnElement column : container.getColumns()) {
                cb.add("if ($N.getObject($S) != null) {\n", AbstRactrecordKit.RESULT_SET_PARAM, column);
                cb.indent();
            }

            cb.add(cbo.build());

            for (ColumnElement column : container.getColumns()) {
                cb.unindent();
                cb.add("}\n");
            }
        } else {
            cb.add(cbo.build());
        }
    }

    /**
     * @see AbstRactrecordKit
     */
    protected void generateExportMethod() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(AbstRactrecordKit.EXPORT_RECORD_METHOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);
        mb.addParameter(TypeName.get(recordElement.getType().unwrap()), AbstRactrecordKit.RECORD_PARAM, Modifier.FINAL);
        mb.addParameter(TypeName.get(AbstRactrecordKit.FieldReceiver.class),
                AbstRactrecordKit.FIELD_RECEIVER_PARAM, Modifier.FINAL);

        CodeBlock.Builder cb = CodeBlock.builder();
        varNames = new VarNames();
        generateContainerToMap(recordElement, null, cb);
        mb.addCode(cb.build());

        classBuilder.addMethod(mb.build());
    }


    protected void generateImportMethod() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(AbstRactrecordKit.IMPORT_RECORD_METHOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);
        mb.returns(TypeName.get(recordKitElement.getRecordType().unwrap()));
        mb.addParameter(TypeName.get(recordKitElement.getRecordType().unwrap()), AbstRactrecordKit.RECORD_PARAM, Modifier.FINAL);
        mb.addParameter(ClassName.get(ResultSet.class), AbstRactrecordKit.RESULT_SET_PARAM, Modifier.FINAL);
        mb.addException(ClassName.get(SQLException.class));

        varNames = new VarNames();

        CodeBlock.Builder cb = CodeBlock.builder();
        generateContainerFromResultSet(recordElement, null, cb);
        mb.addCode(cb.build());

        mb.addStatement("return $N", AbstRactrecordKit.RECORD_PARAM);
        classBuilder.addMethod(mb.build());
    }


    protected void generateGetTableName() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(AbstRactrecordKit.GET_TABLE_NAME_METHOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);
        mb.returns(ClassName.get(String.class));
        mb.addStatement("return $S", getTableName());
        classBuilder.addMethod(mb.build());
    }

    protected void generateGetJoinTables() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(AbstRactrecordKit.GET_TABLES_ALIASES_METHOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);
        TypeName retTypeName = ParameterizedTypeName.get(ClassName.get(Map.class),
                ClassName.get(String.class), ClassName.get(String.class));
        mb.returns(retTypeName);

        if (!recordKitElement.getTableAliases().isEmpty()) {
            ArrayCodegen acg = new ArrayCodegen();
            for (Map.Entry<String, String> jt : recordKitElement.getTableAliases().entrySet()) {
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
        MethodSpec.Builder mb = MethodSpec.methodBuilder(AbstRactrecordKit.GET_RECORD_TOKEN_METHOD);
        mb.addModifiers(Modifier.PROTECTED);
        mb.addAnnotation(Override.class);
        mb.returns(ClassName.get(String.class));

        List<ColumnElement> allColumns = recordElement.getAllColumns();
        List<String> selectItems = new ArrayList<>();

        for (ColumnElement column : allColumns) {
            if (column.getSelectAs() == null) {
                continue;
            }

            String selectAs = column.getSelectAs();
            String tableName = column.getContainer().getTableName();
            if (StringUtils.isBlank(tableName)) {
                selectAs = selectAs.replaceAll(Column.COLUMN_REF + "\\((.+)\\)", "$1");
                selectAs = StringUtils.replace(selectAs, Column.COLUMN_REF, column.getName());
            } else {
                selectAs = selectAs.replaceAll(Column.COLUMN_REF + "\\((.+)\\)", tableName + ".$1");
                selectAs = StringUtils.replace(selectAs, Column.COLUMN_REF, tableName + '.' + column.getName());
            }
            if (!(selectAs.equalsIgnoreCase(column.getName())
                    || selectAs.endsWith('.' + column.getName())
                    || selectAs.endsWith(' ' + column.getName())
            )) {
                selectAs = selectAs + ' ' + column.getName();
            }

            selectItems.add(selectAs);
        }
        String token = StringUtils.join(selectItems, ", ");
        mb.addStatement("return $S", token);
        classBuilder.addMethod(mb.build());
    }

    protected void generateGetColumnsToken() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(AbstRactrecordKit.GET_COLUMNS_TOKEN_METHOD);
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
        MethodSpec.Builder mb = MethodSpec.methodBuilder(AbstRactrecordKit.GET_VALUES_TOKEN_METHOD);
        mb.addModifiers(Modifier.PROTECTED);
        mb.addAnnotation(Override.class);
        mb.returns(ClassName.get(String.class));

        List<ColumnElement> allColumns = recordElement.getAllColumns();
        List<String> columnValues = new ArrayList<>();
        for (ColumnElement column : allColumns) {
            if (column.getInsertAs() == null) {
                continue;
            }

            if (column.getInsertAs().equals(Column.FIELD_REF)) {
                String paramName = generateVarChain(null, column.getContainer(), column, FieldElement::getName);
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
        MethodSpec.Builder mb = MethodSpec.methodBuilder(AbstRactrecordKit.GET_UPDATES_TOKEN_METHOD);
        mb.addModifiers(Modifier.PROTECTED);
        mb.addAnnotation(Override.class);
        mb.returns(ClassName.get(String.class));

        List<ColumnElement> allColumns = recordElement.getAllColumns();
        List<String> assigns = new ArrayList<>();
        for (ColumnElement column : allColumns) {
            if (column.getUpdateAs() == null) {
                continue;
            }

            if (column.getUpdateAs().equals(Column.FIELD_REF)) {
                String paramName = generateVarChain(null, column.getContainer(), column, FieldElement::getName);
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
        sb.append(getTableName()).append("(\n");

        List<String> columnNames = new ArrayList<>();
        for (ColumnElement column : allColumns) {
            String definition = column.getDefinition();
            if (definition == null) {
                continue;
            }
            columnNames.add("    " + column.getName() + " " + definition);
        }
        sb.append(StringUtils.join(columnNames, ", \n"));
        sb.append("\n)\n");
        return sb.toString();
    }

    protected void generateRecordKitClassDoc() {
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add(generateCreateTableSQL());
        classBuilder.addJavadoc(cb.build());
    }

    protected String getTableName() {
        if (StringUtils.isEmpty(recordKitElement.getTableName())) {
            return "[TABLE]";
        } else {
            return recordKitElement.getTableName();
        }
    }

    protected void generateRecord(RecordElement record) {
        this.recordElement = record;
        this.mediatorFields = new KitFields("md");
        this.classBuilder = TypeSpec.classBuilder(RecordKitGeneratorUtils.buildRecordKitInstanceClassName(record));

        classBuilder.addModifiers(Modifier.PUBLIC);

        TypeName baseTypeName = ParameterizedTypeName.get(
                ClassName.bestGuess(recordKitElement.getSuperclass().asClassElement().getName()),
                TypeName.get(recordKitElement.getRecordType().unwrap()));

        classBuilder.superclass(baseTypeName);
        classBuilder.addSuperinterface(TypeName.get(recordKitElement.getOriginClass().asClassType().unwrap()));
        classBuilder.addAnnotation(Singleton.class);

        generateRecordKitClassDoc();

        generateExportMethod();
        generateImportMethod();
        generateGetTableName();
        generateGetJoinTables();
        generateGetRecordToken();
        generateGetColumnsToken();
        generateGetValuesToken();
        generateGetUpdatesToken();
        generateNewRecord();
        generateMediatorFields();
        generateConstructor();

        String packageName = recordKitElement.getOriginClass().getPackageName();
        CodegenUtils.createJavaFile(processingEnv, classBuilder.build(), packageName, recordKitElement.getOriginClass().unwrap());
    }

    public void generate(RecordKitElement recordKit) {
        this.recordKitElement = recordKit;
        for (RecordElement record : recordKit.getRecords()) {
            generateRecord(record);
        }

        IocGenerator iocGenerator = new IocGenerator(processingEnv);
        iocGenerator.generate(recordKit);
    }
}
