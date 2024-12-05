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

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.jdbirec.Record;
import colesico.framework.jdbirec.*;
import colesico.framework.jdbirec.codegen.model.*;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class RecordKitParser extends RecordKitHelpers {

    /**
     * Parsing record
     */
    protected RecordKitElement recordKit;

    /**
     * Parsing record
     */
    protected RecordElement record;

    /**
     * Parsing view
     */
    protected RecordViewElement view;

    public RecordKitParser(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    protected void parseTagFilter(ContainerElement cont, TagFilter tagFilterAnn) {
        TagFilterElement tagFilter = new TagFilterElement();
        if (tagFilterAnn != null) {
            tagFilter.setAnyOf(Arrays.asList(tagFilterAnn.anyOf()));
            tagFilter.setNoneOf(Arrays.asList(tagFilterAnn.noneOf()));
        }
        cont.setTagFilter(tagFilter);
    }

    /**
     * Parse field columns
     */
    protected ColumnElement parseColumn(
            final ContainerElement container,
            final FieldElement field,
            final AnnotationAssist<Column> columnAnn
    ) {

        logger.debug("Parse column:{} on field: {} of container: {}", columnAnn, field.getName(), container);

        // Construct origin column name
        String name = buildColumnName(columnAnn, field);

        Set<String> tags = buildTags(container, field, name, columnAnn.unwrap().tags());

        // Check tags by filter
        if (!acceptTagsByFilter(container, tags)) {
            return null;
        }

        // Construct mediator type
        TypeMirror mediatorTypeMirror = columnAnn.getValueTypeMirror(Column::mediator);
        ClassType mediator = null;

        // Test mediatorTypeMirror!=FieldMediator.calss
        if (!CodegenUtils.isAssignable(FieldMediator.class, mediatorTypeMirror, processingEnv)) {
            mediator = new ClassType(processingEnv, (DeclaredType) mediatorTypeMirror);
        }

        // Construct column definition
        String definition = StringUtils.trim(columnAnn.unwrap().definition());
        if (StringUtils.isBlank(definition)) {
            definition = Column.AS_NOP;
        }

        String insertAs = StringUtils.trim(columnAnn.unwrap().insertAs());
        if (StringUtils.isBlank(insertAs)) {
            insertAs = Column.AS_FIELD;
        }

        String updateAs = StringUtils.trim(columnAnn.unwrap().updateAs());

        if (StringUtils.isBlank(updateAs)) {
            updateAs = Column.AS_INSERT;
        }

        String selectAs = StringUtils.trim(columnAnn.unwrap().selectAs());
        if (StringUtils.isBlank(selectAs)) {
            selectAs = Column.AS_COLUMN;
        }

        // Find and apply column overriding
        Set<ColumnOverridingElement> overridings = findColumnOverridings(container, name);
        for (ColumnOverridingElement overriding : overridings) {
            logger.debug("Overriding for column: {}; overriding: {}", name, overriding);
            overriding.setAssociated(true);

            if (overriding.getName() != null) {
                name = overriding.getName();
            }

            if (overriding.getDefinition() != null) {
                name = overriding.getDefinition();
            }

            if (overriding.getMediator() != null) {
                mediator = overriding.getMediator();
            }
        }

        // Apply renamig from current composition to root
        ContainerElement c = container;
        while (c != null) {
            name = applyColumnRenaming(c, name);
            c = c instanceof CompositionElement comp ? comp.getContainer() : null;
        }

        // Build column element

        ColumnElement column = new ColumnElement(field, name, tags);
        container.addColumn(column);

        column.setMediator(mediator);

        column.setImportable(columnAnn.unwrap().importable());
        column.setExportable(columnAnn.unwrap().exportable());

        // insertAs
        if (!Column.AS_NOP.equals(insertAs)) {
            if (insertAs.equals(Column.AS_UPDATE)) {
                if (!Column.AS_NOP.equals(updateAs)) {
                    column.setInsertAs(updateAs);
                }
            } else {
                column.setInsertAs(insertAs);
            }
        }

        // updateAs
        if (!Column.AS_NOP.equals(updateAs)) {
            if (updateAs.equals(Column.AS_INSERT)) {
                if (!Column.AS_NOP.equals(insertAs)) {
                    column.setUpdateAs(insertAs);
                }
            } else {
                column.setUpdateAs(updateAs);
            }
        }

        // selectAs
        if (!Column.AS_NOP.equals(selectAs)) {
            column.setSelectAs(selectAs);
        }

        // definition
        if (!Column.AS_NOP.equals(definition)) {
            column.setDefinition(definition);
        }

        return column;
    }


    private void parseColumnOverridings(ContainerElement cont, ColumnOverriding[] overridingsAnn) {
        for (ColumnOverriding co : overridingsAnn) {

            AnnotationAssist<ColumnOverriding> overridingAnn = AnnotationAssist.of(processingEnv, co);

            String columnName = overridingAnn.unwrap().column();
            String columnPath = buildContainerPath(cont, columnName);
            ColumnOverridingElement overriding = new ColumnOverridingElement(columnPath);

            // name overriding
            if (StringUtils.isNoneBlank(overridingAnn.unwrap().name())) {
                overriding.setName(StringUtils.trim(co.name()));
            }

            // definition overriding
            if (StringUtils.isNoneBlank(overridingAnn.unwrap().definition())) {
                overriding.setName(StringUtils.trim(overridingAnn.unwrap().definition()));
            }

            // mediator overriding
            TypeMirror mediatorType = overridingAnn.getValueTypeMirror(ColumnOverriding::mediator);
            if (!CodegenUtils.isAssignable(FieldMediator.class, mediatorType, processingEnv)) {
                overriding.setMediator(new ClassType(processingEnv, (DeclaredType) mediatorType));
            }

            cont.addColumnOverriding(overriding);
        }
    }

    private void parseContainerFields(ContainerElement cont) {
        List<FieldElement> fields = cont.getType().asClassElement().getFieldsFiltered(
                f -> !f.unwrap().getModifiers().contains(Modifier.STATIC)
        );

        for (FieldElement field : fields) {
            logger.debug("Parse field: {} of type {}", field.getName(), field.unwrap().asType());

            // Parse field columns
            final Set<AnnotationAssist<Column>> columnsAnn = findFieldColumns(field);
            ColumnElement column = null;
            for (AnnotationAssist<Column> columnAnn : columnsAnn) {
                ColumnElement c = parseColumn(cont, field, columnAnn);
                if (c != null) {
                    if (column == null) {
                        column = c;
                    } else {
                        throw CodegenException.of()
                                .message("Multiple columns for field: " + field.getName())
                                .build();
                    }
                }

            }

            // Parse nested compositions

            if (column == null) {
                Set<AnnotationAssist<Composition>> compsAnn = findFieldCompositions(field);
                CompositionElement comp = null;
                for (AnnotationAssist<Composition> compAnn : compsAnn) {
                    CompositionElement c = parseComposition(cont, field, compAnn);
                    if (c != null) {
                        if (comp == null) {
                            comp = c;
                        } else {
                            throw CodegenException.of().message("Multiple compositions for field: " + field.getName()).build();
                        }
                    }

                }
            }
        }
    }

    /**
     * Parse joint record
     */
    protected JointRecord parseJoinRecord(ClassType compositionType) {

        AnnotationAssist<Record> jointRecordKitAnn = compositionType.asClassElement().getAnnotation(Record.class);
        if (jointRecordKitAnn == null) {
            throw CodegenException.of().message("No @Record annotation on joint composition: " + compositionType).build();
        }
        String jointTableName = StringUtils.trim(jointRecordKitAnn.unwrap().table());
        String jointTableAlias = getTableAlias(jointRecordKitAnn, jointTableName);

        JointRecord rec = new JointRecord(jointTableName, compositionType);
        recordKit.addJointRecord(rec);
        recordKit.addTableAlias(jointTableAlias, jointTableName);

        return rec;
    }

    protected CompositionElement parseComposition(final ContainerElement container,
                                                  final FieldElement field,
                                                  final AnnotationAssist<Composition> compAnn) {

        logger.debug("Parse composition {} for parent container: {} on field {}", compAnn, container, field);

        // Build composition name
        String name = buildCompositionName(compAnn, field);

        // Build composition tags
        Set<String> tags = buildTags(container, field, name, compAnn.unwrap().tags());

        // Check tags by filter
        if (!acceptTagsByFilter(container, tags)) {
            return null;
        }

        CompositionElement comp = new CompositionElement(record, container, field, name, tags);
        container.addComposition(comp);

        // Set renaming
        comp.setRenaming(compAnn.unwrap().renaming());

        // Set null instance
        comp.setNullInstance(compAnn.unwrap().nullInstace());

        // Parse and set tagFilter
        parseTagFilter(comp, compAnn.unwrap().tagFilter());

        // Parse column overriding
        parseColumnOverridings(comp, compAnn.unwrap().columnOverriding());

        // Set table name
        // Check that this composition specified as a  joint record
        JointRecord jointRecord = null;
        if (compAnn.unwrap().join()) {
            jointRecord = parseJoinRecord(comp.getType());
        }
        String tableName;
        if (jointRecord != null) {
            tableName = jointRecord.getTableName();
        } else {
            tableName = container.getTableName();
        }
        comp.setTableName(tableName);

        // Process composition fields
        parseContainerFields(comp);

        return comp;
    }

    protected RecordViewElement parseRecordView(
            final ClassType type,
            final AnnotationAssist<RecordView> viewAnn) {

        logger.debug("Parse record view: {} of type: {};", viewAnn, type);

        // Build composition name
        String viewName = viewAnn.unwrap().name();
        if (StringUtils.isBlank(viewName)) {
            viewName = RecordView.VIEW_DEFAULT;
        }
        checkViewName(viewName, type.asTypeElement());

        view = new RecordViewElement(record, type, viewName);
        record.addView(view);

        // Set record default table name
        view.setTableName(record.getTableName());

        // Set renaming
        view.setRenaming(viewAnn.unwrap().renaming());

        // Set tagFilter
        parseTagFilter(view, viewAnn.unwrap().tagFilter());

        // Parse column overriding
        parseColumnOverridings(view, viewAnn.unwrap().columnOverriding());

        // Process composition fields
        parseContainerFields(view);

        return view;
    }

    protected RecordElement parseRecord(ClassType recordType) {

        AnnotationAssist<Record> recordAnn = recordType.asClassElement().getAnnotation(Record.class);

        String tableName = StringUtils.trim(recordAnn.unwrap().table());
        if (StringUtils.isBlank(tableName)) {
            throw CodegenException.of()
                    .message("Unspecified table name for record: " + recordType)
                    .element(recordType.asClassElement())
                    .build();
        }

        String tableAlias = getTableAlias(recordAnn, tableName);

        record = new RecordElement(recordKit, recordType, tableName, tableAlias);
        recordKit.setRecord(record);

        // Add master table alias if specified
        recordKit.addTableAlias(tableAlias, tableName);

        // Parse record views
        for (RecordView viewAnn : recordAnn.unwrap().views()) {
            parseRecordView(recordType, AnnotationAssist.of(processingEnv, viewAnn));
        }

        for (RecordViewElement view : record.getViews()) {
            validateRecordView(view);
        }

        return record;
    }

    public RecordKitElement parseRecordKit(ClassElement recordKitClass) {
        logger.debug("Parse record kit: {} ", recordKitClass);

        AnnotationAssist<RecordKit> recordKitAnn = recordKitClass.getAnnotation(RecordKit.class);

        TypeMirror superclassMirror = recordKitAnn.getValueTypeMirror(RecordKit::superclass);
        ClassType superclassType = new ClassType(processingEnv, (DeclaredType) superclassMirror);
        recordKit = new RecordKitElement(recordKitClass, superclassType);

        ClassType recordType = getRecordTypeFromKit(recordKitClass);

        parseRecord(recordType);

        return recordKit;

    }
}
