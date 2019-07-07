package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.NodeType;

@Repository
public interface NodeTypeRepository extends MongoRepository<NodeType, String>, NodeTypeRepositoryExtension {
	NodeType findByApplicationIdAndName(String applicationId, String name);

	List<NodeType> findByApplicationId(String applicationId);

	List<NodeType> findByEnabled(boolean enabled);

	List<NodeType> findByApplicationIdAndEnabled(String applicationId, boolean enabled);

	Long deleteByApplicationId(String applicationId);
}
