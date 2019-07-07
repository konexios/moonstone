package com.arrow.rhea.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.pegasus.repo.RepositoryExtension;
import com.arrow.rhea.data.RTURequest;

public interface RTURequestRepositoryExtension extends RepositoryExtension<RTURequest> {
	Page<RTURequest> findRTURequests(Pageable pageable, RTURequestSearchParams params);
	
	List<RTURequest> findRTURequests(RTURequestSearchParams params);
}
