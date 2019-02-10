package colesico.framework.dao;

public class DTOHelperFactory {
    public static String HELPER_CLASS_SUFFIX = "Helper";

    public static <D, H extends DTOHelper<D>> H getHelper(Class<D> dtoClass) {
        String helperClassName = dtoClass.getName() + HELPER_CLASS_SUFFIX;
        try {
            Class<H> helperClass = (Class<H>) Class.forName(helperClassName);
            return helperClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Unknown helper implementation: " + helperClassName + " for DTO class: " + dtoClass.getName());
        }
    }
}
