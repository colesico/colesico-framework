package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.codegen.model.ClassElement;

import java.util.ArrayList;
import java.util.List;

public class TeleAggregateElement {
    /**
     * Aggregate class
     */
    private final ClassElement originClass;

    private final List<TeleAggregateFieldElement> fields = new ArrayList<>();

    private final List<TeleAggregateElement> subAggregates = new ArrayList<>();

    public TeleAggregateElement(ClassElement originClass) {
        this.originClass = originClass;
    }

    public void addField(TeleAggregateFieldElement field) {
        fields.add(field);
    }

    public void addAggregate(TeleAggregateElement aggregate) {
        subAggregates.add(aggregate);
    }

    public ClassElement originClass() {
        return originClass;
    }

    public List<TeleAggregateFieldElement> fields() {
        return fields;
    }

    public List<TeleAggregateElement> subAggregates() {
        return subAggregates;
    }
}
