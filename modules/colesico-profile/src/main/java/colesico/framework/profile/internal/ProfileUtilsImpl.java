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
    protected final ProfileListener listener;

    protected final Map<String, ProfileValueUtils<Profile, Object>> valuesUtils = new HashMap<>();
    protected final Set<ProfileValueUtils<Profile, Object>> attributUtils = new HashSet<>();
    protected final Set<ProfileValueUtils<Profile, Object>> preferenceUtils = new HashSet<>();

    public ProfileUtilsImpl(ProfileConfigPrototype config,
                            ProfileListener listener,
                            Polysupplier<ProfileValueUtils> propertyUtilsSup) {
        this.config = config;
        this.listener = listener;

        propertyUtilsSup.forEach(vu -> {
                    this.valuesUtils.put(vu.getName(), vu);
                    switch (vu.getKind()) {
                        case ATTRIBUTE -> attributUtils.add(vu);
                        case PREFERENCE -> preferenceUtils.add(vu);
                        default -> throw new ProfileException("Unsupported profile property type: " + vu.getKind());
                    }
                }
        );
    }


    @Override
    @SuppressWarnings("unchecked")
    public Profile createProfile(Map<String, Object> values) {
        final Profile profile = config.profileInstance();
        if (!values.isEmpty()) {
            values.forEach((propertyName, value) -> {
                var vu = valuesUtils.get(propertyName);
                if (vu != null) {
                    vu.setValue(profile, value);
                }
            });
        }
        return profile;
    }

    @Override
    public Profile createProfile() {
        return config.profileInstance();
    }

    @Override
    public Map<String, Object> getValues(Profile profile) {
        Map<String, Object> values = new HashMap<>();
        valuesUtils.values().forEach(vu -> {
            values.put(vu.getName(), vu.getValue(profile));
        });
        return values;
    }

    @Override
    public Map<String, Object> getAttributes(Profile profile) {
        Map<String, Object> values = new HashMap<>();
        attributUtils.forEach(vu -> {
            values.put(vu.getName(), vu.getValue(profile));
        });
        return values;
    }

    @Override
    public Map<String, Object> getPreferences(Profile profile) {
        Map<String, Object> values = new HashMap<>();
        preferenceUtils.forEach(vu -> {
            values.put(vu.getName(), vu.getValue(profile));
        });
        return values;
    }

    @Override
    public Map<String, String> toProperties(Map<String, Object> values) {
        Map<String, String> properties = new HashMap<>();
        values.forEach((propName, propValue) -> {
            var vu = valuesUtils.get(propName);
            if (vu != null) {
                properties.put(propName, vu.toProperty(propValue));
            }
        });
        return properties;
    }

    @Override
    public Map<String, Object> fromProperties(Map<String, String> properties) {
        Map<String, Object> values = new HashMap<>();
        properties.forEach((propName, propValue) -> {
            var vu = valuesUtils.get(propName);
            if (vu != null) {
                values.put(vu.getName(), vu.fromProperty(propValue));
            }
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
                var vu = valuesUtils.get(me.getKey());
                if (vu != null) {
                    byte[] propNameBytes = vu.getName().getBytes(StandardCharsets.UTF_8);
                    dos.write(propNameBytes.length);
                    dos.write(propNameBytes);
                    byte[] propBytes = vu.toBytes(me.getValue());
                    dos.write(propBytes.length);
                    dos.write(propBytes);
                }
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
                var vu = valuesUtils.get(propName);
                if (vu != null) {
                    values.put(vu.getName(), vu.fromBytes(propBytes));
                }
            }

        } catch (Exception ex) {
            throw new ProfileException(ex);
        }

        return values;
    }

}
