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
import colesico.framework.assist.codegen.model.AnnotationToolbox;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.jdbirec.*;
import colesico.framework.jdbirec.Record;
import colesico.framework.jdbirec.codegen.model.ColumnElement;
import colesico.framework.jdbirec.codegen.model.CompositionElement;
import colesico.framework.jdbirec.codegen.model.RecordElement;
import colesico.framework.jdbirec.codegen.model.ViewSetElement;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.*;

public class RecordParser extends FrameworkAbstractParser {

    public static final String OP_NOP = "@nop";
    public static final String OU_UPDATE = "@update";
    public static final String OP_INSERT = "@insert";
    /**
     * Parsing record
     */
    protected RecordElement recordElement;

    public RecordParser(ProcessingEnvironment processingEnv) {
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

        AnnotationToolbox<Column> columnAnnElm = field.getAnnotation(Column.class);
        if (columnAnnElm != null) {
            columns.add(columnAnnElm.unwrap());
        } else {
            AnnotationToolbox<Columns> columnsAnnElm = field.getAnnotation(Columns.class);
            if (columnsAnnElm != null) {
                columns.addAll(Arrays.asList(columnsAnnElm.unwrap().value()));
            }
        }
        return columns;
    }

    protected void parseColumn(final CompositionElement composition, FieldElement field, String namePrefix) {
        final List<Column> columns = getColumns(field);
        for (Column columnAnn : columns) {

            // Check view
            if (!isInView(composition.getParentRecord().getView(), columnAnn.views())) {
                continue;
            }

            AnnotationToolbox<Column> columnAnnElm = new AnnotationToolbox<>(processingEnv, columnAnn);

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

            String columnName = StringUtils.trim(namePrefix + rewriteName(acceptedChain, name));
            ColumnElement columnElement = new ColumnElement(field, columnName);
            composition.addColumn(columnElement);

            columnElement.setImportable(columnAnnElm.unwrap().importable());
            columnElement.setExportable(columnAnnElm.unwrap().exportable());

            TypeMirror mediatorType = columnAnnElm.getValueTypeMirror(Column::mediator);
            if (!mediatorType.toString().equals(FieldMediator.class.getName())) {
                columnElement.setMediator(new ClassType(processingEnv, (DeclaredType) mediatorType));
            }

            // insertUs
            if (!OP_NOP.equals(columnAnnElm.unwrap().insertAs())) {
                if (columnAnnElm.unwrap().insertAs().equals(OU_UPDATE)) {
                    if (!OP_NOP.equals(columnAnnElm.unwrap().updateAs())) {
                        columnElement.setInsertAs(columnAnnElm.unwrap().updateAs());
                    }
                } else {
                    columnElement.setInsertAs(columnAnnElm.unwrap().insertAs());
                }
            }

            // updateUs
            if (!OP_NOP.equals(columnAnnElm.unwrap().updateAs())) {
                if (columnAnnElm.unwrap().updateAs().equals(OP_INSERT)) {
                    if (!OP_NOP.equals(columnAnnElm.unwrap().insertAs())) {
                        columnElement.setUpdateAs(columnAnnElm.unwrap().insertAs());
                    }
                } else {
                    columnElement.setUpdateAs(columnAnnElm.unwrap().updateAs());
                }
            }
            if (!OP_NOP.equals(columnAnnElm.unwrap().selectAs())) {
                columnElement.setSelectAs(columnAnnElm.unwrap().selectAs());
            }

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

        AnnotationToolbox<Composition> compositionAnnElm = field.getAnnotation(Composition.class);
        if (compositionAnnElm != null) {
            compositions.add(compositionAnnElm.unwrap());
        } else {
            AnnotationToolbox<Compositions> compositionsAnnElm = field.getAnnotation(Compositions.class);
            if (compositionsAnnElm != null) {
                compositions.addAll(Arrays.asList(compositionsAnnElm.unwrap().value()));
            }
        }
        return compositions;
    }

    protected void parseComposition(final CompositionElement compositionElm, String namePrefix) {
        logger.debug("Parse RECORD composition: " + compositionElm);
        if (namePrefix == null) {
            namePrefix = "";
        }

        List<FieldElement> fields = compositionElm.getOriginClass().getFieldsFiltered(
            f -> !f.unwrap().getModifiers().contains(Modifier.STATIC)
        );

        for (FieldElement field : fields) {
            logger.debug("Process RECORD field: "+field);
            final List<Composition> compositions = getCompositions(field);
            for (Composition compositionAnn : compositions) {

                AnnotationToolbox<Composition> compositionAnnElm = new AnnotationToolbox<>(processingEnv, compositionAnn);

                // Check view
                if (!isInView(compositionElm.getParentRecord().getView(), compositionAnnElm.unwrap().views())) {
                    continue;
                }

                // Filter compositions that contains only not acceptable fields
                if (acceptChain(compositionElm, field.getName(), false) == null) {
                    continue;
                }

                ClassElement compositionClass = field.asClassType().asClassElement();
                CompositionElement subComposition = new CompositionElement(recordElement, compositionClass, field);

                if (compositionAnnElm.unwrap().columns().length > 0) {
                    subComposition.setImportedColumns(compositionAnnElm.unwrap().columns());
                }

                if (StringUtils.isNoneBlank(compositionAnnElm.unwrap().keyColumn())) {
                    subComposition.setKeyColumn(compositionAnnElm.unwrap().keyColumn());
                }

                compositionElm.addSubComposition(subComposition);
                parseComposition(subComposition, namePrefix + compositionAnnElm.unwrap().columnsPrefix());
                break;
            }
            parseColumn(compositionElm, field, namePrefix);
        }
    }

    protected RecordElement parseRecord(ClassElement typeElement, String view) {
        recordElement = new RecordElement(typeElement, view);

        AnnotationToolbox<Record> annElm = typeElement.getAnnotation(Record.class);
        recordElement.setTableName(annElm.unwrap().table());
        TypeMirror extend = annElm.getValueTypeMirror(Record::extend);
        recordElement.setExtend(new ClassType(processingEnv, (DeclaredType) extend));

        parseComposition(recordElement.getRootComposition(), null);
        return recordElement;
    }

    public ViewSetElement parse(ClassElement typeElement) {
        logger.debug("Parse JDBI record: " + typeElement);
        AnnotationToolbox<Record> annElm = typeElement.getAnnotation(Record.class);
        String[] views = annElm.unwrap().views();
        checkViewNames(views, typeElement.unwrap());

        List<String> viewsList = new ArrayList<>();
        for (String prof : views) {
            if (!RecordView.ALL_VIEWS.equals(prof)) {
                viewsList.add(prof);
            }
        }

        ViewSetElement profSetElm = new ViewSetElement();
        for (String view : viewsList) {
            RecordElement recordElm = parseRecord(typeElement, view);
            profSetElm.addRecord(view, recordElm);
        }

        return profSetElm;
    }
}
