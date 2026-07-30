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

        @Aggregate
        private Nested nested;

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

        public Nested getNested() {
            return nested;
        }

        public void setNested(Nested nested) {
            this.nested = nested;
        }
    }

    public static class Nested{
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
