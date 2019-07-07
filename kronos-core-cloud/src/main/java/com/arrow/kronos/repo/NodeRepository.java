package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.Node;

@Repository
public interface NodeRepository extends MongoRepository<Node, String>, NodeRepositoryExtension {

	Node findByName(String name);

	List<Node> findByApplicationId(String applicationId);

	List<Node> findByEnabled(boolean enabled);

	List<Node> findByApplicationIdAndEnabled(String applicationId, boolean enabled);

	List<Node> findByApplicationIdAndParentNodeId(String applicationId, String parentNodeId);

	Long deleteByApplicationId(String applicationId);
}
