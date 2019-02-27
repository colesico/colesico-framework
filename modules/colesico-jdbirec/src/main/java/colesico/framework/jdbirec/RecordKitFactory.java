package colesico.framework.jdbirec;

public class RecordKitFactory {

    public static String KIT_CLASS_SUFFIX = "RKit";

    public static <D, H extends RecordKit<D>> H getKit(Class<D> recordClass) {
        String helperClassName = recordClass.getName() + KIT_CLASS_SUFFIX;
        try {
            Class<H> helperClass = (Class<H>) Class.forName(helperClassName);
            return helperClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Unknown helper implementation: " + helperClassName + " for RECORD class: " + recordClass.getName());
        }
    }
}
