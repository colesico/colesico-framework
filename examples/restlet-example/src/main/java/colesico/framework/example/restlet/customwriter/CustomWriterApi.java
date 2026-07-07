package colesico.framework.example.restlet.customwriter;

import colesico.framework.restlet.Restlet;
import colesico.framework.telehttp.UseWriter;

@Restlet
public class CustomWriterApi {

    /**
     * GET /custom-writer-api/uppercase
     */
    @UseWriter(UppercaseWriter.class)
    public String uppercase(){
        return "write me!";
    }

}
