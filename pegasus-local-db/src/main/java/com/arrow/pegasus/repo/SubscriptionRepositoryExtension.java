package com.arrow.pegasus.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.repo.params.SubscriptionSearchParams;

public interface SubscriptionRepositoryExtension extends RepositoryExtension<Subscription> {
	
	public Page<Subscription> findSubscriptions(Pageable pageable, SubscriptionSearchParams params);

	public List<Subscription> findSubscriptions(SubscriptionSearchParams params);

	public long findSubscriptionCount(SubscriptionSearchParams params);
}
