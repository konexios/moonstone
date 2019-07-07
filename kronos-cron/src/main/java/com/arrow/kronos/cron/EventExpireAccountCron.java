package com.arrow.kronos.cron;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.arrow.kronos.service.DeveloperService;
import com.arrow.pegasus.cron.CronAbstract;

@Component
public class EventExpireAccountCron extends CronAbstract {

	@Autowired
	private DeveloperService developerService;

	@Override
	@Scheduled(cron = "${EventExpireAccountCron.cron}")
	public void start() {
		super.start();
	}

	@Override
	protected void run() {
		String method = "run";
		try {
			startCron();
			developerService.expireUnverifiedAccount();
		} catch (Throwable t) {
			addException(method, t);
		} finally {
			endCron();
		}
	}
}
