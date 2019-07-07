package com.arrow.pegasus.data;

import java.io.Serializable;

public class AccessPrivilege implements Serializable {
    private static final long serialVersionUID = -4603340303355232568L;

    public enum AccessLevel {
        READ, WRITE, OWNER
    }

    private String pri;
    private AccessLevel level;

    public AccessPrivilege() {
    }

    public AccessPrivilege(String pri, AccessLevel level) {
        super();
        this.pri = pri;
        this.level = level;
    }

    public String getPri() {
        return pri;
    }

    public void setPri(String pri) {
        this.pri = pri;
    }

    public AccessLevel getLevel() {
        return level;
    }

    public void setLevel(AccessLevel level) {
        this.level = level;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((level == null) ? 0 : level.hashCode());
        result = prime * result + ((pri == null) ? 0 : pri.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AccessPrivilege other = (AccessPrivilege) obj;
        if (level != other.level)
            return false;
        if (pri == null) {
            if (other.pri != null)
                return false;
        } else if (!pri.equals(other.pri))
            return false;
        return true;
    }
}
