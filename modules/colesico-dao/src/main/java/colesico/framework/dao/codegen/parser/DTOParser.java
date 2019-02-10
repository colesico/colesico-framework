package colesico.framework.dao.codegen.parser;

import colesico.framework.assist.codegen.CodegenUtils;
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
import java.lang.reflect.Field;
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
            fieldsStack.push(current.getOriginalField().getSimpleName().toString());
            current = current.getParentComposition();
        }
        return StringUtils.join(fieldsStack, ".");
    }

    protected boolean acceptField(CompositionElement composition, VariableElement field) {
        String fieldChain = getFieldsChain(composition, field.getSimpleName().toString());

        CompositionElement acceptedFieldsComposition = null;
        while (composition != null) {
            if (composition.getAcceptFields()!= null) {
                acceptedFieldsComposition = composition;
                break;
            }
            composition = composition.getParentComposition();
        }

        if (acceptedFieldsComposition == null) {
            return true;
        }

        for (String acceptedField:acceptedFieldsComposition.getAcceptFields()){
            String acceptedFiledChain = getFieldsChain(acceptedFieldsComposition,acceptedField);
            if (StringUtils.equals(acceptedFiledChain,fieldChain)){
                return true;
            }
        }
        return false;
    }

    protected String renameColumn(CompositionElement composition, String origName) {
        String[] renamings = null;
        while (composition != null) {
            if (composition.getRenameColumns()!=null) {
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

        List<VariableElement> fields = CodegenUtils.getFields(processingEnv, composition.getOriginType(), null, null);
        for (VariableElement field : fields) {
            if (field.getModifiers().contains(Modifier.STATIC)) {
                continue;
            }

            if (!acceptField(composition, field)) {
                continue;
            }

            Composition compositionAnn = field.getAnnotation(Composition.class);
            if (compositionAnn != null) {
                TypeElement fieldType = CodegenUtils.classMemberType((DeclaredType) composition.getOriginType().asType(), field, processingEnv);
                CompositionElement subComposition = new CompositionElement(dtoElement, fieldType, field);
                if (compositionAnn.acceptFields().length > 0) {
                    subComposition.setAcceptFields(compositionAnn.acceptFields());
                }
                if (compositionAnn.renameColumns().length > 0) {
                    subComposition.setRenameColumns(compositionAnn.renameColumns());
                }
                composition.addSubComposition(subComposition);
                parseComposition(subComposition, namePrefix + compositionAnn.columnsPrefix());
                continue;
            }

            Column columnAnn = field.getAnnotation(Column.class);
            if (columnAnn == null) {
                continue;
            }

            String columnName = namePrefix + columnAnn.name();
            ColumnElement columnElement = new ColumnElement(field, renameColumn(composition, columnName));
            composition.addColumn(columnElement);

            TypeMirror converterType  = CodegenUtils.getAnnotationValueTypeMirror(columnAnn,c->c.converter());
            if (!converterType.toString().equals(DTOConverter.class.getName())){
                columnElement.setConverter(converterType);
            }
        }
    }

    public DTOElement parse(TypeElement typeElement) {
        DTO dtoAnn = typeElement.getAnnotation(DTO.class);
        dtoElement = new DTOElement(typeElement);
        parseComposition(dtoElement.getRootComposition(), null);
        return dtoElement;
    }
}
