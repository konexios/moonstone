package com.arrow.pegasus.dashboard.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.dashboard.data.Container;

@Repository
public interface ContainerRepository extends ContainerRepositoryExtension, MongoRepository<Container, String> {

    // public Container findByNameAndUserId(String name, String userId);

    public Container findByName(String name);

    // public List<Container> findByUserId(String userId);
    public List<Container> findByBoardId(String Id);

    public Container findByDescription(String description);
}