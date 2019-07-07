package com.arrow.pegasus.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.data.profile.Zone;

@Repository
public interface ZoneRepository extends MongoRepository<Zone, String>, ZoneRepositoryExtension {
	Zone findByName(String name);

	Zone findBySystemName(String systemName);

	List<Zone> findByRegionId(String regionId);

	List<Zone> findByEnabled(boolean enabled);

	List<Zone> findByRegionIdAndEnabled(String regionId, boolean enabled);

	Long deleteByRegionId(String regionId);
}
