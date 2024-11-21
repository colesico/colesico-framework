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
import colesico.framework.jdbirec.RecordKitConfig;
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
    protected void checkViewNames(final String[] views, Element parentElement) {
        for (String p : views) {
            if (!p.matches("[A-Za-z0-9]+")) {
                throw CodegenException.of().message("Invalid record view name. Alphanumeric chars expected: " + p).element(parentElement).build();
            }
        }
    }

    /**
     * Check that the given views matches current view
     */
    protected boolean isInView(String currentView, String[] views) {
        for (String p : views) {
            if (currentView.equals(p)) {
                return true;
            } else if (RecordView.ALL_VIEWS.equals(p)) {
                return true;
            }
        }
        return false;
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

        // Build composition chain from root to this
        CompositionElement c = columnComposition;
        List<CompositionElement> compositionChain = new ArrayList<>();
        while (c != null) {
            compositionChain.add(c);
            c = c.getParentComposition();
        }
        // Collections.reverse(compositionChain);

        // Lookup overriding definition within compositions, starts from root composition
        List<ColumnOverridingElement> columnOverridings = new ArrayList<>();

        String columnPath = buildCompositionPath(columnComposition, columnName);
        for (CompositionElement ce : compositionChain) {
            logger.debug("Check composition column overriding: {}:{} ", ce, ce.getColumnOverriding().size());

            if (ce.getColumnOverriding().isEmpty()) {
                continue;
            }

            // Find column within overriding
            for (ColumnOverridingElement coe : ce.getColumnOverriding()) {
                logger.debug("FindColumnOverriding:  {} ? {}", columnPath, coe);
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
    protected String findCompositionGroupPath(CompositionElement columnComposition, String columnGroup) {

        // Lookup compositions from current to root
        CompositionElement c = columnComposition;
        String columnGroupPath = buildCompositionPath(columnComposition, columnGroup);

        while (c != null) {
            for (String compositionGroupPath : c.getGroupPaths()) {
                if (compositionGroupPath.equals(columnGroupPath)) {
                    return columnGroupPath;
                }
            }
            c = c.getParentComposition();
        }
        return null;
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

    private List<Column> getFieldColumns(FieldElement field) {
        final List<Column> columns = new ArrayList<>();

        AnnotationAssist<Column> columnAnnElm = field.getAnnotation(Column.class);
        if (columnAnnElm != null) {
            columns.add(columnAnnElm.unwrap());
        } else {
            AnnotationAssist<Columns> columnsAnnElm = field.getAnnotation(Columns.class);
            if (columnsAnnElm != null) {
                columns.addAll(Arrays.asList(columnsAnnElm.unwrap().value()));
            }
        }
        return columns;
    }

    protected void parseFieldCompositions(final CompositionElement composition, FieldElement field) {

        final List<Composition> subcompAnnList = getCompositions(field);

        for (Composition subcompAnn : subcompAnnList) {

            AnnotationAssist<Composition> subcompAst = new AnnotationAssist<>(processingEnv, subcompAnn);

            // Check view
            if (!isInView(composition.getParentRecordKit().getView(), subcompAst.unwrap().views())) {
                continue;
            }

            ClassType subcompType = field.asClassType();
            CompositionElement subcomposition = new CompositionElement(recordKitElement, subcompType, field);
            composition.addSubComposition(subcomposition);

            // Set naming
            subcomposition.setNaming(subcompAst.unwrap().naming());

            // Set null instance
            subcomposition.setNullInstance(subcompAst.unwrap().nullInstace());

            // Parse groups
            List<String> groupPaths = new ArrayList<>();
            for (String group : subcompAst.unwrap().groups()) {
                // Calculate group path from root composition
                String groupPath = buildCompositionPath(subcomposition, group);
                groupPaths.add(groupPath);
            }
            subcomposition.setGroupPaths(groupPaths);

            // Check subcomposition is a joint composition
            JointRecord jointRecord = composition.getParentRecordKit().getJointRecords().get(subcompType);
            String tableName;
            if (jointRecord != null) {
                tableName = jointRecord.getTableName();
            } else {
                tableName = composition.getTableName();
            }
            subcomposition.setTableName(tableName);

            // Parse column overriding

            for (ColumnOverriding co : subcompAst.unwrap().columnOverriding()) {
                AnnotationAssist<ColumnOverriding> coa = new AnnotationAssist<>(processingEnv, co);

                String columnName = co.column();
                String columnPath = buildCompositionPath(subcomposition, columnName);
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

                subcomposition.overrideColumn(cre);
            }

            // Parse nested composition
            parseComposition(subcomposition);

            break;
        } // for compositionAnnList

    }

    protected String applyColumnNaming(CompositionElement composition, FieldElement columnField, String columnName) {
        if (StringUtils.isBlank(composition.getNaming())) {
            return columnName;
        }

        columnName = StringUtils.replace(composition.getNaming(), Composition.COLUMN_REF, columnName);
        columnName = StringUtils.replace(columnName, Composition.FILED_REF, columnField.getName());

        return columnName;
    }

    /**
     * Parse field columns
     */
    protected void parseFieldColumns(final CompositionElement composition, FieldElement field) {
        logger.debug("Parse columns on field: {} of composition: {}", field, composition);
        final List<Column> columnAnnList = getFieldColumns(field);
        for (Column columnAnn : columnAnnList) {

            // Check view
            if (!isInView(composition.getParentRecordKit().getView(), columnAnn.views())) {
                continue;
            }

            // Check group
            String columnGroupPath = findCompositionGroupPath(composition, columnAnn.group());
            if (columnGroupPath == null) {
                continue;
            }

            AnnotationAssist<Column> columnAst = new AnnotationAssist<>(processingEnv, columnAnn);

            // Construct origin column name
            String columnName;
            if (columnAst.unwrap().name().equals(Column.FIELD_REF) || StringUtils.isBlank(columnAst.unwrap().name())) {
                columnName = StrUtils.toSeparatorNotation(field.getName(), '_');
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
            if (!CodegenUtils.isAssignable(FieldMediator.class, mediatorTypeMirror, processingEnv)) {
                mediator = new ClassType(processingEnv, (DeclaredType) mediatorTypeMirror);
            } else {
                throw CodegenException.of()
                        .message("Mediator " + mediatorTypeMirror + " is not subclass of " + FieldMediator.class.getName())
                        .element(field)
                        .build();
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
            List<ColumnOverridingElement> overridingList = findColumnOverridings(composition, columnName);
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
            CompositionElement c = composition;
            while (c != null) {
                columnName = applyColumnNaming(c, field, columnName);
                c = c.getParentComposition();
            }

            // Build column element

            ColumnElement columnElement = new ColumnElement(field, columnName);
            composition.addColumn(columnElement);

            columnElement.setDefinition(definition);
            columnElement.setMediator(mediator);

            columnElement.setImportable(columnAst.unwrap().importable());
            columnElement.setExportable(columnAst.unwrap().exportable());

            // insertAs

            if (!Column.NOP_REF.equals(insertAs)) {
                if (insertAs.equals(Column.UPDATE_AS_REF)) {
                    if (!Column.NOP_REF.equals(updateAs)) {
                        columnElement.setInsertAs(updateAs);
                    }
                } else {
                    columnElement.setInsertAs(insertAs);
                }
            }

            // updateAs

            if (!Column.NOP_REF.equals(updateAs)) {
                if (updateAs.equals(Column.INSERT_AS_REF)) {
                    if (!Column.NOP_REF.equals(insertAs)) {
                        columnElement.setUpdateAs(insertAs);
                    }
                } else {
                    columnElement.setUpdateAs(updateAs);
                }
            }

            // selectAs

            if (!Column.NOP_REF.equals(selectAs)) {
                columnElement.setSelectAs(selectAs);
            }

            // definition

            if (!Column.NOP_REF.equals(definition)) {
                if (StringUtils.isEmpty(definition)) {
                    columnElement.setDefinition("[COLUMN DEFINITION]");
                } else {
                    columnElement.setDefinition(definition);
                }
            }

        }
    }

    private List<Composition> getCompositions(FieldElement field) {
        final List<Composition> compositions = new ArrayList<>();

        AnnotationAssist<Composition> compositionAst = field.getAnnotation(Composition.class);
        if (compositionAst != null) {
            compositions.add(compositionAst.unwrap());
        } else {
            AnnotationAssist<Compositions> compositionsAst = field.getAnnotation(Compositions.class);
            if (compositionsAst != null) {
                compositions.addAll(Arrays.asList(compositionsAst.unwrap().value()));
            }
        }
        return compositions;
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
            if (iface.getErasure().toString().equals(RecordKit.class.getCanonicalName())) {
                superClass = iface;
                break;
            }
        }

        if (superClass == null) {
            throw CodegenException.of().element(recordKitInterface.unwrap()).message("Not extends " + RecordKit.class.getName()).build();
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
    protected RecordKitElement parseRecordView(ClassElement recordKitClass, String view) {

        ClassType recordType = getRecordTypeFromKit(recordKitClass);

        AnnotationAssist<RecordKitConfig> configAnn = recordKitClass.getAnnotation(RecordKitConfig.class);
        String tableName = configAnn.unwrap().table();
        TypeMirror extendMirror = configAnn.getValueTypeMirror(RecordKitConfig::superclass);
        ClassType extendType = new ClassType(processingEnv, (DeclaredType) extendMirror);
        recordKitElement = new RecordKitElement(view, recordKitClass, recordType, extendType, tableName);

        // Add master table alias if specified
        if (StringUtils.isNotBlank(configAnn.unwrap().tableAlias())) {
            recordKitElement.addTableAlias(configAnn.unwrap().tableAlias(), configAnn.unwrap().table());
        }

        // Parse Joint Record Kits
        TypeMirror[] jointRecordKits = configAnn.getValueTypeMirrors(RecordKitConfig::join);
        for (TypeMirror jrk : jointRecordKits) {
            ClassElement jointRecordKitClass = ClassElement.fromType(processingEnv, (DeclaredType) jrk);
            AnnotationAssist<RecordKitConfig> jointConfigAnn = jointRecordKitClass.getAnnotation(RecordKitConfig.class);
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

    public ViewSetElement parse(ClassElement recordKitClass) {
        logger.debug("Parse record kit interface: {} ", recordKitClass);

        AnnotationAssist<RecordKitConfig> configAnn = recordKitClass.getAnnotation(RecordKitConfig.class);
        // Get declared views
        String[] views = configAnn.unwrap().views();
        // View name should be class name compatible
        checkViewNames(views, recordKitClass.unwrap());

        // Build view list
        List<String> viewsList = new ArrayList<>();
        for (String view : views) {
            if (!RecordView.ALL_VIEWS.equals(view)) {
                viewsList.add(view);
            }
        }

        // Parse each view
        ViewSetElement viewSetElm = new ViewSetElement();
        for (String view : viewsList) {
            RecordKitElement kitElm = parseRecordView(recordKitClass, view);
            viewSetElm.addRecordKit(view, kitElm);
        }

        return viewSetElm;
    }
}
