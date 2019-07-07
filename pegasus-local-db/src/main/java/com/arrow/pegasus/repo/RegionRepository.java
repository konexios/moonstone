package com.arrow.pegasus.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.data.profile.Region;

@Repository
public interface RegionRepository extends MongoRepository<Region, String>, RegionRepositoryExtension {
	public Region findByName(String name);

	public List<Region> findByEnabled(boolean enabled);
}
