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

    protected final Map<String, PropertyUtils<Profile, Object>> propertyUtils = new HashMap<>();
    protected final Set<PropertyUtils<Profile, Object>> attributUtils = new HashSet<>();
    protected final Set<PropertyUtils<Profile, Object>> preferenceUtils = new HashSet<>();

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
    public Profile createProfile(Map<String, Object> values) {
        final Profile profile = config.createNewProfile();
        var allValues = config.createDefaultValues();
        allValues.putAll(values);
        allValues.forEach((propertyName, value) -> {
            getPropertyUtils(propertyName).setValue(profile, value);
        });
        return profile;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Profile createProfile() {
        final Profile profile = config.createNewProfile();
        Map<String, Object> values = config.createDefaultValues();
        values.forEach((propertyName, value) -> {
            getPropertyUtils(propertyName).setValue(profile, value);
        });
        return profile;
    }

    @Override
    public Map<String, Object> getValues(Profile profile) {
        Map<String, Object> values = new HashMap<>();
        propertyUtils.values().forEach(pu -> {
            values.put(pu.getName(), pu.getValue(profile));
        });
        return values;
    }

    @Override
    public Map<String, Object> getAttributes(Profile profile) {
        Map<String, Object> values = new HashMap<>();
        attributUtils.forEach(pu -> {
            values.put(pu.getName(), pu.getValue(profile));
        });
        return values;
    }

    @Override
    public Map<String, Object> getPreferences(Profile profile) {
        Map<String, Object> values = new HashMap<>();
        preferenceUtils.forEach(pu -> {
            values.put(pu.getName(), pu.getValue(profile));
        });
        return values;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, String> toProperties(Map<String, Object> values) {
        Map<String, String> properties = new HashMap<>();
        values.forEach((propName, propValue) -> {
            properties.put(propName, getPropertyUtils(propName).toProperty(propValue));
        });
        return properties;
    }

    @Override
    public Map<String, Object> fromProperties(Map<String, String> properties) {
        Map<String, Object> values = new HashMap<>();
        properties.forEach((propName, tag) -> {
            var pu = getPropertyUtils(propName);
            values.put(pu.getName(), pu.fromProperty(tag));
        });
        return values;
    }

    @Override
    @SuppressWarnings("unchecked")
    public byte[] toBytes(Map<String, Object> values) {
        byte[] bytes = null;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(baos)) {

            for (Map.Entry<String, Object> me : values.entrySet()) {
                var pu = getPropertyUtils(me.getKey());
                byte[] propNameBytes = pu.getName().getBytes(StandardCharsets.UTF_8);
                dos.write(propNameBytes.length);
                dos.write(propNameBytes);
                byte[] propBytes = pu.toBytes(me.getValue());
                dos.write(propBytes.length);
                dos.write(propBytes);
            }

            bytes = baos.toByteArray();
        } catch (Exception ex) {
            throw new ProfileException(ex);
        }

        return bytes;
    }

    @Override
    public Map<String, Object> fromBytes(byte[] bytes) {
        Map<String, Object> values = new HashMap<>();
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             DataInputStream dis = new DataInputStream(bais)) {

            while (dis.available() > 0) {
                int propNameLen = dis.read();
                String propName = new String(dis.readNBytes(propNameLen), StandardCharsets.UTF_8);
                int propBytesLen = dis.read();
                byte[] propBytes = dis.readNBytes(propBytesLen);
                var pu = getPropertyUtils(propName);
                values.put(pu.getName(), pu.fromBytes(propBytes));
            }

        } catch (Exception ex) {
            throw new ProfileException(ex);
        }

        return values;
    }

}
