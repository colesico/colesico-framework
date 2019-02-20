package colesico.framework.dao.codegen.parser;

import colesico.framework.assist.codegen.model.AnnotationElement;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.dao.*;
import colesico.framework.dao.codegen.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.*;

public class DTOParser {
    protected static final Logger logger = LoggerFactory.getLogger(DTOParser.class);

    protected final ProcessingEnvironment processingEnv;
    protected DTOElement dtoElement;

    public DTOParser(ProcessingEnvironment processingEnv) {
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

            if (!acceptChain(composition, columnAnn.unwrap().name(),columnAnn.unwrap().option())) {
                continue;
            }

            String columnName = namePrefix + columnAnn.unwrap().name();
            ColumnElement columnElement = new ColumnElement(field, columnName);
            composition.addColumn(columnElement);

            TypeMirror converterType = columnAnn.getValueTypeMirror(a -> a.converter());
            if (!converterType.toString().equals(DTOConverter.class.getName())) {
                columnElement.setConverter(new ClassType(processingEnv, (DeclaredType) converterType));
            }

            if (StringUtils.isNotEmpty(columnAnn.unwrap().formula())) {
                columnElement.setFormula(columnAnn.unwrap().formula());
            }

            if (StringUtils.isNotEmpty(columnAnn.unwrap().definition())) {
                columnElement.setDefinition(columnAnn.unwrap().definition());
            }
        }
    }

    protected void parseComposition(final CompositionElement composition, String namePrefix) {
        logger.debug("Parse DTO composition: " + composition);
        if (namePrefix == null) {
            namePrefix = "";
        }

        List<FieldElement> fields = composition.getOriginClass().getFieldsFiltered(
                f -> !f.unwrap().getModifiers().contains(Modifier.STATIC)
        );
        for (FieldElement field : fields) {
            AnnotationElement<Composition> compositionAnn = field.getAnnotation(Composition.class);
            if (compositionAnn != null) {
                if (!acceptChain(composition, field.getName(),false)) {
                    continue;
                }
                ClassElement compositionClass = field.asClassType().asClassElement();
                CompositionElement subComposition = new CompositionElement(dtoElement, compositionClass, field);

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

    public DTOElement parse(ClassElement typeElement) {
        logger.debug("Parse DTO: " + typeElement);
        AnnotationElement<DTO> dtoAnn = typeElement.getAnnotation(DTO.class);
        dtoElement = new DTOElement(typeElement);
        if (dtoAnn != null) {
            dtoElement.setTableName(dtoAnn.unwrap().tableName());
        }
        parseComposition(dtoElement.getRootComposition(), null);
        return dtoElement;
    }
}
