package com.arrow.pegasus.dashboard.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.pegasus.dashboard.data.WidgetType;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface WidgetTypeRepositoryExtension extends RepositoryExtension<WidgetType> {

	public Page<WidgetType> findWidgetTypes(Pageable pageable, WidgetTypeSearchParams params);

	public List<WidgetType> findWidgetTypes(WidgetTypeSearchParams params);
}
