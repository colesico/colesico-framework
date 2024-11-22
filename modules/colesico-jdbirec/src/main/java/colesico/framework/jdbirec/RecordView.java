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
     * <p>
     * Bind to record a columns and compositions associated with
     * the tags accepted by this filter.
     * </p>
     * <p>
     * For each record field can be specified several columns or
     * compositions with different tags. Only the columns and compositions
     * with acceptable by filter tags will be bound to the record.
     * </p>
     * <p>
     * It is possible to specify  {@link Composition#tags()}
     * or {@link Column#tags()} as global tags or local ones.
     * Global tags begin with the '#' symbol, i.e. #theTag
     * Local tags are automatically converted to global tags
     * by adding '#' symbol and  the set of fields of the path
     * to the record field, i.e. theTag -> #foo.bar.theTag
     * </p>
     * <p>
     * A 'field' tag, consisting of the set of fields of the path to the record field,
     * is always automatically added to the set of tags
     * i.e. #foo.bar.theField
     * </p>
     *
     * @see Column#tags()
     * @see Composition#tags()
     * @see Composition#tagFilter()
     */
    String tagFilter() default ALL_TAGS_FILTER;
}
