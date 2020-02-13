/*
 * Copyright © 2014-2020 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.example.web.pebble;

import colesico.framework.htmlrenderer.HtmlRenderer;
import colesico.framework.ioc.annotation.Classed;
import colesico.framework.pebble.PebbleRenderer;
import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

@Weblet
public class MyPebbleHtmlPage {

    private final HtmlRenderer<String> htmlRender;

    public MyPebbleHtmlPage(@Classed(PebbleRenderer.class) HtmlRenderer<String> htmlRenderer) {
        this.htmlRender = htmlRenderer;
    }

    //  http://localhost:8080/my-pebble-html-page/print?val=10
    public HtmlResponse print(Integer val){
        return htmlRender.render("$tmplRoot/MyPebbleTemplate",val);
    }
}
