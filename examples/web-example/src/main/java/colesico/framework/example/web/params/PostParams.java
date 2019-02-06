package colesico.framework.example.web.params;

import colesico.framework.http.HttpMethod;
import colesico.framework.router.RequestMethod;
import colesico.framework.weblet.*;

@Weblet
public class PostParams {

    // http://localhost:8080/post-params/form?action=default-action
    // http://localhost:8080/post-params/form?action=advanced-action?getparam=1
    public HtmlResponse form(String action){
        StringBuilder sb= new StringBuilder();
        sb.append("<form method='post'>")
                .append("<input type='text' name='formval' value=''/>")
                .append("<input type='submit' value='Submit' formaction='/post-params/"+action+"'/>")
                .append("</form>");

        return new HtmlResponse(sb.toString());
    }

    // for http://localhost:8080/post-params/form?action=default-action
    @RequestMethod(HttpMethod.POST)
    public HtmlResponse defaultAction(String formval){
        return new HtmlResponse("formval="+formval);
    }

    //for http://localhost:8080/post-params/form?action=advanced-action?extraparam=1
    @RequestMethod(HttpMethod.POST)
    public HtmlResponse advancedAction(
                                       /* formval is a get or post param */String formval,
                                       /* getVal is a get or post param  */@ParamName("extraparam") Integer getVal,
                                       /* postVal is a post param only */  @ParamName("extraparam") @ParamOrigin(Origin.POST) Integer postVal){

        return new HtmlResponse("formval="+formval+"; extraparam(get)="+getVal+"; extraparam(post)="+postVal);
    }
}
