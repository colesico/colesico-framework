package colesico.framework.dao.codegen.generator;

import colesico.framework.dao.codegen.model.TableElement;

public interface TableGenerator {

    String OUTPUT_PATH ="META-INF/sql/";

    void generate(TableElement tableElement);
}
