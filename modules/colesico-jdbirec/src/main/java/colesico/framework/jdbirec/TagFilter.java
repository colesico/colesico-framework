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
 * A 'field' tag, consisting of the set of fields of the path to the record field,
 * is always automatically added to the set of tags
 * i.e. #foo.bar.theField
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


    String[] oneOf() default {};

    String[] anyOf() default {};

    String[] noneOf() default {};

}
