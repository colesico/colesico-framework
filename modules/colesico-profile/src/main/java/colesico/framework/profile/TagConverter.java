package colesico.framework.profile;

public interface TagConverter<T> {

    String[] toTag(T item);

    T fromTag(String tagKey, String tagValue);

}
