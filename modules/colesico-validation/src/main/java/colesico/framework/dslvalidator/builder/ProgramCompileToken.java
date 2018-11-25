package colesico.framework.dslvalidator.builder;

import colesico.framework.dslvalidator.Chain;
import colesico.framework.dslvalidator.DSLValidator;
import colesico.framework.validation.Validator;

public final class ProgramCompileToken {


    private final ValidationProgramBuilder programBuilder;
    private final String rootSubject;

    public ProgramCompileToken(ValidationProgramBuilder programBuilder, String rootSubject) {
        this.programBuilder = programBuilder;
        this.rootSubject = rootSubject;
    }

    public <V> Validator<V> compile() {
        if (programBuilder.getStack().size() != 1) {
            throw new RuntimeException("Incomplete validation model");
        }
        Chain chain = programBuilder.getStack().pop();
        return new DSLValidator<>(rootSubject, chain);
    }
}

