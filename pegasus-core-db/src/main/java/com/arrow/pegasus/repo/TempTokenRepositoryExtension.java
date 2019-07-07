package com.arrow.pegasus.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.pegasus.data.TempToken;
import com.arrow.pegasus.repo.params.TempTokenSearchParams;

public interface TempTokenRepositoryExtension extends RepositoryExtension<TempToken> {

	public TempToken findByHid(String hid);

	public Page<TempToken> findBy(Pageable pageable, TempTokenSearchParams params);

	public List<TempToken> findBy(TempTokenSearchParams params);
}
