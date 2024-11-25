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
import colesico.framework.jdbirec.*;
import colesico.framework.jdbirec.RecordKit;
import colesico.framework.jdbirec.codegen.model.*;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.*;


public class RecordKitParser extends RecordKitParserHelpers {

    /**
     * Parsing record
     */
    protected RecordKitElement recordKitElement;

    public RecordKitParser(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    protected TagFilterElement parseTagFilter(TagFilter tagFilter) {
        TagFilterElement tagFilterElement = new TagFilterElement();
        if (tagFilter != null) {
            tagFilterElement.setAnyOf(Arrays.asList(tagFilter.anyOf()));
            tagFilterElement.setOneOf(Arrays.asList(tagFilter.oneOf()));
            tagFilterElement.setNoneOf(Arrays.asList(tagFilter.noneOf()));
        }
        return tagFilterElement;
    }

    /**
     * Parse field columns
     */
    protected ColumnElement parseColumn(
            final CompositionElement parentComp,
            final AnnotationAssist<Column> columnAnn,
            final FieldElement columnField) {

        logger.debug("Parse column:{} on field: {} of composition: {}", columnAnn, columnField.getName(), parentComp);

        // Construct origin column name
        String name = buildColumnName(columnAnn, columnField);

        Set<String> tags = buildTags(parentComp, columnField.getName(), columnAnn.unwrap().tags());

        // Check tags by filter
        if (!acceptTagsByFilter(parentComp, tags)) {
            return null;
        }

        // Construct column definition
        String definition = null;
        if (StringUtils.isNotBlank(columnAnn.unwrap().definition())) {
            definition = StringUtils.trim(columnAnn.unwrap().definition());
        }

        // Construct mediator type
        TypeMirror mediatorTypeMirror = columnAnn.getValueTypeMirror(Column::mediator);
        ClassType mediator = null;

        // Test mediatorTypeMirror!=FieldMediator.calss
        if (!CodegenUtils.isAssignable(FieldMediator.class, mediatorTypeMirror, processingEnv)) {
            mediator = new ClassType(processingEnv, (DeclaredType) mediatorTypeMirror);
        }

        String insertAs = StringUtils.trim(columnAnn.unwrap().insertAs());
        if (StringUtils.isBlank(insertAs)) {
            insertAs = Column.FIELD_REF;
        }

        String updateAs = StringUtils.trim(columnAnn.unwrap().updateAs());

        if (StringUtils.isBlank(updateAs)) {
            updateAs = Column.INSERT_AS_REF;
        }

        String selectAs = StringUtils.trim(columnAnn.unwrap().selectAs());
        if (StringUtils.isBlank(selectAs)) {
            selectAs = Column.COLUMN_REF;
        }

        // Find and apply column overriding
        Set<ColumnOverridingElement> overridings = findColumnOverridings(parentComp, name);
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

        // Apply naming strategy from current composition to root
        CompositionElement c = parentComp;
        while (c != null) {
            name = applyColumnRenaming(c, columnField, name);
            c = c.getParentComposition();
        }

        // Build column element

        ColumnElement column = new ColumnElement(columnField, name, tags);
        parentComp.addColumn(column);

        column.setDefinition(definition);
        column.setMediator(mediator);

        column.setImportable(columnAnn.unwrap().importable());
        column.setExportable(columnAnn.unwrap().exportable());

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

        return column;
    }


    private void parseColumnOverridings(CompositionElement comp, AnnotationAssist<Composition> compAnn) {
        for (ColumnOverriding co : compAnn.unwrap().columnOverriding()) {

            AnnotationAssist<ColumnOverriding> overridingAnn = new AnnotationAssist<>(processingEnv, co);

            String columnName = overridingAnn.unwrap().column();
            String columnPath = buildCompositionPath(comp, columnName);
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

            comp.overrideColumn(overriding);
        }
    }

    private void parseCompositionFields(CompositionElement comp) {
        List<FieldElement> fields = comp.getOriginType().asClassElement().getFieldsFiltered(
                f -> !f.unwrap().getModifiers().contains(Modifier.STATIC)
        );

        for (FieldElement field : fields) {
            logger.debug("Parse composition field: {} of type {}", field.getName(), field.unwrap().asType());

            // Parse field columns
            final Set<AnnotationAssist<Column>> columnsAnn = findFieldColumns(field);
            ColumnElement column = null;
            for (AnnotationAssist<Column> columnAnn : columnsAnn) {
                column = parseColumn(comp, columnAnn, field);
                if (column == null) {
                    continue;
                }
                break;
            }

            // Parse nested compositions

            if (column == null) {
                Set<AnnotationAssist<Composition>> subcompsAnn = findFieldCompositions(field);
                for (AnnotationAssist<Composition> subcompAnn : subcompsAnn) {
                    CompositionElement subcomp = parseComposition(comp, subcompAnn, field, null);
                    if (subcomp == null) {
                        continue;
                    }
                    break;
                }
            }
        }
    }

    protected CompositionElement parseComposition(final CompositionElement parentComp,
                                                  final AnnotationAssist<Composition> compAnn,
                                                  final FieldElement compField,
                                                  final ClassType compType) {
        logger.debug("Parse composition: {} for parent composition: {}; composition field: {}; composition type: {};", compAnn, parentComp, compField, compType);

        // Build composition name
        String name = buildCompositionName(compAnn, compField, compType);

        // Build composition tags
        Set<String> tags = buildTags(parentComp, name, compAnn.unwrap().tags());

        // Check tags by filter
        if (!acceptTagsByFilter(parentComp, tags)) {
            return null;
        }

        CompositionElement comp = new CompositionElement(recordKitElement, compType, name, tags, compField);
        parentComp.addSubComposition(comp);

        // Set renaming
        comp.setRenaming(compAnn.unwrap().renaming());

        // Set null instance
        comp.setNullInstance(compAnn.unwrap().nullInstace());

        // Set tagFilter
        comp.setTagFilter(parseTagFilter(compAnn.unwrap().tagFilter()));

        // Parse column overriding
        parseColumnOverridings(comp, compAnn);

        // Set table name
        // Check that this composition specified as a  joint record
        JointRecord jointRecord = comp.getParentRecordKit().getJointRecords().get(compType);
        String tableName;
        if (jointRecord != null) {
            tableName = jointRecord.getTableName();
        } else {
            tableName = parentComp.getTableName();
        }
        comp.setTableName(tableName);

        // Process composition fields
        parseCompositionFields(comp);

        checkColumnOverridingsAssociated(comp);

        return comp;
    }

    public RecordKitElement parseRecordKit(ClassElement recordKitClass) {
        logger.debug("Parse record kit interface: {} ", recordKitClass);

        ClassType recordType = getRecordTypeFromKit(recordKitClass);

        AnnotationAssist<RecordKit> recordKitAnn = recordKitClass.getAnnotation(RecordKit.class);

        String tableName = recordKitAnn.unwrap().table();
        TypeMirror superclassMirror = recordKitAnn.getValueTypeMirror(RecordKit::superclass);
        ClassType superclassType = new ClassType(processingEnv, (DeclaredType) superclassMirror);
        recordKitElement = new RecordKitElement(recordKitClass, recordType, superclassType, tableName);

        // Add master table alias if specified
        if (StringUtils.isNotBlank(recordKitAnn.unwrap().tableAlias())) {
            recordKitElement.addTableAlias(recordKitAnn.unwrap().tableAlias(), recordKitAnn.unwrap().table());
        }

        // Parse Joint Record Kits
        TypeMirror[] jointRecordKits = recordKitAnn.getValueTypeMirrors(RecordKit::join);
        for (TypeMirror jrk : jointRecordKits) {
            ClassElement jointRecordKitClass = ClassElement.fromType(processingEnv, (DeclaredType) jrk);
            AnnotationAssist<RecordKit> jointRecordKitAnn = jointRecordKitClass.getAnnotation(RecordKit.class);
            String jointTableName = jointRecordKitAnn.unwrap().table();

            ClassType jointRecordType = getRecordTypeFromKit(jointRecordKitClass);

            JointRecord rec = new JointRecord(jointTableName, jointRecordType);
            recordKitElement.addJointRecord(rec);
            recordKitElement.addTableAlias(jointRecordKitAnn.unwrap().tableAlias(), jointTableName);
        }

        // Parse root compositions
        Set<AnnotationAssist<Composition>> compsAnn = findTypeCompositions(recordType);
        for (AnnotationAssist<Composition> compAnn : compsAnn) {
            parseComposition(recordKitElement.getRootComposition(), compAnn, null, recordType);
        }

        return recordKitElement;

    }
}
