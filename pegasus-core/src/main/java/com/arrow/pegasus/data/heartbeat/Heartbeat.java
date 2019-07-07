package com.arrow.pegasus.data.heartbeat;

import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;

@Document(collection = "heartbeat")
public class Heartbeat extends HeartbeatAbstract {
    private static final long serialVersionUID = 8079576787094459378L;

    @Override
    protected String getProductPri() {
        return CoreConstant.PegasusPri.BASE;
    }

    @Override
    protected String getTypePri() {
        return CoreConstant.PegasusPri.HEART_BEAT;
    }
}
