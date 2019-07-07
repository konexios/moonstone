package com.arrow.pegasus.webapi.data;

import java.io.Serializable;

public class IdModel implements Serializable {
    private static final long serialVersionUID = 6895206911781978130L;

    private String id;
    private String message;

    public IdModel() {
    }

    public IdModel(String id) {
        this.id = id;
    }

    public IdModel(String id, String message) {
        this(id);
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
