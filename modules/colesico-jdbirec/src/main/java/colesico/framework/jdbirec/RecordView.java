package colesico.framework.jdbirec;


import java.lang.annotation.*;

/**
 * Record View.
 * <p>
 * To be able to work within the same record with different sets of fields of this record,
 * the system of views is used. Each view includes a specific set of record fields.
 * View name must consist of letters and numbers only.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface RecordView {

    String DEFAULT_VIEW = "default";
    String FULL_VIEW = "full";
    String BRIEF_VIEW = "brief";

    String ALL_TAGS_FILTER = "*";

    /**
     * View name
     */
    String name() default DEFAULT_VIEW;

    /**
     * View tag filter
     *
     * @see TagFilter
     * @see Column#tags()
     * @see Composition#tags()
     */
    TagFilter tagFilter() default @TagFilter;
}
