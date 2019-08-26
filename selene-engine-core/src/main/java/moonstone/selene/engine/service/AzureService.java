package moonstone.selene.engine.service;

import moonstone.acn.client.model.AzureConfigModel;
import moonstone.selene.data.Azure;

public class AzureService extends moonstone.selene.service.AzureService {

    private static class SingletonHolder {
        static final AzureService SINGLETON = new AzureService();
    }

    public static AzureService getInstance() {
        return SingletonHolder.SINGLETON;
    }

    public Azure upsert(AzureConfigModel model) {
        String method = "upsert";

        boolean insert = false;
        Azure azure = findOne();
        if (azure == null) {
            azure = new Azure();
            insert = true;
        }

        // populate
        azure = populate(azure, model);

        // default to enabled
        azure.setEnabled(true);

        if (insert) {
            getAzureDao().insert(azure);
            logInfo(method, "inserted azure record, host: %s", azure.getHost());
        } else {
            getAzureDao().update(azure);
            logInfo(method, "updated azure record, host: %s", azure.getHost());
        }

        return azure;
    }

    public void checkAndDisable() {
        String method = "checkAndDisable";
        Azure azure = findOne();
        if (azure != null && azure.isEnabled()) {
            azure.setEnabled(false);
            getAzureDao().update(azure);
            logInfo(method, "set azure profile to disabled");
        }
    }

    private Azure populate(Azure azure, AzureConfigModel model) {
        azure.setHost(model.getHost());
        azure.setAccessKey(getCryptoService().encrypt(model.getAccessKey()));
        return azure;
    }
}
