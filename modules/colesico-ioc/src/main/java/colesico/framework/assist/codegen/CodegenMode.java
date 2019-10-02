package colesico.framework.assist.codegen;

import org.apache.commons.lang3.StringUtils;
/*

  Usage in maven compiler plugin:
  <compilerArgs>
    <!-- Code generation model: default|optimized -->
    <arg>-Acolesico.framework.codegen=${codegen-mode}</arg>
  </compilerArgs>

 */
public enum CodegenMode {

    DEFAULT("default"),
    OPTIMIZED("optimized");

    private final String key;

    CodegenMode(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public boolean isDefault() {
        return "default".equals(key);
    }

    public boolean isOptimized() {
        return "optimized".equals(key);
    }

    public static CodegenMode fromKey(String key) {
        // Default codegen mode is for production
        if (StringUtils.isBlank(key)) {
            return CodegenMode.DEFAULT;
        }

        // find out specified mode
        for (CodegenMode mode : CodegenMode.values()) {
            if (mode.getKey().equals(key)) {
                return mode;
            }
        }

        throw new RuntimeException("Unsupported code generation mode:" + key);
    }
}
