package colesico.framework.jdbirec.codegen.parser;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractParser;
import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.jdbirec.*;
import colesico.framework.jdbirec.codegen.model.ColumnOverridingElement;
import colesico.framework.jdbirec.codegen.model.CompositionElement;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.*;

abstract public class RecordKitParserHelpers extends FrameworkAbstractParser {

    public RecordKitParserHelpers(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    protected String buildCompositionName(AnnotationAssist<Composition> compAnn, FieldElement compField, ClassType compType) {
        String name;
        if (StringUtils.isNotBlank(compAnn.unwrap().name())) {
            name = StringUtils.trim(compAnn.unwrap().name());
        } else {
            if (compField != null) {
                name = compField.getName();
            } else {
                name = StrUtils.firstCharToLowerCase(compType.asClassElement().getSimpleName());
            }
        }
        return name;
    }

    protected static String buildColumnName(AnnotationAssist<Column> columnAst, FieldElement columnField) {
        String name;
        if (StringUtils.isBlank(columnAst.unwrap().name())) {
            name = StrUtils.toSeparatorNotation(columnField.getName(), '_');
        } else {
            name = StringUtils.trim(columnAst.unwrap().name());
        }
        return name;
    }


    /**
     * Builds compositions path string:  comp1.comp2.comp.name
     */
    protected String buildCompositionPath(CompositionElement comp, String name) {
        Deque<String> namesStack = new ArrayDeque<>();

        if (name != null) {
            namesStack.push(name);
        }

        CompositionElement c = comp;
        while (c.getOriginField() != null) {
            namesStack.push(c.getOriginField().getName());
            c = c.getParentComposition();
        }

        return StringUtils.join(namesStack, ".");
    }

    /**
     * Check view name should be class name compatible to generate class name suffix
     */
    protected void checkViewName(final String viewName, Element parentElement) {
        if (!viewName.matches("[A-Za-z0-9]+")) {
            throw CodegenException.of().message("Invalid record view name. Alphanumeric chars expected: " + viewName).element(parentElement).build();
        }
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

    /**
     * Lookup column overridings from column composition to root composition
     */
    protected Set<ColumnOverridingElement> findColumnOverridings(CompositionElement columnComposition, String columnName) {
        logger.debug("Find composition {} column '{}' overridings...", columnComposition, columnName);

        // Build composition chain from root to this
        CompositionElement c = columnComposition;
        List<CompositionElement> compositionChain = new ArrayList<>();
        while (c != null) {
            compositionChain.add(c);
            c = c.getParentComposition();
        }
        logger.debug("compositionChain size: {} ", compositionChain.size());

        // Collections.reverse(compositionChain);

        // Lookup overriding definition within compositions, starts from root composition
        Set<ColumnOverridingElement> columnOverridings = new HashSet<>();
        String columnPath = buildCompositionPath(columnComposition, columnName);
        for (CompositionElement ce : compositionChain) {
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
     * Check tags accepted by filters
     */
    protected boolean acceptTagsByFilter(CompositionElement columnComposition, Set<String> tags) {
        //TODO: implement filter
        return true;
    }

    protected boolean hasParentOverridings(CompositionElement cmp) {
        CompositionElement curComp = cmp;
        while (curComp != null) {
            if (!curComp.getColumnOverriding().isEmpty()) {
                return true;
            }
            curComp = curComp.getParentComposition();
        }
        return false;
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

    protected String applyColumnRenaming(CompositionElement composition, FieldElement columnField, String columnColumnName) {
        String columnOriginName = columnColumnName;
        if (StringUtils.isNotBlank(composition.getRenaming())) {
            columnColumnName = StringUtils.replace(composition.getRenaming(), Composition.RN_AUTO, composition.getName() + "_" + columnOriginName);
            columnColumnName = StringUtils.replace(columnColumnName, Composition.RN_COLUMN_FILED, columnField.getName());
            columnColumnName = StringUtils.replace(columnColumnName, Composition.RN_COLUMN, columnOriginName);
            columnColumnName = StringUtils.replace(columnColumnName, Composition.RN_COMPOSITION_FIELD, composition.getOriginField().getName());
            columnColumnName = StringUtils.replace(columnColumnName, Composition.RN_COMPOSITION, composition.getName());
        }
        logger.debug("Column renaming: {} -> rule '{}' -> {}", columnOriginName, composition.getRenaming(), columnColumnName);

        return columnColumnName;
    }

    protected Set<String> buildTags(CompositionElement composition, String name, String[] tagsDef) {
        Set<String> result = new HashSet<>();

        // Add default tags
        String fieldTag = "#" + buildCompositionPath(composition, name);
        result.add(fieldTag);

        // Transform local tags to global
        for (String tagDef : tagsDef) {
            if (!StringUtils.startsWith(tagDef, "#")) {
                tagDef = "#" + buildCompositionPath(composition, tagDef);
            }
            result.add(tagDef);
        }

        return result;
    }


    /**
     * Check column overriding was really associated with a columns
     */
    protected void checkColumnOverridingsAssociated(final CompositionElement composition) {
        for (ColumnOverridingElement overriding : composition.getColumnOverriding()) {
            if (!overriding.isAssociated()) {

                String path = buildCompositionPath(composition, null);
                throw CodegenException.of()
                        .message("Composition '" + recordKitElement.getRecordType().unwrap() + "." + path + "' column overriding has not associated column: " + overriding.getColumnPath())
                        .element(composition.getOriginField().unwrap())
                        .build();

            }
        }
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


}
