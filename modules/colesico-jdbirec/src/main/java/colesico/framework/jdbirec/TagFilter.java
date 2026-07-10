package colesico.framework.jdbirec;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

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
 * A tag,  consisting of the path of the composition fields to the element and the name of
 * this element, is automatically added to the tags of each element (composition or column),
 * i.e. #foo.bar.compName or #foo.bar.baz.columnName
 * </p>
 * <p>
 * Tag filter fields are combined with an AND condition, i.e.
 * oneOf and anyOn and noneOf...
 * </p>
 * <p>
 * Applying tag filters starts with filters in column compositions
 * and works up the object hierarchy to the tag filter specified for view
 * </p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface TagFilter {

    String TG_FULL = "#full";
    String TG_BRIEF = "#brief";

    /**
     * Tag filter -  not empty tag set
     */
    String TF_HAS_TAGS = "?has-tags";

    /**
     * Tag filter - empty tag set
     */
    String TF_NO_TAGS = "?no-tags";

    /**
     * Accepts for any of the given tag.
     * Analogue of SQL IN(...)
     */
    String[] anyOf() default {};

    /**
     * Accepts for none of the given tag.
     * Analogue of SQL NOT IN(...)
     */
    String[] noneOf() default {};

}
