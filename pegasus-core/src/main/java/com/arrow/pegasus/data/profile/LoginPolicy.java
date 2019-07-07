package com.arrow.pegasus.data.profile;

import java.io.Serializable;

public class LoginPolicy implements Serializable {
    private static final long serialVersionUID = 5249540968674733320L;

    private final static int DEFAULT_MAX_FAILED_LOGINS = 5;
    private final static int DEFAULT_LOCK_TIMEOUT_SECS = 30 * 60;

    private int maxFailedLogins = DEFAULT_MAX_FAILED_LOGINS;
    private int lockTimeoutSecs = DEFAULT_LOCK_TIMEOUT_SECS;

    public int getMaxFailedLogins() {
        return maxFailedLogins;
    }

    public void setMaxFailedLogins(int maxFailedLogins) {
        this.maxFailedLogins = maxFailedLogins;
    }

    public int getLockTimeoutSecs() {
        return lockTimeoutSecs;
    }

    public void setLockTimeoutSecs(int lockTimeoutSecs) {
        this.lockTimeoutSecs = lockTimeoutSecs;
    }
}
