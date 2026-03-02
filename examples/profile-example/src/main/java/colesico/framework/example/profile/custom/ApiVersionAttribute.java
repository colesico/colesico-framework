package colesico.framework.example.profile.custom;

import colesico.framework.profile.AbstractProfileAttribute;

public class ApiVersionAttribute extends AbstractProfileAttribute<CustomProfile, String> {

    static final String ATTRIBUTE_NAME = "api_version";

    public ApiVersionAttribute(CustomProfile profile) {
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
    public boolean readonly() {
        return true;
    }
}
