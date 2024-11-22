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

package colesico.framework.jdbirec.codegen.parser;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractParser;
import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.jdbirec.*;
import colesico.framework.jdbirec.RecordKit;
import colesico.framework.jdbirec.codegen.model.*;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.*;


public class RecordKitParser extends FrameworkAbstractParser {

    /**
     * Parsing record
     */
    protected RecordKitElement recordKitElement;

    public RecordKitParser(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    /**
     * Check view name should be class name compatible to generate class name suffix
     */
    protected void checkViewName(final String viewName, Element parentElement) {
        if (!viewName.matches("[A-Za-z0-9]+")) {
            throw CodegenException.of().message("Invalid record view name. Alphanumeric chars expected: " + p).element(parentElement).build();
        }
    }

    /**
     * Builds compositions path string:  comp1.comp2.comp.name
     */
    protected String buildCompositionPath(CompositionElement comp, String name) {
        Deque<String> namesStack = new ArrayDeque<>();

        if (name != null) {
            namesStack.push(name);
        }

        CompositionElement c = comp;
        while (c.getOriginField() != null) {
            namesStack.push(c.getOriginField().getName());
            c = c.getParentComposition();
        }

        return StringUtils.join(namesStack, ".");
    }

    /**
     * Lookup column overridings from column composition to root composition
     */
    protected List<ColumnOverridingElement> findColumnOverridings(CompositionElement columnComposition, String columnName) {
        logger.debug("Find composition {} column '{}' overridings...", columnComposition, columnName);

        // Build composition chain from root to this
        CompositionElement c = columnComposition;
        List<CompositionElement> compositionChain = new ArrayList<>();
        while (c != null) {
            compositionChain.add(c);
            c = c.getParentComposition();
        }
        logger.debug("compositionChain size: {} ", compositionChain.size());

        // Collections.reverse(compositionChain);

        // Lookup overriding definition within compositions, starts from root composition
        List<ColumnOverridingElement> columnOverridings = new ArrayList<>();
        String columnPath = buildCompositionPath(columnComposition, columnName);
        for (CompositionElement ce : compositionChain) {
            logger.debug("Composition {} column overriding size= {} ", ce, ce.getColumnOverriding().size());

            if (ce.getColumnOverriding().isEmpty()) {
                continue;
            }

            // Find column within overriding
            for (ColumnOverridingElement coe : ce.getColumnOverriding()) {
                logger.debug("Test column {} overriding:  {}", columnPath, coe);
                if (coe.getColumnPath().equals(columnPath)) {
                    columnOverridings.add(coe);
                }
            }
        }

        return columnOverridings;
    }

    /**
     * Finds the composition group path matches the column group
     */
    protected boolean isInGroup(CompositionElement columnComposition, String columnGroup) {

        // Lookup compositions from current to root
        CompositionElement c = columnComposition;
        while (c != null) {
            for (String compositionGroup : c.getTagFilter()) {
                if (compositionGroup.equals(columnGroup)) {
                    return true;
                }
            }
            c = c.getParentComposition();

        }
        return false;
    }

    protected boolean hasParentOverridings(CompositionElement cmp) {
        CompositionElement curComp = cmp;
        while (curComp != null) {
            if (!curComp.getColumnOverriding().isEmpty()) {
                return true;
            }
            curComp = curComp.getParentComposition();
        }
        return false;
    }

    private List<AnnotationAssist<Column>> findFieldColumns(FieldElement field) {
        final List<AnnotationAssist<Column>> result = new ArrayList<>();

        AnnotationAssist<Column> column = field.getAnnotation(Column.class);
        if (column != null) {
            result.add(column);
        } else {
            AnnotationAssist<Columns> columns = field.getAnnotation(Columns.class);
            if (columns != null) {
                for (Column col : columns.unwrap().value()) {
                    column = new AnnotationAssist<>(processingEnv, col);
                    result.add(column);
                }
            }
        }
        return result;
    }

    protected void parseFieldCompositions(final CompositionElement parentComp, FieldElement compField) {

        final List<AnnotationAssist<Composition>> compositions = findFieldCompositions(compField);

        for (AnnotationAssist<Composition> compAst : compositions) {

            Set<String> tags = buildTags(parentComp, compField, compAst.unwrap().tags());

            // Check tags
            // TODO: check tags

            ClassType compType = compField.asClassType();
            CompositionElement comp = new CompositionElement(recordKitElement, compType, compField, tags);
            parentComp.addSubComposition(comp);

            // Set name
            if (StringUtils.isNotBlank(compAst.unwrap().name())) {
                comp.setName(StringUtils.trim(compAst.unwrap().name()));
            } else {
                comp.setName(compField.getName());
            }

            // Set renaming
            comp.setRenaming(compAst.unwrap().renaming());

            // Set null instance
            comp.setNullInstance(compAst.unwrap().nullInstace());

            // Set tagFilter
            comp.setTagFilter(compAst.unwrap().tagFilter());

            // Check subcomposition is a joint composition
            JointRecord jointRecord = parentComp.getParentRecordKit().getJointRecords().get(compType);
            String tableName;
            if (jointRecord != null) {
                tableName = jointRecord.getTableName();
            } else {
                tableName = parentComp.getTableName();
            }
            comp.setTableName(tableName);

            // Parse column overriding

            for (ColumnOverriding co : compAst.unwrap().columnOverriding()) {
                AnnotationAssist<ColumnOverriding> coa = new AnnotationAssist<>(processingEnv, co);

                String columnName = co.column();
                String columnPath = buildCompositionPath(comp, columnName);
                ColumnOverridingElement cre = new ColumnOverridingElement(columnPath);

                // name overriding
                if (StringUtils.isNoneBlank(co.name())) {
                    cre.setName(StringUtils.trim(co.name()));
                }

                // definition overriding
                if (StringUtils.isNoneBlank(co.definition())) {
                    cre.setName(StringUtils.trim(co.definition()));
                }

                // mediator overriding
                TypeMirror mediatorType = coa.getValueTypeMirror(ColumnOverriding::mediator);
                if (!CodegenUtils.isAssignable(FieldMediator.class, mediatorType, processingEnv)) {
                    cre.setMediator(new ClassType(processingEnv, (DeclaredType) mediatorType));
                }

                comp.overrideColumn(cre);
            }

            // Parse nested composition
            parseComposition(comp);

            break;
        } // for compositionAnnList

    }

    protected String applyColumnRenaming(CompositionElement composition, FieldElement columnField, String columnColumnName) {
        String columnOriginName = columnColumnName;
        if (StringUtils.isNotBlank(composition.getRenaming())) {
            columnColumnName = StringUtils.replace(composition.getRenaming(), Composition.RN_AUTO, composition.getName() + "_" + columnOriginName);
            columnColumnName = StringUtils.replace(columnColumnName, Composition.RN_COLUMN_FILED, columnField.getName());
            columnColumnName = StringUtils.replace(columnColumnName, Composition.RN_COLUMN, columnOriginName);
            columnColumnName = StringUtils.replace(columnColumnName, Composition.RN_COMPOSITION_FIELD, composition.getOriginField().getName());
            columnColumnName = StringUtils.replace(columnColumnName, Composition.RN_COMPOSITION, composition.getName());
        }
        logger.debug("Column renaming: {} -> rule '{}' -> {}", columnOriginName, composition.getRenaming(), columnColumnName);

        return columnColumnName;
    }

    protected Set<String> buildTags(CompositionElement composition, FieldElement columnField, String[] tagDefinitions) {
        Set<String> tags = new HashSet<>();

        // Add default tags
        String fieldTag = "#" + buildCompositionPath(composition, columnField.getName());
        tags.add(fieldTag);

        // Transform local tags to global
        for (String tag : tagDefinitions) {
            if (!StringUtils.startsWith(tag, "#")) {
                tag = "#" + buildCompositionPath(composition, tag);
            }
            tags.add(tag);
        }

        return tags;
    }

    /**
     * Parse field columns
     */
    protected void parseFieldColumns(final CompositionElement parentComp, FieldElement columnField) {
        logger.debug("Parse columns on field: {} of composition: {}", columnField.getName(), parentComp);
        final List<AnnotationAssist<Column>> columns = findFieldColumns(columnField);
        final Set<String> fieldGroups = new HashSet<>();

        for (AnnotationAssist<Column> columnAst : columns) {

            Set<String> tags = buildTags(parentComp, columnField, columnAst.unwrap().tags());

            // Check tags
            // TODO: check tags

            // Build group path
            String groupPath = buildColumnTags(parentComp, columnField, columnAst);
            if (fieldGroups.contains(groupPath)) {
                throw CodegenException.of()
                        .message("Duplicate column group '" + groupPath
                                + "' on field '" + parentComp.getOriginType().getName() + '.' +
                                columnField.getName() + "'.")
                        .element(parentComp.getOriginField()).build();
            }
            fieldGroups.add(groupPath);
            logger.debug("Group path: {}", groupPath);

            // Check group path
            if (!isInGroup(parentComp, groupPath)) {
                logger.debug("Skipped by group: {}", groupPath);
                continue;
            }

            // Construct origin column name
            String columnName;
            if (columnAst.unwrap().name().equals(Column.FIELD_REF) || StringUtils.isBlank(columnAst.unwrap().name())) {
                columnName = StrUtils.toSeparatorNotation(columnField.getName(), '_');
            } else {
                columnName = StringUtils.trim(columnAst.unwrap().name());
            }

            // Construct column definition
            String definition = null;
            if (StringUtils.isNotBlank(columnAst.unwrap().definition())) {
                definition = StringUtils.trim(columnAst.unwrap().definition());
            }

            // Construct mediator type
            TypeMirror mediatorTypeMirror = columnAst.getValueTypeMirror(Column::mediator);
            ClassType mediator = null;

            // Test mediatorTypeMirror!=FieldMediator.calss
            if (!CodegenUtils.isAssignable(FieldMediator.class, mediatorTypeMirror, processingEnv)) {
                mediator = new ClassType(processingEnv, (DeclaredType) mediatorTypeMirror);
            }

            String insertAs = StringUtils.trim(columnAst.unwrap().insertAs());
            if (StringUtils.isBlank(insertAs)) {
                insertAs = Column.FIELD_REF;
            }

            String updateAs = StringUtils.trim(columnAst.unwrap().updateAs());

            if (StringUtils.isBlank(updateAs)) {
                updateAs = Column.INSERT_AS_REF;
            }

            String selectAs = StringUtils.trim(columnAst.unwrap().selectAs());
            if (StringUtils.isBlank(selectAs)) {
                selectAs = Column.COLUMN_REF;
            }

            // Find and apply column overriding
            List<ColumnOverridingElement> overridingList = findColumnOverridings(parentComp, columnName);
            for (ColumnOverridingElement overriding : overridingList) {
                logger.debug("Overriding for column: {}; overriding: {}", columnName, overriding);
                overriding.setAssociated(true);

                if (overriding.getName() != null) {
                    columnName = overriding.getName();
                }

                if (overriding.getDefinition() != null) {
                    columnName = overriding.getDefinition();
                }

                if (overriding.getMediator() != null) {
                    mediator = overriding.getMediator();
                }
            }

            // Apply naming strategy from current composition to root
            CompositionElement c = parentComp;
            while (c != null) {
                columnName = applyColumnRenaming(c, columnField, columnName);
                c = c.getParentComposition();
            }

            // Build column element

            ColumnElement column = new ColumnElement(columnField, columnName, groupPath,tags);
            parentComp.addColumn(column);

            column.setDefinition(definition);
            column.setMediator(mediator);

            column.setImportable(columnAst.unwrap().importable());
            column.setExportable(columnAst.unwrap().exportable());

            // insertAs

            if (!Column.NOP_REF.equals(insertAs)) {
                if (insertAs.equals(Column.UPDATE_AS_REF)) {
                    if (!Column.NOP_REF.equals(updateAs)) {
                        column.setInsertAs(updateAs);
                    }
                } else {
                    column.setInsertAs(insertAs);
                }
            }

            // updateAs

            if (!Column.NOP_REF.equals(updateAs)) {
                if (updateAs.equals(Column.INSERT_AS_REF)) {
                    if (!Column.NOP_REF.equals(insertAs)) {
                        column.setUpdateAs(insertAs);
                    }
                } else {
                    column.setUpdateAs(updateAs);
                }
            }

            // selectAs

            if (!Column.NOP_REF.equals(selectAs)) {
                column.setSelectAs(selectAs);
            }

            // definition

            if (!Column.NOP_REF.equals(definition)) {
                if (StringUtils.isEmpty(definition)) {
                    column.setDefinition("[COLUMN DEFINITION]");
                } else {
                    column.setDefinition(definition);
                }
            }

        }
    }

    private List<AnnotationAssist<Composition>> findFieldCompositions(FieldElement field) {
        final List<AnnotationAssist<Composition>> result = new ArrayList<>();

        AnnotationAssist<Composition> composition = field.getAnnotation(Composition.class);
        if (composition != null) {
            result.add(composition);
        } else {
            AnnotationAssist<Compositions> compositions = field.getAnnotation(Compositions.class);
            if (compositions != null) {
                for (Composition comp : compositions.unwrap().value()) {
                    composition = new AnnotationAssist<>(processingEnv, comp);
                    result.add(composition);
                }
            }
        }
        return result;
    }

    protected void parseComposition(final CompositionElement composition) {
        logger.debug("Parse record composition: {}", composition);

        List<FieldElement> fields = composition.getOriginType().asClassElement().getFieldsFiltered(
                f -> !f.unwrap().getModifiers().contains(Modifier.STATIC)
        );

        // Process record fields
        for (FieldElement field : fields) {
            logger.debug("Parse record field: {} of type {}", field.getName(), field.unwrap().asType());

            // Parse field columns
            parseFieldColumns(composition, field);

            // Parse field compositions
            parseFieldCompositions(composition, field);

        }

        // check
        checkColumnOverridingsAssociated(composition);
    }

    /**
     * Check column overriding was really associated with a columns
     */
    protected void checkColumnOverridingsAssociated(final CompositionElement composition) {
        for (ColumnOverridingElement overriding : composition.getColumnOverriding()) {
            if (!overriding.isAssociated()) {

                String path = buildCompositionPath(composition, null);
                throw CodegenException.of()
                        .message("Composition '" + recordKitElement.getRecordType().unwrap() + "." + path + "' column overriding has not associated column: " + overriding.getColumnPath())
                        .element(composition.getOriginField().unwrap())
                        .build();

            }
        }
    }

    /**
     * Extract record type from record kit interface  (generic parameter)
     */
    protected ClassType getRecordTypeFromKit(ClassElement recordKitInterface) {

        ClassType superClass = null;
        List<ClassType> interfaces = recordKitInterface.getInterfaces();
        for (ClassType iface : interfaces) {
            if (iface.getErasure().toString().equals(RecordKitApi.class.getCanonicalName())) {
                superClass = iface;
                break;
            }
        }

        if (superClass == null) {
            throw CodegenException.of().element(recordKitInterface.unwrap()).message("Not extends " + RecordKitApi.class.getName()).build();
        }

        if (superClass.unwrap().getTypeArguments().size() != 1) {
            throw CodegenException.of().element(recordKitInterface.unwrap()).message("Unable to extract record type").build();
        }

        TypeMirror recordMirror = superClass.unwrap().getTypeArguments().get(0);
        ClassType recordType = new ClassType(processingEnv, (DeclaredType) recordMirror);

        return recordType;
    }

    /**
     * Parse given record view
     */
    protected RecordKitElement parseRecordView(ClassElement recordKitClass, RecordViewElement view) {

        ClassType recordType = getRecordTypeFromKit(recordKitClass);

        AnnotationAssist<RecordKit> configAnn = recordKitClass.getAnnotation(RecordKit.class);
        String tableName = configAnn.unwrap().table();
        TypeMirror extendMirror = configAnn.getValueTypeMirror(RecordKit::superclass);
        ClassType extendType = new ClassType(processingEnv, (DeclaredType) extendMirror);
        recordKitElement = new RecordKitElement(view, recordKitClass, recordType, extendType, tableName);

        // Add master table alias if specified
        if (StringUtils.isNotBlank(configAnn.unwrap().tableAlias())) {
            recordKitElement.addTableAlias(configAnn.unwrap().tableAlias(), configAnn.unwrap().table());
        }

        // Parse Joint Record Kits
        TypeMirror[] jointRecordKits = configAnn.getValueTypeMirrors(RecordKit::join);
        for (TypeMirror jrk : jointRecordKits) {
            ClassElement jointRecordKitClass = ClassElement.fromType(processingEnv, (DeclaredType) jrk);
            AnnotationAssist<RecordKit> jointConfigAnn = jointRecordKitClass.getAnnotation(RecordKit.class);
            String jointTableName = jointConfigAnn.unwrap().table();

            ClassType jointRecordType = getRecordTypeFromKit(jointRecordKitClass);

            JointRecord rec = new JointRecord(jointTableName, jointRecordType);
            recordKitElement.addJointRecord(rec);
            recordKitElement.addTableAlias(jointConfigAnn.unwrap().tableAlias(), jointTableName);
        }

        // Parse record compositions and columns
        parseComposition(recordKitElement.getRootComposition());
        return recordKitElement;
    }

    public ViewSetElement parseRecord(ClassElement recordKitClass) {
        logger.debug("Parse record kit interface: {} ", recordKitClass);

        AnnotationAssist<RecordKit> rkConfigAnn = recordKitClass.getAnnotation(RecordKit.class);
        // Get declared views
        RecordView[] declaredViews = rkConfigAnn.unwrap().views();

        // Build views
        Set<RecordViewElement> views = new HashSet<>();
        for (RecordView declaredView : declaredViews) {
            // View name should be class name compatible
            checkViewName(declaredView.name(), recordKitClass.unwrap());
            RecordViewElement view = new RecordViewElement(declaredView.name(), declaredView.tagFilter());
            views.add(view);
        }

        // Parse each view
        ViewSetElement viewSetElm = new ViewSetElement();
        for (RecordViewElement view : views) {
            RecordKitElement kitElm = parseRecordView(recordKitClass, view);
            viewSetElm.addRecordKit(view, kitElm);
        }

        return viewSetElm;
    }
}
