package colesico.framework.dao.codegen.generator;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.dao.codegen.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PostgresTabeGenerator implements TableGenerator {

    private static final Logger logger = LoggerFactory.getLogger(PostgresTabeGenerator.class);
    private final ProcessingEnvironment processingEnv;

    public PostgresTabeGenerator(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    protected void generateCreateTableSQL(TableElement tableElement, Writer w) throws Exception {
        w.write("\n");
        w.write("CREATE TABLE " + tableElement.getName() + "(\n");

        List<String> columnsList = new ArrayList<>();

        for (ColumnElement column : tableElement.getColumns()) {
            StringBuilder cb = new StringBuilder("        ");
            cb.append(column.getName() + " " + column.getType());
            if (column.isRequired()) {
                cb.append(" NOT NULL");
            }
            if (StringUtils.isNotEmpty(column.getDefaultVal())) {
                cb.append(" DEFAULT " + column.getDefaultVal());
            }
            columnsList.add(cb.toString());
        }

        String columnsBlock = StringUtils.join(columnsList, ",\n");
        w.write(columnsBlock);
        if (tableElement.getPrimaryKey() != null) {
            generatePKConstraint(tableElement.getPrimaryKey(), w);
        }

        if (!tableElement.getForeignKeys().isEmpty()) {
            generateFKConstraints(tableElement, w);
        }

        w.write("\n);\n");
    }

    protected void generateColumnComments(TableElement tableElement, Writer w) throws Exception {
        for (ColumnElement column : tableElement.getColumns()) {
            if (StringUtils.isEmpty(column.getComment())) {
                continue;
            }
            w.write("COMMENT ON COLUMN " + tableElement.getName() + "." + column.getName() + " IS " + "'" + column.getComment() + "';\n");
        }
    }

    protected void generatePKConstraint(PrimaryKeyElement pk, Writer w) throws Exception {
        w.write(",\n        CONSTRAINT " + pk.getName() + " PRIMARY KEY (");
        String columnsList = StringUtils.join(pk.getColumns(), ",");
        w.write(columnsList);
        w.write(")");
    }


    public void generateFKConstraints(TableElement tableElement, Writer w) throws Exception {
        for (ForeignKeyElement fke : tableElement.getForeignKeys()) {
            w.write(",\n        CONSTRAINT " + fke.getName() + " FOREIGN KEY (");
            String localColumnsStr = StringUtils.join(fke.getLocalColumns(), ",");
            w.write(localColumnsStr);
            w.write(") REFERENCES " + fke.getForeignTable() + " (");
            String foregnColumns = StringUtils.join(fke.getForeignColumns(), ",");
            w.write(foregnColumns);
            w.write(")");
        }
    }

    public void generateFKIndexes(TableElement tableElement, Writer w) throws Exception {
        for (ForeignKeyElement fke : tableElement.getForeignKeys()) {
            w.write("CREATE INDEX fki_" + fke.getName() + " ON " + tableElement.getName() + " (");
            String localColumnsStr = StringUtils.join(fke.getLocalColumns(), ",");
            w.write(localColumnsStr);
            w.write(");\n");
        }
    }

    public void generateInsertSQL(TableElement tableElement, Writer w) throws Exception {
        List<String> columns = new ArrayList<>();
        List<String> vars = new ArrayList<>();
        for (ColumnElement column : tableElement.getColumns()) {
            columns.add(column.getName());
            vars.add(":" + column.getName());
        }

        String columnsStr = StringUtils.join(columns, ", ");
        w.write("INSERT INTO " + tableElement.getName() + " (" + columnsStr + ")\n");
        String varsStr = StringUtils.join(vars, ", ");
        w.write("       VALUES (" + varsStr + ")\n");
    }

    public void generateUpdateSQL(TableElement tableElement, Writer w) throws Exception {
        List<String> assigns = new ArrayList<>();

        Set<String> pkColumns;
        if (tableElement.getPrimaryKey() != null) {
            logger.debug("Table "+tableElement.getName()+" has primary key");
            pkColumns = tableElement.getPrimaryKey().getColumns();
        } else {
            pkColumns = new HashSet<>();
        }

        for (ColumnElement column : tableElement.getColumns()) {
            if (!pkColumns.contains(column.getName())) {
                assigns.add(column.getName() + " = :" + column.getName());
            }
        }

        String assignsStr = StringUtils.join(assigns, ", ");
        w.write("UPDATE " + tableElement.getName() + " SET " + assignsStr + "\n");

        if (tableElement.getPrimaryKey() != null) {
            List<String> where = new ArrayList<>();
            for (String columnName : tableElement.getPrimaryKey().getColumns()) {
                where.add(columnName + " = :" + columnName);
            }
            String whereStr = StringUtils.join(where, " AND ");
            w.write("        WHERE " + whereStr + "\n");
        }
    }

    @Override
    public void generate(TableElement tableElement) {
        try {
            String filePath = OUTPUT_PATH + tableElement.getOriginClass().getSimpleName().toString() + ".sql";
            FileObject fileObject = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", filePath);
            try (OutputStream output = fileObject.openOutputStream()) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));

                generateCreateTableSQL(tableElement, writer);
                generateColumnComments(tableElement, writer);
                generateFKIndexes(tableElement, writer);
                generateInsertSQL(tableElement, writer);
                generateUpdateSQL(tableElement, writer);
                writer.flush();
            }
        } catch (Exception e) {
            throw CodegenException.of().message("Error generating table SQL file: " + ExceptionUtils.getRootCauseMessage(e)).element(tableElement.getOriginClass()).build();
        }
    }
}
