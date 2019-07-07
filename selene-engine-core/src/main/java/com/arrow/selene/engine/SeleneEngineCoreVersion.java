package com.arrow.selene.engine;

import com.arrow.acs.VersionAbstract;
import com.arrow.acs.client.model.VersionModel;

public class SeleneEngineCoreVersion extends VersionAbstract {
    private final static VersionModel VERSION = readManifest(SeleneEngineCoreVersion.class);

    public final static VersionModel get() {
        return VERSION;
    }
}
