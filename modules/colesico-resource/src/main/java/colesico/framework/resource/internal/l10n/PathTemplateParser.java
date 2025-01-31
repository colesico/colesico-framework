package colesico.framework.resource.internal.l10n;

import colesico.framework.resource.ResourceException;

import java.util.ArrayList;
import java.util.List;

public class PathTemplateParser {

    private static final String QUALIFIERS = "{Q}";

    private static final String SUBSTITUTE_BEGIN = "{{";
    private static final String SUBSTITUTE_AS = "=";
    private static final String SUBSTITUTE_END = "}}";

    private final String template;
    private final int templateLength;
    private int templateIndex = 0;
    private final StringBuilder path = new StringBuilder();

    private final List<PathTag> tags = new ArrayList<>();

    public static PathTemplateParser parse(String template) {
        return new PathTemplateParser(template).parseTemplate();
    }

    private PathTemplateParser(String template) {
        this.template = template;
        this.templateLength = template.length();
    }

    private boolean isNotEnd() {
        return templateIndex < templateLength;
    }

    // Shift template index
    private void shiftIndex(int offset) {
        templateIndex = templateIndex + offset;
        if (templateIndex > templateLength) {
            templateIndex = templateLength;
        }
    }

    private void shiftIndex() {
        shiftIndex(1);
    }

    // Char at current template index + offset
    private char charAt(int offset) {
        int i = templateIndex + offset;
        if (i < templateLength) {
            return template.charAt(i);
        } else {
            return '\0';
        }
    }

    private char charAt() {
        return charAt(0);
    }

    // Given token is starts from current template index
    private boolean isToken(String token) {
        int i = 0;
        for (int n = token.length(); i < n; i++) {
            if (charAt(i) != token.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    // Return template text from template current index to  given token
    private String textToToken(String token) {
        StringBuilder text = new StringBuilder();
        while (isNotEnd()) {
            if (isToken(token)) {
                shiftIndex(token.length());
                return text.toString();
            } else {
                text.append(charAt());
                shiftIndex();
            }
        }
        throw new ResourceException("Invalid path template. Token '" + token + "' expected");
    }

    private void parseQualifiersSuffixOperator() {
        QualifiersTag operator = new QualifiersTag(path.length());
        this.tags.add(operator);
        shiftIndex(QUALIFIERS.length());
    }

    private void parseSubstituteOperator() {
        int position = path.length();
        shiftIndex(SUBSTITUTE_BEGIN.length());
        String part = textToToken(SUBSTITUTE_AS);
        int length = part.length();
        path.append(part);
        String substitution = textToToken(SUBSTITUTE_END);
        SubstituteTag action = new SubstituteTag(position, position + length, substitution);
        tags.add(action);
    }

    private PathTemplateParser parseTemplate() {
        while (isNotEnd()) {
            if (isToken(QUALIFIERS)) {
                parseQualifiersSuffixOperator();
            } else if (isToken(SUBSTITUTE_BEGIN)) {
                parseSubstituteOperator();
            } else {
                path.append(charAt());
                shiftIndex();
            }
        }
        return this;
    }

    public PathTag[] getTags() {
        // reverse to apply rewriting from path end to beginning
        return tags.reversed().toArray(PathTag[]::new);
    }

    public String getPath() {
        return path.toString();
    }
}
