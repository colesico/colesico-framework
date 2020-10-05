package colesico.framework.example.hocon;

import colesico.framework.config.Config;
import colesico.framework.config.UseFileSource;

@Config
@UseFileSource(prefix = "hocon",  file = "hocon.conf")
public class MyConfig {

    private String strValue;
    private Integer intValue;
    private String str2Value;

    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public String getStr2Value() {
        return str2Value;
    }

    public void setStr2Value(String str2Value) {
        this.str2Value = str2Value;
    }

    @Override
    public String toString() {
        return "MyConfig{" +
                "strValue='" + strValue + '\'' +
                ", intValue=" + intValue +
                ", str2Value='" + str2Value + '\'' +
                '}';
    }
}
