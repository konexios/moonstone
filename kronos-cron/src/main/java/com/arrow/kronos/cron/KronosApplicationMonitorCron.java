package com.arrow.kronos.cron;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.arrow.kronos.service.KronosApplicationProvisioningService;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.cron.CronAbstract;
import com.arrow.pegasus.service.PlatformConfigService;

@Component
public class KronosApplicationMonitorCron extends CronAbstract {

	@Autowired
	private KronosApplicationProvisioningService service;
	@Autowired
	private PlatformConfigService platformConfigService;

	@Override
	@Scheduled(cron = "${KronosApplicationMonitorCron.cron}")
	public void start() {
		super.start();
	}
	
	@Override
	protected void run() {
		String method = "run";
		try {
			startCron();
			String zone = platformConfigService.getConfig().getZoneSystemName();
			addLog(method, String.format("processing zone: %s", zone));
			int result = service.monitorNewApplications(this, zone, CoreConstant.CRON_USER);
			addLog(method, String.format("result: %d", result));
			setSendEmail(result > 0);
		} catch (Throwable t) {
			addException(method, t);
		} finally {
			endCron();
		}
	}
}
