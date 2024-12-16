package colesico.framework.profile;

public interface PropertyConverter<P> {

    String getTagKey();

    String toTag(P property);

    P fromTag(String tag);

    byte[] toBytes(P property);

    P fromBytes(byte[] propertyBytes);
}
