package moonstone.selene.service;

import java.util.List;

import org.apache.commons.lang3.Validate;

import moonstone.selene.SeleneException;
import moonstone.selene.dao.GatewayDao;
import moonstone.selene.data.Gateway;

public class GatewayService extends ServiceAbstract {

    private static class SingletonHolder {
        private static final GatewayService SINGLETON = new GatewayService();
    }

    public static GatewayService getInstance() {
        return SingletonHolder.SINGLETON;
    }

    private final GatewayDao gatewayDao;

    protected GatewayService() {
        gatewayDao = GatewayDao.getInstance();
    }

    public Gateway findOne() {
        List<Gateway> all = gatewayDao.findAll();
        if (all.size() > 1) {
            throw new SeleneException("ERROR: more than one Gateway record found!");
        }
        if (all.size() == 1) {
            return all.get(0);
        } else {
            return null;
        }
    }

    public Gateway update(Gateway gateway) {
        Validate.notNull(gateway, "gateway is null");
        gatewayDao.update(gateway);
        return gateway;
    }

    protected GatewayDao getGatewayDao() {
        return gatewayDao;
    }
}
