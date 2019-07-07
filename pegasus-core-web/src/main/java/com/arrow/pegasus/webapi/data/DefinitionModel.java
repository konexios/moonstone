package com.arrow.pegasus.webapi.data;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class DefinitionModel implements Serializable {
    private static final long serialVersionUID = 6504508707320755854L;

    private final String id;
    private final String name;
    private final String description;

    public DefinitionModel(String id, String name, String description) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof DefinitionModel))
            return false;
        DefinitionModel dm = (DefinitionModel) obj;
        return StringUtils.equals(id, dm.getId());
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }
}
