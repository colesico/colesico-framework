package colesico.framework.dao.codegen.parser;

import colesico.framework.assist.codegen.CodegenUtils;
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
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
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

    protected String getFieldsChain(CompositionElement comp, String fieldName) {
        Deque<String> fieldsStack = new ArrayDeque<>();
        if (fieldName != null) {
            fieldsStack.push(fieldName);
        }
        CompositionElement current = comp;
        while (current.getOriginalField() != null) {
            fieldsStack.push(current.getOriginalField().getName());
            current = current.getParentComposition();
        }
        return StringUtils.join(fieldsStack, ".");
    }

    protected boolean acceptField(CompositionElement composition, FieldElement field) {
        String fieldChain = getFieldsChain(composition, field.getName());

        CompositionElement acceptedFieldsComposition = null;
        while (composition != null) {
            if (composition.getAcceptFields() != null) {
                acceptedFieldsComposition = composition;
                break;
            }
            composition = composition.getParentComposition();
        }

        if (acceptedFieldsComposition == null) {
            return true;
        }

        for (String acceptedField : acceptedFieldsComposition.getAcceptFields()) {
            String acceptedFiledChain = getFieldsChain(acceptedFieldsComposition, acceptedField);
            if (StringUtils.equals(acceptedFiledChain, fieldChain)) {
                return true;
            }
        }
        return false;
    }

    protected String renameColumn(CompositionElement composition, String origName) {
        String[] renamings = null;
        while (composition != null) {
            if (composition.getRenameColumns() != null) {
                renamings = composition.getRenameColumns();
                break;
            }
            composition = composition.getParentComposition();
        }

        if (renamings == null) {
            return origName;
        }

        for (String renaming : renamings) {
            String[] rn = StringUtils.split(renaming, "->");
            String oldName = StringUtils.trim(rn[0]);
            String newName = StringUtils.trim(rn[1]);
            if (StringUtils.equals(origName, oldName)) {
                return newName;
            }
        }
        return origName;
    }

    protected void parseComposition(CompositionElement composition, String namePrefix) {
        if (namePrefix == null) {
            namePrefix = "";
        }

        List<FieldElement> fields = composition.getOriginType().getFieldsFiltered(
                f -> !f.unwrap().getModifiers().contains(Modifier.STATIC) & acceptField(composition, f)
        );
        for (FieldElement field : fields) {

            AnnotationElement<Composition> compositionAnn = field.getAnnotation(Composition.class);
            if (compositionAnn != null) {
                ClassElement fieldType = field.asClassType().asClassElement();

                CompositionElement subComposition = new CompositionElement(dtoElement, fieldType, field);
                if (compositionAnn.unwrap().acceptFields().length > 0) {
                    subComposition.setAcceptFields(compositionAnn.unwrap().acceptFields());
                }
                if (compositionAnn.unwrap().renameColumns().length > 0) {
                    subComposition.setRenameColumns(compositionAnn.unwrap().renameColumns());
                }
                composition.addSubComposition(subComposition);
                parseComposition(subComposition, namePrefix + compositionAnn.unwrap().columnsPrefix());
                continue;
            }

            AnnotationElement<Column> columnAnn = field.getAnnotation(Column.class);
            if (columnAnn == null) {
                continue;
            }

            String columnName = namePrefix + columnAnn.unwrap().name();
            ColumnElement columnElement = new ColumnElement(field, renameColumn(composition, columnName));
            composition.addColumn(columnElement);

            TypeMirror converterType = columnAnn.getValueTypeMirror(a -> a.converter());
            if (!converterType.toString().equals(DTOConverter.class.getName())) {
                columnElement.setConverter(new ClassType(processingEnv, (DeclaredType) converterType));
            }
        }
    }

    public DTOElement parse(ClassElement typeElement) {
        //AnnotationElement<DTO> dtoAnn = typeElement.getAnnotation(DTO.class);
        dtoElement = new DTOElement(typeElement);
        parseComposition(dtoElement.getRootComposition(), null);
        return dtoElement;
    }
}
