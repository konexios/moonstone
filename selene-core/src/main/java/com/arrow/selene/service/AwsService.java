package com.arrow.selene.service;

import java.util.List;

import com.arrow.selene.dao.AwsDao;
import com.arrow.selene.data.Aws;

public class AwsService extends ServiceAbstract {

    private static class SingletonHolder {
        static final AwsService SINGLETON = new AwsService();
    }

    public static AwsService getInstance() {
        return SingletonHolder.SINGLETON;
    }

    private final AwsDao awsDao;

    protected AwsService() {
        awsDao = AwsDao.getInstance();
    }

    public Aws findOne() {
        List<Aws> all = awsDao.findAll();
        if (all.size() > 1) {
            throw new RuntimeException("ERROR: more than one AWS record found!");
        }
        if (all.size() == 1) {
            return all.get(0);
        } else {
            return null;
        }
    }

    protected AwsDao getAwsDao() {
        return awsDao;
    }
}
