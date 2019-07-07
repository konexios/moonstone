package com.arrow.apollo.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.apollo.data.ApolloWidget;

@Repository
public interface ApolloWidgetRepository
        extends MongoRepository<ApolloWidget, String>, ApolloWidgetRepositoryExtension {
}