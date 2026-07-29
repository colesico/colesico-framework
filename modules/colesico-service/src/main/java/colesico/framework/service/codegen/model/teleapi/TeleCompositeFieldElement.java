package colesico.framework.service.codegen.model.teleapi;

public class TeleCompositeFieldElement implements TeleReadableElement  {

    private TeleReadElement readSpec;

    public TeleCompositeFieldElement(TeleReadElement readSpec) {
        this.readSpec = readSpec;
    }

    @Override
    public TeleReadElement readSpec() {
        return readSpec;
    }

    @Override
    public void setReadSpec(TeleReadElement readSpec) {
        this.readSpec = readSpec;
    }
}
