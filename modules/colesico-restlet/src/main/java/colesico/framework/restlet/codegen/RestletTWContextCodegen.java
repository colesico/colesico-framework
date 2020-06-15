package colesico.framework.restlet.codegen;

import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.weblet.codegen.WebletTWContextCodegen;

public class RestletTWContextCodegen extends WebletTWContextCodegen {
    public RestletTWContextCodegen(TeleMethodElement teleMethod, Class<?> contextClass) {
        super(teleMethod, contextClass);
    }
}
