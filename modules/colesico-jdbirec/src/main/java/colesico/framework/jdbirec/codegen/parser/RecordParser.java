package colesico.framework.jdbirec.codegen.parser;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.model.AnnotationElement;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.jdbirec.*;
import colesico.framework.jdbirec.codegen.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.*;

public class RecordParser {
    protected static final Logger logger = LoggerFactory.getLogger(RecordParser.class);

    protected final ProcessingEnvironment processingEnv;
    protected RecordElement recordElement;

    public RecordParser(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
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

    protected boolean acceptChain(final CompositionElement composition, String localName, boolean isOption) {
        String accessChain = buildAccessChain(composition, localName);
        logger.debug("Check access chain: " + accessChain);
        Set<String> acceptedChains = new HashSet<>();
        CompositionElement current = composition;
        while (current != null) {
            if (composition.getColumnsFilter() != null) {
                for (String lcName : composition.getColumnsFilter()) {
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
            return !isOption;
        }

        for (String acceptedChain : acceptedChains) {
            if (StringUtils.startsWith(acceptedChain, accessChain)) {
                return true;
            }
        }

        return false;
    }


    protected void parseColumn(final CompositionElement composition, FieldElement field, String namePrefix) {

        final List<Column> columns = new ArrayList<>();

        AnnotationElement<Column> columnAnn = field.getAnnotation(Column.class);
        if (columnAnn != null) {
            columns.add(columnAnn.unwrap());
        } else {
            AnnotationElement<Columns> columnsAnn = field.getAnnotation(Columns.class);
            if (columnsAnn != null) {
                columns.addAll(Arrays.asList(columnsAnn.unwrap().value()));
            }
        }

        if (columns.isEmpty()) {
            return;
        }

        for (Column column : columns) {

            columnAnn = new AnnotationElement<>(processingEnv, column);
            String name;
            if (columnAnn.unwrap().name().equals("@field")) {
                name = StrUtils.toLowerCaseNotation(field.getName(),'_');
            } else {
                name = columnAnn.unwrap().name();
            }

            if (!acceptChain(composition, name, columnAnn.unwrap().option())) {
                continue;
            }

            String columnName = namePrefix + name;
            ColumnElement columnElement = new ColumnElement(field, columnName);
            composition.addColumn(columnElement);

            columnElement.setImportable(columnAnn.unwrap().importable());
            columnElement.setExportable(columnAnn.unwrap().exportable());

            TypeMirror converterType = columnAnn.getValueTypeMirror(a -> a.converter());
            if (!converterType.toString().equals(FieldConverter.class.getName())) {
                columnElement.setConverter(new ClassType(processingEnv, (DeclaredType) converterType));
            }

            if (StringUtils.isNotEmpty(columnAnn.unwrap().insertAs())) {
                if (columnAnn.unwrap().insertAs().equals("@update")) {
                    columnElement.setInsertAs(columnAnn.unwrap().updateAs());
                } else {
                    columnElement.setInsertAs(columnAnn.unwrap().insertAs());
                }
            }
            if (StringUtils.isNotEmpty(columnAnn.unwrap().updateAs())) {
                if (columnAnn.unwrap().updateAs().equals("@insert")) {
                    columnElement.setUpdateAs(columnAnn.unwrap().insertAs());
                } else {
                    columnElement.setUpdateAs(columnAnn.unwrap().updateAs());
                }
            }
            if (StringUtils.isNotEmpty(columnAnn.unwrap().selectAs())) {
                columnElement.setSelectAs(columnAnn.unwrap().selectAs());
            }

            if (StringUtils.isNotEmpty(columnAnn.unwrap().definition())) {
                columnElement.setDefinition(columnAnn.unwrap().definition());
            }


        }
    }

    protected void parseComposition(final CompositionElement composition, String namePrefix) {
        logger.debug("Parse RECORD composition: " + composition);
        if (namePrefix == null) {
            namePrefix = "";
        }

        List<FieldElement> fields = composition.getOriginClass().getFieldsFiltered(
                f -> !f.unwrap().getModifiers().contains(Modifier.STATIC)
        );
        for (FieldElement field : fields) {
            AnnotationElement<Composition> compositionAnn = field.getAnnotation(Composition.class);
            if (compositionAnn != null) {
                if (!acceptChain(composition, field.getName(), false)) {
                    continue;
                }
                ClassElement compositionClass = field.asClassType().asClassElement();
                CompositionElement subComposition = new CompositionElement(recordElement, compositionClass, field);

                if (compositionAnn.unwrap().columns().length > 0) {
                    subComposition.setColumnsFilter(compositionAnn.unwrap().columns());
                }

                composition.addSubComposition(subComposition);
                parseComposition(subComposition, namePrefix + compositionAnn.unwrap().columnsPrefix());
                continue;
            }
            parseColumn(composition, field, namePrefix);
        }
    }

    public RecordElement parse(ClassElement typeElement) {
        logger.debug("Parse DB Record: " + typeElement);
        AnnotationElement<Record> recordAnn = typeElement.getAnnotation(Record.class);
        recordElement = new RecordElement(typeElement);

        recordElement.setTableName(recordAnn.unwrap().tableName());
        TypeMirror extend = recordAnn.getValueTypeMirror(a -> a.extend());
        recordElement.setExtend(new ClassType(processingEnv, (DeclaredType) extend));

        parseComposition(recordElement.getRootComposition(), null);
        return recordElement;
    }
}
