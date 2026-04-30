package colesico.framework.example.profile.custom;

public class ApiVersionProperty extends AbstractProfileAttribute<CustomProfile, String> {

    static final String ATTRIBUTE_NAME = "api_version";

    public ApiVersionProperty(CustomProfile profile) {
        super(profile);
    }

    @Override
    public String name() {
        return ATTRIBUTE_NAME;
    }

    @Override
    public String getValue() {
        return profile.getApiVersion();
    }

    @Override
    public void setValue(String value) {
        profile.setApiVersion(value);
    }

    @Override
    public String getString() {
        return profile.getApiVersion();
    }

    @Override
    public void setString(String value) {
        profile.setApiVersion(value);
    }

    @Override
    protected void initMetadata() {
        super.initMetadata();
        metadata.dataPortWritable = false;
    }
}
