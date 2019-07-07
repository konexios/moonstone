package com.arrow.pegasus.data.heartbeat;

import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;

@Document(collection = "last_heartbeat")
public class LastHeartbeat extends HeartbeatAbstract {
    private static final long serialVersionUID = -3135553606493920986L;

    @Override
    protected String getProductPri() {
        return CoreConstant.PegasusPri.BASE;
    }

    @Override
    protected String getTypePri() {
        return CoreConstant.PegasusPri.LAST_HEARTBEAT;
    }
}
