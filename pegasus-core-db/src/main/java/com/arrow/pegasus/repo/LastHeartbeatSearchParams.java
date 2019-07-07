package com.arrow.pegasus.repo;

import java.util.EnumSet;
import java.util.Set;

import com.arrow.pegasus.data.heartbeat.HeartbeatObjectType;
import com.arrow.pegasus.repo.params.PegasusDocumentSearchParams;

public class LastHeartbeatSearchParams extends PegasusDocumentSearchParams {
    private static final long serialVersionUID = -7073776590754034202L;

    private EnumSet<HeartbeatObjectType> objectTypes;
    private Set<String> objectIds;

    public EnumSet<HeartbeatObjectType> getObjectTypes() {
        return objectTypes;
    }

    public LastHeartbeatSearchParams addObjectTypes(HeartbeatObjectType... objectTypes) {
        if (objectTypes == null || objectTypes.length <= 0)
            return this;
        if (this.objectTypes == null) {
            this.objectTypes = EnumSet.noneOf(HeartbeatObjectType.class);
        }
        for (HeartbeatObjectType t : objectTypes) {
            this.objectTypes.add(t);
        }
        return this;
    }

    public Set<String> getObjectIds() {
        return objectIds;
    }

    public LastHeartbeatSearchParams addObjectIds(String... objectIds) {
        this.objectIds = super.addValues(this.objectIds, objectIds);

        return this;
    }
}
