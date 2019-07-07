package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.kronos.data.SocialEventDevice;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface SocialEventDeviceRepositoryExtension extends RepositoryExtension<SocialEventDevice> {

	public Page<SocialEventDevice> findSocialEventDevices(Pageable pageable,
			SocialEventDeviceSearchParams params);

	public List<SocialEventDevice> findSocialEventDevices(SocialEventDeviceSearchParams params);
}
