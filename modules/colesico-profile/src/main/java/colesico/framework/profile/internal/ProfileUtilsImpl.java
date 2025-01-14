package colesico.framework.profile.internal;

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.profile.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProfileUtilsImpl implements ProfileUtils {

    private final ProfileConfigPrototype config;

    protected final Map<String, PropertyUtils<Profile, ?>> propertyUtils = new HashMap<>();
    protected final Set<PropertyUtils<Profile, ?>> attributUtils = new HashSet<>();
    protected final Set<PropertyUtils<Profile, ?>> preferenceUtils = new HashSet<>();

    public ProfileUtilsImpl(ProfileConfigPrototype config, Polysupplier<PropertyUtils> propertyUtilsSup) {
        this.config = config;

        propertyUtilsSup.forEach(pu -> {
                    this.propertyUtils.put(pu.getName(), pu);
                    switch (pu.getKind()) {
                        case ATTRIBUTE -> attributUtils.add(pu);
                        case PREFERENCE -> preferenceUtils.add(pu);
                        default -> throw new ProfileException("Unsupported profile property type: " + pu.getKind());
                    }
                }
        );
    }

    protected PropertyUtils getPropertyUtils(String propertyName) {
        var pu = propertyUtils.get(propertyName);
        if (pu == null) {
            throw new ProfileException("Profile property utils not found for property name: " + propertyName);
        }
        return pu;
    }

    @Override
    @SuppressWarnings("unchecked")
    public colesico.framework.profile.Profile createProfile(Map<String, ?> properties) {
        final colesico.framework.profile.Profile profile = config.createProfile();
        if (properties != null) {
            properties.forEach((propName, propValue) -> {
                getPropertyUtils(propName).setValue(profile, propValue);
            });
        }

        return profile;
    }

    @Override
    public Map<String, ?> getProperties(Profile profile) {
        Map<String, ? super Object> result = new HashMap<>();
        propertyUtils.values().forEach(pu -> {
            result.put(pu.getName(), pu.getValue(profile));
        });
        return result;
    }

    @Override
    public Map<String, ?> getAttributes(Profile profile) {
        Map<String, ? super Object> attributes = new HashMap<>();
        attributUtils.forEach(pu -> {
            attributes.put(pu.getName(), pu.getValue(profile));
        });
        return attributes;
    }

    @Override
    public Map<String, ?> getPreferences(Profile profile) {
        Map<String, ? super Object> preferences = new HashMap<>();
        preferenceUtils.forEach(pu -> {
            preferences.put(pu.getName(), pu.getValue(profile));
        });
        return preferences;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, String> toTags(Map<String, ?> properties) {
        Map<String, String> tags = new HashMap<>();
        properties.forEach((propName, propValue) -> {
            tags.put(propName, getPropertyUtils(propName).toTag(propValue));
        });
        return tags;
    }

    @Override
    public Map<String, ?> fromTags(Map<String, String> tags) {
        Map<String, ? super Object> properties = new HashMap<>();
        tags.forEach((propName, tag) -> {
            var pu = getPropertyUtils(propName);
            properties.put(pu.getName(), pu.fromTag(tag));
        });
        return properties;
    }

    @Override
    @SuppressWarnings("unchecked")
    public byte[] toBytes(Map<String, ?> properties) {
        byte[] result = null;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(baos)) {

            for (Map.Entry<String, ?> me : properties.entrySet()) {
                var pu = getPropertyUtils(me.getKey());
                byte[] propNameBytes = pu.getName().getBytes(StandardCharsets.UTF_8);
                dos.write(propNameBytes.length);
                dos.write(propNameBytes);
                byte[] propBytes = pu.toBytes(me.getValue());
                dos.write(propBytes.length);
                dos.write(propBytes);
            }

            result = baos.toByteArray();
        } catch (Exception ex) {
            throw new ProfileException(ex);
        }

        return result;
    }

    @Override
    public Map<String, ?> fromBytes(byte[] bytes) {
        Map<String, ? super Object> properties = new HashMap<>();
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             DataInputStream dis = new DataInputStream(bais)) {

            while (dis.available() > 0) {
                int propNameLen = dis.read();
                String propName = new String(dis.readNBytes(propNameLen), StandardCharsets.UTF_8);
                int propBytesLen = dis.read();
                byte[] propBytes = dis.readNBytes(propBytesLen);
                var pu = getPropertyUtils(propName);
                properties.put(pu.getName(), pu.fromBytes(propBytes));
            }

        } catch (Exception ex) {
            throw new ProfileException(ex);
        }

        return properties;
    }

}
