package com.arrow.pegasus;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.arrow.pegasus.data.Enabled;

import moonstone.acs.AcsLogicalException;
import moonstone.acs.Loggable;

public abstract class LifeCycleAbstract extends Loggable {

    private boolean terminating = false;

    @PostConstruct
    protected void postConstruct() {
    }

    @PreDestroy
    protected void preDestroy() {
        setTerminating(true);
    }

    protected boolean isTerminating() {
        return terminating;
    }

    protected void setTerminating(boolean terminating) {
        this.terminating = terminating;
    }

    protected void checkEnabled(Enabled resource, String name) {
        if (resource == null) {
            throw new AcsLogicalException(name + " is not found");
        }
        if (!resource.isEnabled()) {
            throw new AcsLogicalException(name + " is disabled");
        }
    }
}
