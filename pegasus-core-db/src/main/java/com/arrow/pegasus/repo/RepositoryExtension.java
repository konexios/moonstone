package com.arrow.pegasus.repo;

import java.util.Collection;

import com.arrow.pegasus.data.DocumentAbstract;

public interface RepositoryExtension<T extends DocumentAbstract> {
    public T doFindByHid(String hid);

    public T doInsert(T document, String who);

    public void doInsert(Collection<T> documents, String who);

    public T doSave(T document, String who);
}
