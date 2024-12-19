package colesico.framework.profile.internal;

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.profile.ProfileConverterBindings;
import colesico.framework.profile.ProfileUtils;
import colesico.framework.profile.PropertyConverter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class ProfileUtilsImpl implements ProfileUtils<ProfileImpl> {

    protected final Map<Class<?>, PropertyConverter<?>> valueConvertersByClass = new HashMap<>();
    protected final Map<String, PropertyConverter<?>> valueConvertersByProperty = new HashMap<>();

    public ProfileUtilsImpl(Polysupplier<ProfileConverterBindings> profConverters) {
        profConverters.forEach(cfg -> {
                    cfg.forEach(cvb -> {
                                valueConvertersByClass.put(cvb.propertyClass(), cvb.converter());
                                valueConvertersByProperty.put(cvb.converter().getTagKey(), cvb.converter());
                            }
                    );
                }
        );
    }

    protected PropertyConverter getValueConverter(Class valueCalss) {
        PropertyConverter conv = valueConvertersByClass.get(valueCalss);
        if (conv == null) {
            throw new RuntimeException("Profile value converter not found for class: " + valueCalss);
        }
        return conv;
    }

    protected PropertyConverter getValueConverter(String propertyName) {
        PropertyConverter conv = valueConvertersByProperty.get(propertyName);
        if (conv == null) {
            throw new RuntimeException("Profile value converter not found for property: " + propertyName);
        }
        return conv;
    }

    @Override
    public ProfileImpl create(Collection<?> attributes, Collection<?> preferences) {
        ProfileImpl profile = new ProfileImpl();
        if (attributes != null) {
            attributes.forEach(profile::setAttribute);
        }
        if (preferences != null) {
            preferences.forEach(profile::setAttribute);
        }
        return profile;
    }

    @Override
    public Collection<?> getAttributes(ProfileImpl profile) {
        return profile.getAttributes();
    }

    @Override
    public Collection<?> getPreferences(ProfileImpl profile) {
        return profile.getPreferences();
    }

    @Override
    public Collection<?> getProperties(ProfileImpl profile) {
        return profile.getProperties().values();
    }

    @Override
    public <T> T setAttribute(ProfileImpl profile, T property) {
        return profile.setAttribute(property);
    }

    @Override
    public <T> T setPreference(ProfileImpl profile, T property) {
        return profile.setPreference(property);
    }

    @Override
    public Map<String, String> toTags(Collection<?> properties) {
        Map<String, String> tags = new HashMap<>();
        for (Object property : properties) {
            PropertyConverter converter = getValueConverter(property.getClass());
            tags.put(converter.getTagKey(), converter.toTag(property));
        }
        return tags;
    }

    @Override
    public Collection<?> fromTags(Map<String, String> tags) {
        Collection<Object> properties = new ArrayList<>();
        for (Map.Entry<String, String> prop : tags.entrySet()) {
            String propertyName = prop.getKey();
            PropertyConverter<?> converter = getValueConverter(propertyName);
            Object value = converter.fromTag(prop.getValue());
            properties.add(value);
        }
        return properties;
    }

    @Override
    public byte[] toBytes(Collection<?> properties) {
        byte[] result = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(baos)) {

            for (Object property : properties) {
                PropertyConverter converter = getValueConverter(property.getClass());
                byte[] tagKeyBytes = converter.getTagKey().getBytes(StandardCharsets.UTF_8);
                dos.write(tagKeyBytes.length);
                dos.write(tagKeyBytes);
                byte[] propBytes = converter.toBytes(property);
                dos.write(propBytes.length);
                dos.write(propBytes);
            }
            result = baos.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return result;
    }

    @Override
    public Collection<?> fromBytes(byte[] propertiesBytes) {
        Collection<Object> properties = new ArrayList<>();
        try (ByteArrayInputStream bais = new ByteArrayInputStream(propertiesBytes);
             DataInputStream dis = new DataInputStream(bais)) {

            while (dis.available() > 0) {
                int tagNameLen = dis.read();
                String tagName = new String(dis.readNBytes(tagNameLen), StandardCharsets.UTF_8);
                int propBytesLen = dis.read();
                byte[] propBytes = dis.readNBytes(propBytesLen);
                PropertyConverter<?> converter = getValueConverter(tagName);
                properties.add(converter.fromBytes(propBytes));
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return properties;
    }


}
