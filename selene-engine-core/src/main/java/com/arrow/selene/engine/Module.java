package com.arrow.selene.engine;

public interface Module {
    String getName();

    ModuleState getState();

    void setState(ModuleState state);

    void setErrorState(String errorStateMessage);

    String getErrorStateMessage();

    void start();

    void stop();
}
