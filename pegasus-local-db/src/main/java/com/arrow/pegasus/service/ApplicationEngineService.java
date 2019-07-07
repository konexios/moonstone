package com.arrow.pegasus.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arrow.acs.AcsLogicalException;
import com.arrow.pegasus.data.ApplicationEngine;
import com.arrow.pegasus.repo.ApplicationEngineRepository;

@Service
public class ApplicationEngineService extends BaseServiceAbstract {

    @Autowired
    private ApplicationEngineRepository applicationEngineRepository;

    public ApplicationEngineRepository getApplicationEngineRepository() {
        return applicationEngineRepository;
    }

    public ApplicationEngine create(ApplicationEngine applicationEngine, String who) {
        String method = "create";

        if (applicationEngine == null) {
            logInfo(method, "applicationEngine is null");
            throw new AcsLogicalException("applicationEngine is null");
        }
        if (StringUtils.isEmpty(who)) {
            logInfo(method, "who is empty");
            throw new AcsLogicalException("who is empty");
        }

        applicationEngine = applicationEngineRepository.doInsert(applicationEngine, who);

        return applicationEngine;
    }
}
