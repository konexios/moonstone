package com.arrow.selene.engine.service;

import com.arrow.acn.client.model.IbmConfigModel;
import com.arrow.selene.data.Ibm;

public class IbmService extends com.arrow.selene.service.IbmService {

    private static class SingletonHolder {
        private static final IbmService singleton = new IbmService();
    }

    public static IbmService getInstance() {
        return SingletonHolder.singleton;
    }

    public Ibm upsert(IbmConfigModel model) {
        String method = "upsert";

        boolean insert = false;
        Ibm ibm = findOne();
        if (ibm == null) {
            ibm = new Ibm();
            insert = true;
        }

        // populate
        ibm = fromModel(ibm, model);

        // default to enabled
        ibm.setEnabled(true);

        if (insert) {
            getIbmDao().insert(ibm);
            logInfo(method, "inserted ibm record, organization: %s", ibm.getOrganizationId());
        } else {
            getIbmDao().update(ibm);
            logInfo(method, "updated ibm record, organization: %s", ibm.getOrganizationId());
        }

        return ibm;
    }

    public void checkAndDisable() {
        String method = "checkAndDisable";
        Ibm ibm = findOne();
        if (ibm != null && ibm.isEnabled()) {
            ibm.setEnabled(false);
            getIbmDao().update(ibm);
            logInfo(method, "set IBM profile to disabled");
        }
    }

    public Ibm fromModel(Ibm ibm, IbmConfigModel model) {
        ibm.setAuthMethod(model.getAuthMethod());
        ibm.setAuthToken(getCryptoService().encrypt(model.getAuthToken()));
        ibm.setGatewayId(model.getGatewayId());
        ibm.setGatewayType(model.getGatewayType());
        ibm.setOrganizationId(model.getOrganizationId());
        return ibm;
    }

    public IbmConfigModel toModel(IbmConfigModel model, Ibm ibm) {
        model.setAuthMethod(ibm.getAuthMethod());
        model.setAuthToken(getCryptoService().encrypt(ibm.getAuthToken()));
        model.setGatewayId(ibm.getGatewayId());
        model.setGatewayType(ibm.getGatewayType());
        model.setOrganizationId(ibm.getOrganizationId());
        return model;
    }
}
