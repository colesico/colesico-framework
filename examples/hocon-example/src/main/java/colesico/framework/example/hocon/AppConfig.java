package colesico.framework.example.hocon;

import colesico.framework.config.Config;
import colesico.framework.config.UseFileSource;

@Config
@UseFileSource
public class AppConfig {
   private Integer val;

    public Integer getVal() {
        return val;
    }

    public void setVal(Integer val) {
        this.val = val;
    }
}
