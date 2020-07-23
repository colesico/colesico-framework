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

    public static final String OP_NOP = "@nop";
    public static final String OU_UPDATE = "@update";
    public static final String OP_INSERT = "@insert";
    /**
     * Parsing record
     */
    protected RecordKitElement recordKitElement;

    public RecordKitParser(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    protected String rewriteName(String acceptedChain, String defaultName) {
        int pos = StringUtils.indexOf(acceptedChain, '=');
        if (pos < 0) {
            return defaultName;
        } else {
            return StringUtils.trim(StringUtils.substring(acceptedChain, pos + 1));
        }
    }

    protected void checkViewNames(final String[] views, Element parentElement) {
        for (String p : views) {
            if (!p.matches("[A-Za-z0-9]+")) {
                throw CodegenException.of().message("Invalid record view name. Alphanumeric chars expected: " + p).element(parentElement).build();
            }
        }
    }

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

    protected String buildAccessChain(CompositionElement comp, String localName) {
        Deque<String> namesStack = new ArrayDeque<>();

        if (localName != null) {
            namesStack.push(localName);
        }

        CompositionElement current = comp;
        while (current.getOriginField() != null) {
            namesStack.push(current.getOriginField().getName());
            current = current.getParentComposition();
        }

        return StringUtils.join(namesStack, ".");
    }

    protected String acceptChain(final CompositionElement composition, String localName, boolean isOption) {
        String accessChain = buildAccessChain(composition, localName);
        logger.debug("Check access chain: " + accessChain);

        // Build all access chains set
        Set<String> acceptedChains = new HashSet<>();
        CompositionElement current = composition;
        while (current != null) {
            if (composition.getImportedColumns() != null) {
                for (String lcName : composition.getImportedColumns()) {
                    //logger.debug("Add accepted chain: "+lcName);
                    String chain = buildAccessChain(composition, lcName);
                    acceptedChains.add(chain);
                }
            }
            current = current.getParentComposition();
        }

        if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Accepted chains: ");
            for (String acceptedChain : acceptedChains) {
                sb.append(acceptedChain).append("; ");
            }
            logger.debug(sb.toString());
        }

        if (acceptedChains.isEmpty()) {
            if (isOption) {
                return null;
            } else {
                return accessChain;
            }
        }

        for (String acceptedChain : acceptedChains) {
            if (StringUtils.startsWith(acceptedChain, accessChain)) {
                return acceptedChain;
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

    protected void parseColumn(final CompositionElement composition, FieldElement field) {
        final List<Column> columns = getColumns(field);
        for (Column columnAnn : columns) {

            // Check view
            if (!isInView(composition.getParentRecordKit().getView(), columnAnn.views())) {
                continue;
            }

            AnnotationAssist<Column> columnAnnElm = new AnnotationAssist<>(processingEnv, columnAnn);

            String name;
            if (columnAnnElm.unwrap().name().equals("@field")) {
                name = StrUtils.toSeparatorNotation(field.getName(), '_');
            } else {
                name = columnAnnElm.unwrap().name();
            }

            String acceptedChain = acceptChain(composition, name, columnAnnElm.unwrap().virtual());
            if (acceptedChain == null) {
                continue;
            }

            String columnName = StringUtils.trim(composition.getNamePrefix() + rewriteName(acceptedChain, name));
            ColumnElement columnElement = new ColumnElement(field, columnName);
            composition.addColumn(columnElement);

            columnElement.setImportable(columnAnnElm.unwrap().importable());
            columnElement.setExportable(columnAnnElm.unwrap().exportable());

            TypeMirror mediatorType = columnAnnElm.getValueTypeMirror(Column::mediator);
            if (!mediatorType.toString().equals(FieldMediator.class.getName())) {
                columnElement.setMediator(new ClassType(processingEnv, (DeclaredType) mediatorType));
            }

            String insertAs = StringUtils.trim(columnAnnElm.unwrap().insertAs());
            String updateAs = StringUtils.trim(columnAnnElm.unwrap().updateAs());
            String selectAs = StringUtils.trim(columnAnnElm.unwrap().selectAs());

            // insertAs
            if (!OP_NOP.equals(insertAs)) {
                if (insertAs.equals(OU_UPDATE)) {
                    if (!OP_NOP.equals(updateAs)) {
                        columnElement.setInsertAs(updateAs);
                    }
                } else {
                    columnElement.setInsertAs(insertAs);
                }
            }

            // updateAs
            if (!OP_NOP.equals(updateAs)) {
                if (updateAs.equals(OP_INSERT)) {
                    if (!OP_NOP.equals(insertAs)) {
                        columnElement.setUpdateAs(insertAs);
                    }
                } else {
                    columnElement.setUpdateAs(updateAs);
                }
            }

            // selectAs
            if (!OP_NOP.equals(selectAs)) {
                columnElement.setSelectAs(selectAs);
            }

            // definition
            if (!OP_NOP.equals(columnAnnElm.unwrap().definition())) {
                if (StringUtils.isEmpty(columnAnnElm.unwrap().definition())) {
                    columnElement.setDefinition("[COLUMN DEFINITION]");
                } else {
                    columnElement.setDefinition(columnAnnElm.unwrap().definition());
                }
            }

        }
    }

    private List<Composition> getCompositions(FieldElement field) {
        final List<Composition> compositions = new ArrayList<>();

        AnnotationAssist<Composition> compositionAnnElm = field.getAnnotation(Composition.class);
        if (compositionAnnElm != null) {
            compositions.add(compositionAnnElm.unwrap());
        } else {
            AnnotationAssist<Compositions> compositionsAnnElm = field.getAnnotation(Compositions.class);
            if (compositionsAnnElm != null) {
                compositions.addAll(Arrays.asList(compositionsAnnElm.unwrap().value()));
            }
        }
        return compositions;
    }

    protected void parseComposition(final CompositionElement composition) {
        logger.debug("Parse record composition: " + composition);

        List<FieldElement> fields = composition.getOriginType().asClassElement().getFieldsFiltered(
                f -> !f.unwrap().getModifiers().contains(Modifier.STATIC)
        );

        for (FieldElement field : fields) {
            logger.debug("Process record field: {} of type {}", field.getName(), field.unwrap().asType());
            final List<Composition> compositions = getCompositions(field);
            for (Composition compAnn : compositions) {

                AnnotationAssist<Composition> compositionAnn = new AnnotationAssist<>(processingEnv, compAnn);

                // Check view
                if (!isInView(composition.getParentRecordKit().getView(), compositionAnn.unwrap().views())) {
                    continue;
                }

                // Filter compositions that contains only not acceptable fields
                if (acceptChain(composition, field.getName(), false) == null) {
                    continue;
                }

                ClassType compositionType = field.asClassType();
                CompositionElement subComposition = new CompositionElement(recordKitElement, compositionType, field);

                subComposition.setNamePrefix(composition.getNamePrefix() + compositionAnn.unwrap().columnsPrefix());

                String tableName;

                JointRecord jointRecord = composition.getParentRecordKit().getJointRecords().get(compositionType);
                if (jointRecord != null) {
                    tableName = jointRecord.getTableName();
                } else {
                    tableName = composition.getTableName();
                }

                subComposition.setTableName(tableName);

                if (compositionAnn.unwrap().columns().length > 0) {
                    subComposition.setImportedColumns(compositionAnn.unwrap().columns());
                }

                if (StringUtils.isNoneBlank(compositionAnn.unwrap().keyColumn())) {
                    subComposition.setKeyColumn(compositionAnn.unwrap().keyColumn());
                }

                composition.addSubComposition(subComposition);
                parseComposition(subComposition);
                break;
            }
            parseColumn(composition, field);
        }
    }

    protected ClassType getRecordTypeFromKit(ClassElement recordKitClass) {

        ClassType superClass = null;
        List<ClassType> interfaces = recordKitClass.getInterfaces();
        for (ClassType iface : interfaces) {
            if (iface.getErasure().toString().equals(RecordKit.class.getName())) {
                superClass = iface;
                break;
            }
        }

        if (superClass == null) {
            throw CodegenException.of().element(recordKitClass.unwrap()).message("Not extends " + RecordKit.class.getName()).build();
        }

        if (superClass.unwrap().getTypeArguments().size() != 1) {
            throw CodegenException.of().element(recordKitClass.unwrap()).message("Unable to extract record type").build();
        }

        TypeMirror recordMirror = superClass.unwrap().getTypeArguments().get(0);
        ClassType recordType = new ClassType(processingEnv, (DeclaredType) recordMirror);

        return recordType;
    }

    protected RecordKitElement parseRecordView(ClassElement recordKitClass, String view) {

        ClassType recordType = getRecordTypeFromKit(recordKitClass);

        AnnotationAssist<RecordKitConfig> configAnn = recordKitClass.getAnnotation(RecordKitConfig.class);
        String tableName = configAnn.unwrap().table();
        TypeMirror extendMirror = configAnn.getValueTypeMirror(RecordKitConfig::extend);
        ClassType extendType = new ClassType(processingEnv, (DeclaredType) extendMirror);
        recordKitElement = new RecordKitElement(view, recordKitClass, recordType, extendType, tableName);

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
        }

        parseComposition(recordKitElement.getRootComposition());
        return recordKitElement;
    }

    public ViewSetElement parse(ClassElement recordKitClass) {
        logger.debug("Parse record kit: " + recordKitClass);

        AnnotationAssist<RecordKitConfig> configAnn = recordKitClass.getAnnotation(RecordKitConfig.class);
        String[] views = configAnn.unwrap().views();
        checkViewNames(views, recordKitClass.unwrap());

        List<String> viewsList = new ArrayList<>();
        for (String prof : views) {
            if (!RecordView.ALL_VIEWS.equals(prof)) {
                viewsList.add(prof);
            }
        }

        ViewSetElement viewSetElm = new ViewSetElement();
        for (String view : viewsList) {
            RecordKitElement recordElm = parseRecordView(recordKitClass, view);
            viewSetElm.addRecordKit(view, recordElm);
        }

        return viewSetElm;
    }
}
