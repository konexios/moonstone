package com.arrow.pegasus.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.arrow.acs.AcsRuntimeException;
import com.arrow.acs.AcsSystemException;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.LifeCycleAbstract;

public abstract class ProcessorAbstract extends LifeCycleAbstract implements Processor {

    private String name;
    private ExecutorService service;

    @Override
    public void start() {
        String method = "ProcessorAbstract.start";
        logInfo(method, "starting ...");
        if (getServiceNumThreads() > 0) {
            logInfo(method, "creating thread pool, size: %d", getServiceNumThreads());
            service = Executors.newFixedThreadPool(getServiceNumThreads());
        }
        logInfo(method, "started");
    }

    @Override
    protected void preDestroy() {
        String method = "ProcessorAbstract.stop";
        super.preDestroy();
        logInfo(method, "stopping ...");
        if (service != null) {
            logInfo(method, "stopping service ...");
            service.shutdown();
            try {
                service.awaitTermination(CoreConstant.DEFAULT_SHUTDOWN_WAIT_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                service.shutdownNow();
            }
            service = null;
        }
        logInfo(method, "stopping complete");
    }

    @Override
    public String getName() {
        return name == null ? getClass().getSimpleName() : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected ExecutorService getService() {
        return service;
    }

    protected AcsRuntimeException handleException(Exception e) {
        String method = "handleException";
        logError(method, e);
        if (e.getClass().isAssignableFrom(AcsRuntimeException.class)) {
            return (AcsRuntimeException) e;
        } else {
            return new AcsSystemException("Exception encountered: " + e.getClass().getName(), e);
        }
    }

    protected abstract int getServiceNumThreads();
}
