/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
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

import colesico.framework.assist.StringUtils;
import colesico.framework.assist.codegen.ArrayCodegen;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.jdbirec.AbstRactrecordKit;
import colesico.framework.jdbirec.Column;
import colesico.framework.jdbirec.FieldMediator;
import colesico.framework.jdbirec.codegen.model.*;
import com.palantir.javapoet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
import jakarta.inject.Singleton;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;

public class RecordKitGenerator {

    private static final Logger logger = LoggerFactory.getLogger(RecordKitGenerator.class);

    private final ProcessingEnvironment processingEnv;

    protected RecordKitElement recordKit;

    protected RecordElement record;

    protected RecordViewElement recordView;

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
        String fieldName = field.name();
        return "get" + StringUtils.firstCharToUpperCase(fieldName);
    }

    protected String toSetterName(FieldElement field) {
        String fieldName = field.name();
        return "set" + StringUtils.firstCharToUpperCase(fieldName);
    }

    public void generateMediatorFields() {
        for (Map.Entry<TypeMirror, String> f : mediatorFields.fieldsMap().entrySet()) {
            FieldSpec.Builder fb = FieldSpec.builder(TypeName.get(f.getKey()), f.getValue(), Modifier.FINAL);
            classBuilder.addField(fb.build());
        }
    }

    public void generateConstructor() {
        MethodSpec.Builder mb = MethodSpec.constructorBuilder();
        mb.addModifiers(Modifier.PUBLIC);
        for (Map.Entry<TypeMirror, String> f : mediatorFields.fieldsMap().entrySet()) {
            mb.addStatement("this.$N = new $T()", f.getValue(), TypeName.get(f.getKey()));
        }
        classBuilder.addMethod(mb.build());
    }

    public void generateNewRecord() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(AbstRactrecordKit.NEW_RECORD_METHOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);
        mb.returns(TypeName.get(record.type().unwrap()));
        mb.addStatement("return new $T()", TypeName.get(record.type().unwrap()));
        classBuilder.addMethod(mb.build());
    }

    public String generateVarChain(String rootVarName, ContainerElement container, ColumnElement column, Function<FieldElement, String> fieldTransformer) {
        Deque<FieldElement> fieldsStack = new ArrayDeque<>();
        if (column != null) {
            fieldsStack.push(column.field());
        }
        ContainerElement c = container;
        while (c instanceof CompositionElement comp) {
            fieldsStack.push(comp.field());
            c = comp.container();
        }
        List<FieldElement> fieldsChain = new ArrayList<>(fieldsStack);

        List<String> gettersChain = new ArrayList<>();
        if (!StringUtils.isBlank(rootVarName)) {
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
            if (composition.join()) {
                return;
            }

            compositionVar = varNames.getNextVarName(composition.field().name());
            cb.add("\n");
            cb.add("// Composition: " + composition.type().unwrap() + "\n\n");
            // SubCompositionType comp=parentComp.getSubComposition()
            cb.addStatement("$T $N = $N.$N()",
                    TypeName.get(composition.type().unwrap()),
                    compositionVar,
                    parentContainerVar,
                    toGetterName(composition.field()));
            // if (comp == null) { comp = new SubCompositionType()}
            cb.add("if ($N == null) { $N = new $T(); }\n", compositionVar, compositionVar, TypeName.get(container.type().unwrap()));
        } else {
            compositionVar = AbstRactrecordKit.RECORD_PARAM;
        }

        for (ColumnElement column : container.columns()) {
            if (!column.exportable()) {
                continue;
            }
            String paramName = generateVarChain(null, column.container(), column, FieldElement::name);
            String fieldGetterName = toGetterName(column.field());
            if (column.mediator() == null) {
                // fr.receive("column",comp.getField1())
                cb.add("$N.$N($S, ", AbstRactrecordKit.FIELD_RECEIVER_PARAM, AbstRactrecordKit.FieldReceiver.SET_METHOD, paramName);
                cb.add("$N.$N()", compositionVar, fieldGetterName);
                cb.add(");\n");
            } else {
                // mediator.exportField(comp.getField1(),"param1",fr)
                String mediatorField = mediatorFields.addField(column.mediator().unwrap());
                cb.add("$N.$N(", mediatorField, FieldMediator.EXPORT_METHOD);
                cb.add("$N.$N()", compositionVar, fieldGetterName);
                cb.add(",$S,$N);\n", paramName, AbstRactrecordKit.FIELD_RECEIVER_PARAM);
            }
        }

        for (CompositionElement subComposition : container.compositions()) {
            generateContainerToMap(subComposition, compositionVar, cb);
        }
    }

    private String getSelectColumnName(ColumnElement column) {
        return column.name();
        //return StringUtils.isBlank(column.getTableName()) ? column.getName() : column.getTableName() + '.' + column.getName();
    }

    protected void generateGetValue(ColumnElement column, CodeBlock.Builder cb) {

        if (column.mediator() != null) {
            String mediatorField = mediatorFields.addField(column.mediator().unwrap());
            cb.add("$N.$N($S,$N)",
                    mediatorField,
                    FieldMediator.IMPORT_METHOD,
                    getSelectColumnName(column),
                    AbstRactrecordKit.RESULT_SET_PARAM);
            return;
        }

        String fieldType = column.field().originType().toString();
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
                cb.add("$N.getObject($S,$T.class)", AbstRactrecordKit.RESULT_SET_PARAM, getSelectColumnName(column), TypeName.get(column.field().originType()));
                break;

            default:
                cb.add("$N.getObject($S,$T.class)", AbstRactrecordKit.RESULT_SET_PARAM, getSelectColumnName(column), TypeName.get(column.field().originType()));
        }
    }

    protected void generateContainerFromResultSet(ContainerElement container, String parentContainerVar, CodeBlock.Builder cb) {
        CodeBlock.Builder cbo = CodeBlock.builder();
        String compositionVar;
        if (container instanceof CompositionElement composition) {
            cbo.add("\n");
            cbo.add("// Container: " + container.type().unwrap());
            cbo.add("\n\n");
            compositionVar = varNames.getNextVarName(composition.field().name());
            // SubCompositionType comp=parentComp.getSubComposition()
            cbo.addStatement("$T $N = $N.$N()",
                    TypeName.get(composition.type().unwrap()),
                    compositionVar,
                    parentContainerVar,
                    toGetterName(composition.field()));
            // if (comp == null )
            cbo.add("if ($N == null) {\n", compositionVar);
            cbo.indent();
            // comp = new SubCompositionType();
            cbo.addStatement("$N = new $T()", compositionVar, TypeName.get(composition.type().unwrap()));
            // parentComp.setSubComposition(comp)
            cbo.addStatement("$N.$N($N)", parentContainerVar, toSetterName(composition.field()), compositionVar);
            cbo.unindent();
            cbo.add("}\n");
        } else {
            compositionVar = AbstRactrecordKit.RECORD_PARAM;
        }

        for (ColumnElement column : container.columns()) {
            if (!column.importable()) {
                continue;
            }
            // comp.setField(
            cbo.add("$N.$N", compositionVar, toSetterName(column.field()));
            cbo.add("(");
            generateGetValue(column, cbo);
            cbo.add(");\n");
        }

        for (CompositionElement subComposition : container.compositions()) {
            generateContainerFromResultSet(subComposition, compositionVar, cbo);
        }

        if (container instanceof CompositionElement comp && !comp.nullInstance()) {

            for (ColumnElement column : container.columns()) {
                cb.add("if ($N.getObject($S) != null) {\n", AbstRactrecordKit.RESULT_SET_PARAM, column);
                cb.indent();
            }

            cb.add(cbo.build());

            for (ColumnElement column : container.columns()) {
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
        mb.addParameter(TypeName.get(recordView.type().unwrap()), AbstRactrecordKit.RECORD_PARAM, Modifier.FINAL);
        mb.addParameter(TypeName.get(AbstRactrecordKit.FieldReceiver.class),
                AbstRactrecordKit.FIELD_RECEIVER_PARAM, Modifier.FINAL);

        CodeBlock.Builder cb = CodeBlock.builder();
        varNames = new VarNames();
        generateContainerToMap(recordView, null, cb);
        mb.addCode(cb.build());

        classBuilder.addMethod(mb.build());
    }


    protected void generateImportMethod() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(AbstRactrecordKit.IMPORT_RECORD_METHOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);
        mb.returns(TypeName.get(record.type().unwrap()));
        mb.addParameter(TypeName.get(record.type().unwrap()), AbstRactrecordKit.RECORD_PARAM, Modifier.FINAL);
        mb.addParameter(ClassName.get(ResultSet.class), AbstRactrecordKit.RESULT_SET_PARAM, Modifier.FINAL);
        mb.addException(ClassName.get(SQLException.class));

        varNames = new VarNames();

        CodeBlock.Builder cb = CodeBlock.builder();
        generateContainerFromResultSet(recordView, null, cb);
        mb.addCode(cb.build());

        mb.addStatement("return $N", AbstRactrecordKit.RECORD_PARAM);
        classBuilder.addMethod(mb.build());
    }


    protected void generateGetTableName() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(AbstRactrecordKit.GET_TABLE_NAME_METHOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);
        mb.returns(ClassName.get(String.class));
        mb.addStatement("return $S", tableName());
        classBuilder.addMethod(mb.build());
    }

    protected void generateGetTableAliases() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(AbstRactrecordKit.GET_TABLES_ALIASES_METHOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);
        TypeName retTypeName = ParameterizedTypeName.get(ClassName.get(Map.class),
                ClassName.get(String.class), ClassName.get(String.class));
        mb.returns(retTypeName);

        if (!recordKit.tableAliases().isEmpty()) {
            ArrayCodegen acg = new ArrayCodegen();
            for (Map.Entry<String, String> jt : recordKit.tableAliases().entrySet()) {
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

    protected void generateGetSelectRecordToken() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(AbstRactrecordKit.GET_RECORD_TOKEN_METHOD);
        mb.addModifiers(Modifier.PROTECTED);
        mb.addAnnotation(Override.class);
        mb.returns(ClassName.get(String.class));

        List<ColumnElement> allColumns = recordView.allColumns();
        List<String> selectItems = new ArrayList<>();

        for (ColumnElement column : allColumns) {
            if (column.selectAs() == null) {
                continue;
            }

            String selectAs = column.selectAs();

            if (!Column.AS_COLUMN.equals(selectAs)) {
                selectAs = selectAs + " as " + column.name();
            }

            String tableName = column.container().tableName();
            if (StringUtils.isBlank(tableName)) {
                selectAs = StringUtils.replace(selectAs, Column.AS_COLUMN, column.name());
            } else {
                selectAs = StringUtils.replace(selectAs, Column.AS_COLUMN, tableName + '.' + column.name());
            }

            selectItems.add(selectAs);
        }
        String token = StringUtils.join(selectItems, ", ");
        mb.addStatement("return $S", token);
        classBuilder.addMethod(mb.build());
    }

    /**
     * For insert sql statements
     */
    protected void generateGetInsertColumnsToken() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(AbstRactrecordKit.GET_COLUMNS_TOKEN_METHOD);
        mb.addModifiers(Modifier.PROTECTED);
        mb.addAnnotation(Override.class);
        mb.returns(ClassName.get(String.class));

        List<ColumnElement> allColumns = recordView.allColumns();
        List<String> columnNames = new ArrayList<>();
        for (ColumnElement column : allColumns) {
            if (column.insertAs() == null) {
                continue;
            }
            if (column.container() instanceof CompositionElement c) {
                if (c.join()) {
                    continue;
                }
            }
            columnNames.add(column.name());
        }
        String token = StringUtils.join(columnNames, ", ");
        mb.addStatement("return $S", token);
        classBuilder.addMethod(mb.build());
    }

    protected void generateGetInsertValuesToken() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(AbstRactrecordKit.GET_VALUES_TOKEN_METHOD);
        mb.addModifiers(Modifier.PROTECTED);
        mb.addAnnotation(Override.class);
        mb.returns(ClassName.get(String.class));

        List<ColumnElement> allColumns = recordView.allColumns();
        List<String> columnValues = new ArrayList<>();
        for (ColumnElement column : allColumns) {
            if (column.insertAs() == null) {
                continue;
            }

            if (column.container() instanceof CompositionElement c) {
                if (c.join()) {
                    continue;
                }
            }

            if (column.insertAs().equals(Column.AS_FIELD)) {
                String paramName = generateVarChain(null, column.container(), column, FieldElement::name);
                columnValues.add(":" + paramName);
            } else {
                columnValues.add(column.insertAs());
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

        List<ColumnElement> allColumns = recordView.allColumns();
        List<String> assigns = new ArrayList<>();
        for (ColumnElement column : allColumns) {
            if (column.updateAs() == null) {
                continue;
            }

            if (column.container() instanceof CompositionElement c) {
                if (c.join()) {
                    continue;
                }
            }

            if (column.updateAs().equals(Column.AS_FIELD)) {
                String paramName = generateVarChain(null, column.container(), column, FieldElement::name);
                assigns.add(column.name() + " = :" + paramName);
            } else {
                assigns.add(column.name() + " = " + column.updateAs());
            }
        }
        String token = StringUtils.join(assigns, ", ");

        mb.addStatement("return $S", token);
        classBuilder.addMethod(mb.build());
    }


    protected String generateCreateTableSQL() {
        List<ColumnElement> allColumns = recordView.allColumns();
        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        sb.append(tableName()).append("(\n");

        List<String> columnNames = new ArrayList<>();
        for (ColumnElement column : allColumns) {
            String definition = column.definition();
            if (definition == null) {
                continue;
            }
            columnNames.add("    " + column.name() + " " + definition);
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

    protected String tableName() {
        if (StringUtils.isBlank(record.tableName())) {
            return "[TABLE]";
        } else {
            return record.tableName();
        }
    }

    protected void generateRecord(RecordViewElement view) {
        this.recordView = view;
        this.mediatorFields = new KitFields("md");
        this.classBuilder = TypeSpec.classBuilder(RecordKitGeneratorUtils.buildRecordKitInstanceClassName(view));

        classBuilder.addModifiers(Modifier.PUBLIC);

        TypeName baseTypeName = ParameterizedTypeName.get(
                ClassName.bestGuess(recordKit.superclass().asClassElement().name()),
                TypeName.get(view.type().unwrap()));

        classBuilder.superclass(baseTypeName);
        classBuilder.addSuperinterface(TypeName.get(recordKit.originClass().asClassType().unwrap()));
        classBuilder.addAnnotation(Singleton.class);

        generateRecordKitClassDoc();

        generateExportMethod();
        generateImportMethod();
        generateGetTableName();
        generateGetTableAliases();
        generateGetSelectRecordToken();
        generateGetInsertColumnsToken();
        generateGetInsertValuesToken();
        generateGetUpdatesToken();
        generateNewRecord();
        generateMediatorFields();
        generateConstructor();

        String packageName = recordKit.originClass().packageName();
        CodegenUtils.createJavaFile(processingEnv, classBuilder.build(), packageName, recordKit.originClass().unwrap());
    }

    public void generate(RecordKitElement recordKit) {
        this.recordKit = recordKit;
        this.record = recordKit.record();
        for (RecordViewElement view : recordKit.record().views()) {
            generateRecord(view);
        }

        IocGenerator iocGenerator = new IocGenerator(processingEnv);
        iocGenerator.generate(recordKit);
    }
}
