package com.arrow.apollo.repo;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.arrow.apollo.data.ApolloWidget;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface ApolloWidgetRepositoryExtension extends RepositoryExtension<ApolloWidget> {

	public long countApolloWidgets(ApolloWidgetSearchParams params);

	public List<ApolloWidget> findApolloWidgets(ApolloWidgetSearchParams params, Sort sort);
}