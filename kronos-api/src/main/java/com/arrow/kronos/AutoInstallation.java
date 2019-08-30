package com.arrow.kronos;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.arrow.kronos.service.KronosApplicationProvisioningService;
import com.arrow.kronos.service.KronosApplicationService;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.PlatformConfig;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.service.CoreCacheService;
import com.arrow.pegasus.service.PlatformConfigService;
import com.fasterxml.jackson.core.type.TypeReference;

import moonstone.acs.AcsUtils;
import moonstone.acs.JsonUtils;
import moonstone.acs.Loggable;

@Component
public class AutoInstallation extends Loggable implements CommandLineRunner {
	@Autowired
	private ApplicationContext context;
	@Autowired
	private PlatformConfigService platformConfigService;
	@Autowired
	private KronosApplicationService kronosApplicationService;
	@Autowired
	private CoreCacheService coreCacheService;
	@Autowired
	private KronosApplicationProvisioningService provService;

	@Override
	public void run(String... args) throws Exception {
		String method = "run";
		try {
			checkCreatePlatformConfig();
			provisionDefaultApplication();
		} catch (Exception e) {
			logError(method, e);
			throw e;
		}
	}

	void checkCreatePlatformConfig() throws Exception {
		String method = "checkCreatePlatformConfig";
		long cfgCount = platformConfigService.getPlatformConfigRepository().count();
		if (cfgCount == 0) {
			List<PlatformConfig> cfgs = JsonUtils.fromJson(AcsUtils.streamToString(
					context.getResource("classpath:install/platform-config.json").getInputStream(),
					StandardCharsets.UTF_8), new TypeReference<List<PlatformConfig>>() {
					});
			logInfo(method, "cfgs: %d", cfgs.size());
			for (PlatformConfig cfg : cfgs) {
				cfg = platformConfigService.getPlatformConfigRepository().doInsert(cfg, CoreConstant.ADMIN_USER);
				logInfo(method, "created platformConfig ==> systemName: %s, id: %s", cfg.getZoneSystemName(),
						cfg.getId());
			}
		} else {
			logInfo(method, "cfgCount: %d", cfgCount);
		}
	}

	void provisionDefaultApplication() throws Exception {
		String method = "activateDefaultApplication";
		long appCount = kronosApplicationService.getKronosApplicationRepository().count();
		if (appCount == 0) {
			Application kronos = coreCacheService.findApplicationByName("default-kronos");
			if (kronos != null) {
				provService.provisionApplication(kronos.getId(), true, CoreConstant.ADMIN_USER);
				logInfo(method, "provisioned default application!");
			}
		} else {
			logInfo(method, "appCount: %d", appCount);
		}
	}
}
