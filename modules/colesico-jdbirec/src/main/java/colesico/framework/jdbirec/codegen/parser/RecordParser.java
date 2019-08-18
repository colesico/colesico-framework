package colesico.framework.jdbirec.codegen.parser;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractParser;
import colesico.framework.assist.codegen.model.AnnotationElement;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.jdbirec.*;
import colesico.framework.jdbirec.codegen.model.*;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.*;

public class RecordParser extends FrameworkAbstractParser {

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

    protected void checkProfileNames(final String[] profiles, Element parentElement) {
        for (String p : profiles) {
            if (!p.matches("[A-Za-z0-9]+")) {
                throw CodegenException.of().message("Invalid record profile name. Alphanumeric chars expected: " + p).element(parentElement).build();
            }
        }
    }

    protected boolean isInProfile(String currentProfile, String[] profiles) {
        if (RecordKitProfile.DEFAULT.equals(currentProfile)) {
            return true;
        }
        for (String p : profiles) {
            if (currentProfile.equals(p)) {
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

            // Check profile
            if (!isInProfile(composition.getParentRecord().getProfile(), column.profiles())) {
                continue;
            }

            columnAnn = new AnnotationElement<>(processingEnv, column);
            String name;
            if (columnAnn.unwrap().name().equals("@field")) {
                name = StrUtils.toSeparatorNotation(field.getName(), '_');
            } else {
                name = columnAnn.unwrap().name();
            }

            String acceptedChain = acceptChain(composition, name, columnAnn.unwrap().virtual());
            if (acceptedChain == null) {
                continue;
            }

            String columnName = StringUtils.trim(namePrefix + rewriteName(acceptedChain, name));
            ColumnElement columnElement = new ColumnElement(field, columnName);
            composition.addColumn(columnElement);

            columnElement.setImportable(columnAnn.unwrap().importable());
            columnElement.setExportable(columnAnn.unwrap().exportable());

            TypeMirror mediatorType = columnAnn.getValueTypeMirror(a -> a.mediator());
            if (!mediatorType.toString().equals(FieldMediator.class.getName())) {
                columnElement.setMediator(new ClassType(processingEnv, (DeclaredType) mediatorType));
            }

            if (!"@nop".equals(columnAnn.unwrap().insertAs())) {
                if (columnAnn.unwrap().insertAs().equals("@update")) {
                    columnElement.setInsertAs(columnAnn.unwrap().updateAs());
                } else {
                    columnElement.setInsertAs(columnAnn.unwrap().insertAs());
                }
            }
            if (!"@nop".equals(columnAnn.unwrap().updateAs())) {
                if (columnAnn.unwrap().updateAs().equals("@insert")) {
                    columnElement.setUpdateAs(columnAnn.unwrap().insertAs());
                } else {
                    columnElement.setUpdateAs(columnAnn.unwrap().updateAs());
                }
            }
            if (!"@nop".equals(columnAnn.unwrap().selectAs())) {
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
                // Check profile
                if (!isInProfile(composition.getParentRecord().getProfile(), compositionAnn.unwrap().profiles())) {
                    continue;
                }

                // Filter compositions that contains only not acceptable fields
                if (acceptChain(composition, field.getName(), false) == null) {
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

    protected RecordElement parseRecord(ClassElement typeElement, String profile) {
        recordElement = new RecordElement(typeElement, profile);

        AnnotationElement<Record> annElm = typeElement.getAnnotation(Record.class);
        recordElement.setTableName(annElm.unwrap().table());
        TypeMirror extend = annElm.getValueTypeMirror(a -> a.extend());
        recordElement.setExtend(new ClassType(processingEnv, (DeclaredType) extend));

        parseComposition(recordElement.getRootComposition(), null);
        return recordElement;
    }

    public ProfileSetElement parse(ClassElement typeElement) {
        logger.debug("Parse DB record: " + typeElement);
        AnnotationElement<Record> annElm = typeElement.getAnnotation(Record.class);
        String[] profiles = annElm.unwrap().profiles();
        checkProfileNames(profiles, typeElement.unwrap());

        List<String> profilesList = new ArrayList<>();
        profilesList.add(RecordKitProfile.DEFAULT);
        profilesList.addAll(Arrays.asList(profiles));

        ProfileSetElement profSetElm = new ProfileSetElement();
        for (String profile : profilesList) {
            RecordElement recordElm = parseRecord(typeElement, profile);
            profSetElm.addRecord(profile, recordElm);
        }

        return profSetElm;
    }
}
