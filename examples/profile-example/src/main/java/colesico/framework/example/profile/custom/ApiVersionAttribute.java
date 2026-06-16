package colesico.framework.example.profile.custom;

public class ApiVersionAttribute extends AbstractProfileAttribute<CustomProfile, String> {

    static final String ATTRIBUTE_NAME = "api_version";

    public ApiVersionAttribute(CustomProfile profile, String name) {
        super(profile, name);
    }

    @Override
    public String name() {
        return ATTRIBUTE_NAME;
    }

    @Override
    public String value() {
        return profile.apiVersion();
    }

    @Override
    public void setValue(String value) {
        profile.setApiVersion(value);
    }

    @Override
    public String asString() {
        return profile.apiVersion();
    }

    @Override
    public void setString(String value) {
        profile.setApiVersion(value);
    }


}
