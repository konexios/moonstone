package com.arrow.apollo.web.model;

import java.io.Serializable;

/**
 * Created by dantonov on 30.10.2017.
 */
public class WidgetModels {

    public static class WidgetType implements Serializable{
        private static final long serialVersionUID = 1858523434841404067L;

        private String id;
        private String name;
        private String description;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public WidgetType withId(String id) {
            setId(id);
            return this;
        }

        public WidgetType withName(String name) {
            setName(name);
            return this;
        }

        public WidgetType withDescription(String description) {
            setDescription(description);
            return this;
        }

    }
}
