package colesico.framework.example.web.params;

import colesico.framework.service.Aggregate;
import colesico.framework.weblet.Weblet;

@Weblet
public class AggregateParams {


    public String action(@Aggregate Form formData) {
        return formData.id + formData.name;
    }

    public static class Form {
        private Long id;
        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
