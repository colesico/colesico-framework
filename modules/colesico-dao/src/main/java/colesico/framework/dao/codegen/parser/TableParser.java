package colesico.framework.dao.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class TableParser {

    protected static final Logger logger = LoggerFactory.getLogger(TableParser.class);
    protected final ProcessingEnvironment processingEnv;

    public TableParser(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    protected <C extends ColumnElement> C fillColumn(C columnElement, Column columnAnn, String namePrefix) {
        if (namePrefix == null) {
            namePrefix = "";
        }
        columnElement.setComment(columnAnn.comment());
        columnElement.setName(namePrefix + columnAnn.name());
        columnElement.setRequired(columnAnn.required());
        columnElement.setType(columnAnn.type());
        columnElement.setDefaultVal(columnAnn.defaultVal());

        if (StringUtils.isNotEmpty(columnAnn.comment())) {
            columnElement.setComment(columnAnn.comment());
        }

        return columnElement;
    }

    protected void parseField(TableElement tableElement, Deque<VariableElement> fieldPath, String namePrefix) {
        if (namePrefix == null) {
            namePrefix = "";
        }

        VariableElement field = fieldPath.peek();
        if (field.getModifiers().contains(Modifier.STATIC)){
            return;
        }

        TypeElement fieldType = CodegenUtils.classMemberType((DeclaredType) tableElement.getOriginClass().asType(), field, processingEnv);

        Composition compositionAnn = field.getAnnotation(Composition.class);
        if (compositionAnn != null) {
            List<VariableElement> subfields = CodegenUtils.getFields(processingEnv, fieldType, null, null);
            for (VariableElement subfield : subfields) {
                fieldPath.push(subfield);
                parseField(tableElement, fieldPath, namePrefix + compositionAnn.fieldPrefix());
                fieldPath.pop();
            }
            return;
        }

        Column columnAnn = field.getAnnotation(Column.class);
        if (columnAnn == null) {
            return;
        }

        FieldColumnElement columnElement = new FieldColumnElement(fieldPath);
        columnElement = fillColumn(columnElement, columnAnn, namePrefix);
        tableElement.addColumn(columnElement);
    }

    protected void parseFields(TableElement tableElement) {
        List<VariableElement> fields = CodegenUtils.getFields(processingEnv, tableElement.getOriginClass(), null, null);
        for (VariableElement field : fields) {
            Deque<VariableElement> fieldPath = new ArrayDeque<>();
            fieldPath.push(field);
            parseField(tableElement, fieldPath, null);
        }
    }

    protected void parsePK(TableElement tableElement) {
        PrimaryKey pkAnn = tableElement.getOriginClass().getAnnotation(PrimaryKey.class);
        if (pkAnn == null) {
            return;
        }

        PrimaryKeyElement pkElement = new PrimaryKeyElement();
        pkElement.setName(pkAnn.name());
        for (String colmn : pkAnn.columns()) {
            pkElement.addColumn(colmn);
        }
        tableElement.setPrimaryKey(pkElement);
    }

    protected void parseExtraColumns(TableElement tableElement) {
        ExtraColumns extraColumns = tableElement.getOriginClass().getAnnotation(ExtraColumns.class);
        if (extraColumns == null) {
            return;
        }

        for (Column columnAnn : extraColumns.value()) {
            ColumnElement columnElement = new ColumnElement();
            columnElement = fillColumn(columnElement, columnAnn, null);
            tableElement.addColumn(columnElement);
        }
    }

    protected void parseColumnsOverridings(TableElement tableElement) {
        ColumnOverrides extraColumns = tableElement.getOriginClass().getAnnotation(ColumnOverrides.class);
        if (extraColumns == null) {
            return;
        }

        for (Column columnAnn : extraColumns.value()) {
            ColumnElement columnElement = new ColumnElement();
            columnElement = fillColumn(columnElement, columnAnn, null);
            tableElement.updateColumn(columnElement);
        }
    }

    protected void parseForeignKeys(TableElement tableElement) {
        List<ForeignKey> fkAnnList = new ArrayList<>();

        ForeignKeys fksAnn = tableElement.getOriginClass().getAnnotation(ForeignKeys.class);
        if (fksAnn != null) {
            for (ForeignKey fkAnn : fksAnn.value()) {
                fkAnnList.add(fkAnn);
            }
        } else {
            ForeignKey fkAnn = tableElement.getOriginClass().getAnnotation(ForeignKey.class);
            if (fkAnn != null) {
                fkAnnList.add(fkAnn);
            }
        }

        if (fkAnnList.isEmpty()) {
            return;
        }

        for (ForeignKey fkAnn : fkAnnList) {
            ForeignKeyElement fkElm = new ForeignKeyElement();
            fkElm.setName(fkAnn.name());
            fkElm.setForeignTable(fkAnn.foreignTable());
            fkElm.setForeignColumns(fkAnn.foreignColumns());
            fkElm.setLocalColumns(fkAnn.localColumns());
            tableElement.addForeignKey(fkElm);
        }
    }

    public TableElement parse(TypeElement typeElement) {
        Table tableAnn = typeElement.getAnnotation(Table.class);
        TableElement tableElement = new TableElement(typeElement);
        tableElement.setName(tableAnn.name());
        if (StringUtils.isNotEmpty(tableAnn.comment())) {
            tableElement.setComment(tableAnn.comment());
        }
        parseFields(tableElement);
        parseExtraColumns(tableElement);
        parseColumnsOverridings(tableElement);
        parseForeignKeys(tableElement);
        parsePK(tableElement);
        return tableElement;
    }
}
