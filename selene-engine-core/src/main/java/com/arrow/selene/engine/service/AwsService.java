package com.arrow.selene.engine.service;

import com.arrow.acn.client.model.AwsConfigModel;
import com.arrow.selene.data.Aws;
import com.arrow.selene.service.CryptoService;

public class AwsService extends com.arrow.selene.service.AwsService {

    private static class SingletonHolder {
        static final AwsService SINGLETON = new AwsService();
    }

    public static AwsService getInstance() {
        return SingletonHolder.SINGLETON;
    }

    public Aws upsert(AwsConfigModel model) {
        String method = "upsert";

        boolean insert = false;
        Aws aws = findOne();
        if (aws == null) {
            aws = new Aws();
            insert = true;
        }

        // populate data
        aws = populate(aws, model);

        // default to enabled
        aws.setEnabled(true);

        if (insert) {
            getAwsDao().insert(aws);
            logInfo(method, "inserted aws record, host: %s", aws.getHost());
        } else {
            getAwsDao().update(aws);
            logInfo(method, "updated  aws record, host: %s", aws.getHost());
        }

        return aws;
    }

    public void checkAndDisable() {
        String method = "checkAndDisable";
        Aws aws = findOne();
        if (aws != null && aws.isEnabled()) {
            aws.setEnabled(false);
            getAwsDao().update(aws);
            logInfo(method, "set AWS profile to disabled");
        }
    }

    private Aws populate(Aws aws, AwsConfigModel model) {
        aws.setHost(model.getHost());
        aws.setPort(model.getPort());
        CryptoService crypto = getCryptoService();
        aws.setRootCert(crypto.encrypt(model.getCaCert()));
        aws.setClientCert(crypto.encrypt(model.getClientCert()));
        aws.setPrivateKey(crypto.encrypt(model.getPrivateKey()));
        return aws;
    }
}
