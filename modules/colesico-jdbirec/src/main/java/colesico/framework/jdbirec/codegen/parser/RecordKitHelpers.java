package colesico.framework.jdbirec.codegen.parser;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractParser;
import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.jdbirec.Record;
import colesico.framework.jdbirec.*;
import colesico.framework.jdbirec.codegen.model.ColumnOverridingElement;
import colesico.framework.jdbirec.codegen.model.CompositionElement;
import colesico.framework.jdbirec.codegen.model.ContainerElement;
import colesico.framework.jdbirec.codegen.model.RecordElement;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.*;

abstract public class RecordKitHelpers extends FrameworkAbstractParser {

    public RecordKitHelpers(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    protected static String buildColumnName(AnnotationAssist<Column> columnAnn, FieldElement columnField) {
        String name;
        if (StringUtils.isBlank(columnAnn.unwrap().name())) {
            name = StrUtils.toSeparatorNotation(columnField.getName(), '_');
        } else {
            name = StringUtils.trim(columnAnn.unwrap().name());
        }
        return name;
    }

    protected String buildCompositionName(AnnotationAssist<Composition> compAnn, FieldElement field) {
        String name;
        if (StringUtils.isNotBlank(compAnn.unwrap().name())) {
            name = StringUtils.trim(compAnn.unwrap().name());
        } else {
            name = field.getName();
        }
        return name;
    }

    /**
     * Check view name should be class name compatible to generate class name suffix
     */
    protected void checkViewName(final String viewName, Element record) {
        if (!viewName.matches("[A-Za-z0-9]+")) {
            throw CodegenException.of().message("Invalid record view name. Alphanumeric chars expected: " + viewName).element(record).build();
        }
    }

    protected Set<AnnotationAssist<Record>> findTypeRecords(ClassType type) {
        final Set<AnnotationAssist<Record>> result = new HashSet<>();

        AnnotationAssist<Record> record = type.asClassElement().getAnnotation(Record.class);
        if (record != null) {
            result.add(record);
        } else {
            AnnotationAssist<Records> records = type.asClassElement().getAnnotation(Records.class);
            if (records != null) {
                for (Record rec : records.unwrap().value()) {
                    record = new AnnotationAssist<>(processingEnv, rec);
                    result.add(record);
                }
            }
        }
        return result;
    }

    /**
     * Lookup column overridings from column composition to root composition
     */
    protected Set<ColumnOverridingElement> findColumnOverridings(ContainerElement container, String columnName) {
        logger.debug("Find container {} column '{}' overridings...", container, columnName);

        // Build composition chain from root to this
        ContainerElement c = container;
        List<ContainerElement> containerChain = new ArrayList<>();
        while (c != null) {
            containerChain.add(c);
            c = c instanceof CompositionElement comp ? comp.getContainer() : null;
        }

        logger.debug("ContainerChain size: {} ", containerChain.size());

        // Lookup overriding definition within compositions, starts from root composition
        Set<ColumnOverridingElement> columnOverridings = new HashSet<>();
        String columnPath = buildContainerPath(container, columnName);
        for (ContainerElement ce : containerChain) {
            logger.debug("Composition {} column overriding size= {} ", ce, ce.getColumnOverriding().size());

            if (ce.getColumnOverriding().isEmpty()) {
                continue;
            }

            // Find column within overriding
            for (ColumnOverridingElement coe : ce.getColumnOverriding()) {
                logger.debug("Test column {} overriding:  {}", columnPath, coe);
                if (coe.getColumnPath().equals(columnPath)) {
                    columnOverridings.add(coe);
                }
            }
        }

        return columnOverridings;
    }

    /**
     * Builds container path string:  field1.field2.field.name
     */
    protected String buildContainerPath(ContainerElement container, String name) {
        Deque<String> namesStack = new ArrayDeque<>();

        if (StringUtils.isNotBlank(name)) {
            namesStack.push(name);
        }

        ContainerElement c = container;
        while (c != null) {
            if (c instanceof CompositionElement comp) {
                namesStack.push(comp.getField().getName());
                c = comp.getContainer();
            } else {
                c = null;
            }
        }

        return StringUtils.join(namesStack, ".");
    }

    /**
     * Check tags accepted by filters
     */
    protected boolean acceptTagsByFilter(ContainerElement container, Set<String> tags) {
        //TODO: implement filter
        return true;
    }

    protected Set<AnnotationAssist<Composition>> findFieldCompositions(FieldElement field) {
        Set<AnnotationAssist<Composition>> result = new HashSet<>();

        Set<AnnotationAssist<Composition>> typeCompsAnn = findTypeCompositions(field.asClassType());
        result.addAll(typeCompsAnn);

        AnnotationAssist<Composition> composition = field.getAnnotation(Composition.class);
        if (composition != null) {
            result.add(composition);
        } else {
            AnnotationAssist<Compositions> compositions = field.getAnnotation(Compositions.class);
            if (compositions != null) {
                for (Composition comp : compositions.unwrap().value()) {
                    composition = new AnnotationAssist<>(processingEnv, comp);
                    result.add(composition);
                }
            }
        }

        return result;
    }

    protected Set<AnnotationAssist<Composition>> findTypeCompositions(ClassType type) {
        final Set<AnnotationAssist<Composition>> result = new HashSet<>();

        AnnotationAssist<Composition> composition = type.asClassElement().getAnnotation(Composition.class);
        if (composition != null) {
            result.add(composition);
        } else {
            AnnotationAssist<Compositions> compositions = type.asClassElement().getAnnotation(Compositions.class);
            if (compositions != null) {
                for (Composition comp : compositions.unwrap().value()) {
                    composition = new AnnotationAssist<>(processingEnv, comp);
                    result.add(composition);
                }
            }
        }
        return result;
    }

    protected Set<AnnotationAssist<Column>> findFieldColumns(FieldElement field) {
        final Set<AnnotationAssist<Column>> result = new HashSet<>();

        AnnotationAssist<Column> column = field.getAnnotation(Column.class);
        if (column != null) {
            result.add(column);
        } else {
            AnnotationAssist<Columns> columns = field.getAnnotation(Columns.class);
            if (columns != null) {
                for (Column col : columns.unwrap().value()) {
                    column = new AnnotationAssist<>(processingEnv, col);
                    result.add(column);
                }
            }
        }
        return result;
    }

    protected String applyColumnRenaming(ContainerElement container, String columnName) {
        String columnOriginName = columnName;
        if (StringUtils.isNotBlank(container.getRenaming())) {
            if (container instanceof CompositionElement comp) {
                columnName = StringUtils.replace(container.getRenaming(), Composition.RN_AUTO, comp.getName() + "_" + columnOriginName);
                columnName = StringUtils.replace(columnName, Composition.RN_COLUMN_NAME, columnOriginName);
                columnName = StringUtils.replace(columnName, Composition.RN_COMPOSITION_NAME, comp.getName());
            } else {
                columnName = StringUtils.replace(container.getRenaming(), Composition.RN_AUTO, columnOriginName);
                columnName = StringUtils.replace(columnName, Composition.RN_COLUMN_NAME, columnOriginName);
                columnName = StringUtils.replace(columnName, Composition.RN_COMPOSITION_NAME, "");
            }
        }

        logger.debug("Column renaming: {} -> rule '{}' -> {}", columnOriginName, container.getRenaming(), columnName);

        return columnName;
    }

    protected Set<String> buildTags(
            ContainerElement container,
            FieldElement field,
            String name,
            String[] tagsDef) {

        Set<String> result = new HashSet<>();

        // Add default tags
        String fieldTag = "#" + buildContainerPath(container, field.getName());
        result.add(fieldTag);

        String nameTag = "#" + buildContainerPath(container, name);
        result.add(nameTag);

        // Transform local tags to global
        for (String tagDef : tagsDef) {
            if (!StringUtils.startsWith(tagDef, "#")) {
                tagDef = "#" + buildContainerPath(container, tagDef);
            }
            result.add(tagDef);
        }

        return result;
    }


    /**
     * Extract record type from record kit interface  (generic parameter)
     */
    protected ClassType getRecordTypeFromKit(ClassElement recordKitInterface) {

        ClassType superClass = null;
        List<ClassType> interfaces = recordKitInterface.getInterfaces();
        for (ClassType iface : interfaces) {
            if (iface.getErasure().toString().equals(RecordKitApi.class.getCanonicalName())) {
                superClass = iface;
                break;
            }
        }

        if (superClass == null) {
            throw CodegenException.of().element(recordKitInterface.unwrap()).message("Not extends " + RecordKitApi.class.getName()).build();
        }

        if (superClass.unwrap().getTypeArguments().size() != 1) {
            throw CodegenException.of().element(recordKitInterface.unwrap()).message("Unable to extract record type").build();
        }

        TypeMirror recordMirror = superClass.unwrap().getTypeArguments().get(0);
        ClassType recordType = new ClassType(processingEnv, (DeclaredType) recordMirror);

        return recordType;
    }


    protected void validateRecord(RecordElement record) {
        //TODO ?

    }

}
