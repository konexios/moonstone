package com.arrow.pegasus.dashboard.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.pegasus.dashboard.data.Widget;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface WidgetRepositoryExtension extends RepositoryExtension<Widget> {

	public Page<Widget> findWidgets(Pageable pageable, WidgetSearchParams params);

	public List<Widget> findWidgets(WidgetSearchParams params);
}
