package moonstone.selene.service;

import java.util.List;

import moonstone.selene.dao.IbmDao;
import moonstone.selene.data.Ibm;

public class IbmService extends ServiceAbstract {

    private static class SingletonHolder {
        private static final IbmService SINGLETON = new IbmService();
    }

    public static IbmService getInstance() {
        return SingletonHolder.SINGLETON;
    }

    private final IbmDao ibmDao;

    protected IbmService() {
        super();
        ibmDao = IbmDao.getInstance();
    }

    public Ibm findOne() {
        List<Ibm> all = ibmDao.findAll();
        if (all.size() > 1) {
            throw new RuntimeException("ERROR: more than one IBM record found!");
        }
        if (all.size() == 1) {
            return all.get(0);
        } else {
            return null;
        }
    }

    protected IbmDao getIbmDao() {
        return ibmDao;
    }
}
