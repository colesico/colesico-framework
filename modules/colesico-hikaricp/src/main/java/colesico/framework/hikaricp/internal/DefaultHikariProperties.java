package colesico.framework.hikaricp.internal;

import colesico.framework.config.Config;
import colesico.framework.config.DefaultMessage;
import colesico.framework.hikaricp.HikariProperties;
import colesico.framework.ioc.conditional.Substitute;

import static colesico.framework.ioc.conditional.Substitution.STUB;

@Config
@DefaultMessage
@Substitute(STUB)
public class DefaultHikariProperties extends HikariProperties {
}
