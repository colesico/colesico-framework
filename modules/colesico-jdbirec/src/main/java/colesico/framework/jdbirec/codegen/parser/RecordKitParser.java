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
     * CHeck view name should be class name compatible to generate class name suffix
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

    protected ColumnBindingElement findColumnBinding(CompositionElement compositionElement, String columnName) {
        // TODO
        return null;
    }

    /**
     * Builds access path string:  filed1.field2.name
     */
    protected String buildAccessPath(CompositionElement comp, String name) {
        Deque<String> namesStack = new ArrayDeque<>();

        namesStack.push(name);

        CompositionElement current = comp;
        while (current.getOriginField() != null) {
            namesStack.push(current.getOriginField().getName());
            current = current.getParentComposition();
        }

        return StringUtils.join(namesStack, ".");
    }

    /**
     * Not null if given access path  (defined by composition and local name) is accepted by
     * compositions chain {@link Composition#columns()}
     */
    protected String acceptPath(final CompositionElement composition, String columnName, boolean isVirtual) {
        // Verified access path
        String accessPath = buildAccessPath(composition, columnName);
        logger.debug("Check access chain: " + accessPath);

        // Build all access path set
        Set<String> acceptedPathSet = new HashSet<>();
        CompositionElement curComp = composition;
        while (curComp != null) {
            String path;
            if (composition.getColumnBindings().isEmpty()) {
                for (ColumnBindingElement cre : composition.getColumnBindings()) {
                    //logger.debug("Add accepted chain: "+lcName);
                    path = buildAccessPath(composition, cre.getName());
                    acceptedPathSet.add(path);
                }
            } else {
                for (ColumnBindingElement cre : composition.getColumnBindings()) {
                    //logger.debug("Add accepted chain: "+lcName);
                    path = buildAccessPath(composition, cre.getName());
                    acceptedPathSet.add(path);
                }
            }
            curComp = curComp.getParentComposition();
        }

        if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Accepted chains: ");
            for (String acceptedChain : acceptedPathSet) {
                sb.append(acceptedChain).append("; ");
            }
            logger.debug(sb.toString());
        }

        if (acceptedPathSet.isEmpty()) {
            if (isVirtual) {
                return null;
            } else {
                return accessPath;
            }
        }

        for (String acceptedPath : acceptedPathSet) {
            if (StringUtils.startsWith(acceptedPath, accessPath)) {
                return acceptedPath;
            }
        }

        return null;
    }

    private List<Column> getColumns(FieldElement field) {
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

    protected void parseColumns(final CompositionElement composition, FieldElement field) {
        final List<Column> columnAnnList = getColumns(field);
        for (Column columnAnn : columnAnnList) {

            // Check view
            if (!isInView(composition.getParentRecordKit().getView(), columnAnn.views())) {
                continue;
            }

            AnnotationAssist<Column> columnAst = new AnnotationAssist<>(processingEnv, columnAnn);

            String name;
            if (columnAst.unwrap().name().equals(Column.FIELD_REF)) {
                name = StrUtils.toSeparatorNotation(field.getName(), '_');
            } else {
                name = StringUtils.trim(columnAst.unwrap().name());
            }

            ColumnBindingElement binding = findColumnBinding(composition, name);

            if (binding == null) {
                if (columnAst.unwrap().virtual()) {
                    continue;
                }
                if (!composition.getColumnBindings().isEmpty()) {
                    return;
                }
            }

            // Build final column name

            String fullName;
            if (binding != null && binding.getName() != null) {
                fullName = binding.getName();
            } else {
                fullName = name;
            }
            fullName = composition.getColumnsPrefix() + fullName;

            ColumnElement columnElement = new ColumnElement(field, fullName);
            composition.addColumn(columnElement);

            columnElement.setImportable(columnAst.unwrap().importable());
            columnElement.setExportable(columnAst.unwrap().exportable());

            // Mediator type

            if (binding != null && binding.getMediator() != null) {
                columnElement.setMediator(binding.getMediator());
            } else {
                TypeMirror mediatorType = columnAst.getValueTypeMirror(Column::mediator);
                if (!CodegenUtils.isAssignable(FieldMediator.class, mediatorType, processingEnv)) {
                    columnElement.setMediator(new ClassType(processingEnv, (DeclaredType) mediatorType));
                }
            }

            String insertAs = StringUtils.trim(columnAst.unwrap().insertAs());
            String updateAs = StringUtils.trim(columnAst.unwrap().updateAs());

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

            String selectAs = StringUtils.trim(columnAst.unwrap().selectAs());

            // selectAs

            if (!Column.NOP_REF.equals(selectAs)) {
                columnElement.setSelectAs(selectAs);
            }

            // definition

            String definition = StringUtils.trim(columnAst.unwrap().definition());
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
        logger.debug("Parse record composition: " + composition);

        List<FieldElement> fields = composition.getOriginType().asClassElement().getFieldsFiltered(
                f -> !f.unwrap().getModifiers().contains(Modifier.STATIC)
        );

        // Process record fields
        for (FieldElement field : fields) {
            logger.debug("Parse record field: {} of type {}", field.getName(), field.unwrap().asType());

            // Parse compositions
            final List<Composition> compositionAnnList = getCompositions(field);
            for (Composition compositionAnn : compositionAnnList) {

                AnnotationAssist<Composition> compositionAst = new AnnotationAssist<>(processingEnv, compositionAnn);

                // Check view
                if (!isInView(composition.getParentRecordKit().getView(), compositionAst.unwrap().views())) {
                    continue;
                }

                ClassType compositionType = field.asClassType();
                CompositionElement subComposition = new CompositionElement(recordKitElement, compositionType, field);

                String columnsPrefix = composition.getColumnsPrefix() + StringUtils.trim(compositionAst.unwrap().columnsPrefix());
                subComposition.setColumnsPrefix(columnsPrefix);

                String tableName;

                // Check subcomposition is a joint composition
                JointRecord jointRecord = composition.getParentRecordKit().getJointRecords().get(compositionType);
                if (jointRecord != null) {
                    tableName = jointRecord.getTableName();
                } else {
                    tableName = composition.getTableName();
                }

                subComposition.setTableName(tableName);

                // Parse column bindings
                if (compositionAst.unwrap().columns().length > 0) {
                    for (ColumnBinding cb : compositionAst.unwrap().columns()) {
                        AnnotationAssist<ColumnBinding> cba = new AnnotationAssist<>(processingEnv, cb);

                        ColumnBindingElement cbe = new ColumnBindingElement(cb.column());
                        // name overriding
                        if (StringUtils.isNoneBlank(cb.name())) {
                            cbe.setName(cb.name());
                        }

                        // mediator overriding
                        TypeMirror mediatorType = cba.getValueTypeMirror(ColumnBinding::mediator);
                        if (!CodegenUtils.isAssignable(FieldMediator.class, mediatorType, processingEnv)) {
                            cbe.setMediator(new ClassType(processingEnv, (DeclaredType) mediatorType));
                        }

                        subComposition.bindColumn(cbe);
                    }
                }

                if (StringUtils.isNoneBlank(compositionAst.unwrap().keyColumn())) {
                    subComposition.setKeyColumn(columnsPrefix + compositionAst.unwrap().keyColumn());
                }

                composition.addSubComposition(subComposition);
                parseComposition(subComposition);
                break;
            }

            // Parse columns
            parseColumns(composition, field);
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
    protected RecordKitElement parseRecordView(ClassElement recordKitInterface, String view) {

        ClassType recordType = getRecordTypeFromKit(recordKitInterface);

        AnnotationAssist<RecordKitConfig> configAnn = recordKitInterface.getAnnotation(RecordKitConfig.class);
        String tableName = configAnn.unwrap().table();
        TypeMirror extendMirror = configAnn.getValueTypeMirror(RecordKitConfig::extend);
        ClassType extendType = new ClassType(processingEnv, (DeclaredType) extendMirror);
        recordKitElement = new RecordKitElement(view, recordKitInterface, recordType, extendType, tableName);

        /**
         * Add master table alias if specified
         */
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
        logger.debug("Parse record kit interface: " + recordKitClass);

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
