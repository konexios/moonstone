package moonstone.selene.engine;

import moonstone.acs.VersionAbstract;
import moonstone.acs.client.model.VersionModel;

public class SeleneEngineCoreVersion extends VersionAbstract {
    private final static VersionModel VERSION = readManifest(SeleneEngineCoreVersion.class);

    public final static VersionModel get() {
        return VERSION;
    }
}
