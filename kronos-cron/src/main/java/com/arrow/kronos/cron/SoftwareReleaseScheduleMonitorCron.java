package com.arrow.kronos.cron;

import java.time.ZonedDateTime;
import java.util.EnumSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.arrow.kronos.data.SoftwareReleaseSchedule;
import com.arrow.kronos.data.SoftwareReleaseSchedule.Status;
import com.arrow.kronos.repo.SoftwareReleaseScheduleSearchParams;
import com.arrow.kronos.service.SoftwareReleaseScheduleService;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.cron.CronAbstract;

@Component
public class SoftwareReleaseScheduleMonitorCron extends CronAbstract {

	@Autowired
	private SoftwareReleaseScheduleService softwareReleaseScheduleService;

	@Override
	@Scheduled(cron = "${SoftwareReleaseScheduleMonitorCron.cron}")
	public void start() {
		super.start();
	}

	@Override
	protected void run() {
		String method = "run";
		try {
			startCron();
			ZonedDateTime datetime = ZonedDateTime.now();
			List<SoftwareReleaseSchedule> schedules = findSoftwareReleaseSchedules();
			addLog(method, "datetime: " + datetime + ", softwareReleaseSchedules: " + schedules.size());
			for (SoftwareReleaseSchedule softwareReleaseSchedule : schedules) {
				try {
					softwareReleaseScheduleService.checkAndComplete(softwareReleaseSchedule, CoreConstant.ADMIN_USER);
				} catch (Exception e) {
					addException(method, e);
				}
			}
		} catch (Throwable t) {
			addException(method, t);
		} finally {
			endCron();
		}
	}

	private List<SoftwareReleaseSchedule> findSoftwareReleaseSchedules() {
		SoftwareReleaseScheduleSearchParams params = new SoftwareReleaseScheduleSearchParams();
		params.setStatuses(EnumSet.of(Status.INPROGRESS));
		return softwareReleaseScheduleService.getSoftwareReleaseScheduleRepository()
				.findSoftwareReleaseSchedules(params);
	}
}
