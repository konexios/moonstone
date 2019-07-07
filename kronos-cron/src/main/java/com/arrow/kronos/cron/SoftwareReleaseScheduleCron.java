package com.arrow.kronos.cron;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
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
public class SoftwareReleaseScheduleCron extends CronAbstract {

	@Autowired
	private SoftwareReleaseScheduleService softwareReleaseScheduleService;

	@Override
	@Scheduled(cron = "${SoftwareReleaseScheduleCron.cron}")
	public void start() {
		super.start();
	}

	@Override
	protected void run() {
		String method = "run";
		try {
			startCron();
			ZonedDateTime datetime = ZonedDateTime.now().truncatedTo(ChronoUnit.HOURS);
			addLog(method, "datetime: " + datetime);
			Instant scheduledDate = datetime.toInstant();
			List<SoftwareReleaseSchedule> schedules = findSoftwareReleaseSchedules(scheduledDate);
			for (SoftwareReleaseSchedule softwareReleaseSchedule : schedules) {
				try {
					softwareReleaseScheduleService.checkAndStart(softwareReleaseSchedule, CoreConstant.ADMIN_USER);
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

	private List<SoftwareReleaseSchedule> findSoftwareReleaseSchedules(Instant scheduledDate) {
		SoftwareReleaseScheduleSearchParams params = new SoftwareReleaseScheduleSearchParams();
		params.setStatuses(EnumSet.of(Status.SCHEDULED));
		// "$gte" scheduledDate "$lt" scheduledDate + hour
		// params.setFromScheduledDate(scheduledDate);
		params.setToScheduledDate(scheduledDate.plus(1, ChronoUnit.HOURS));
		params.setOnDemand(false);
		return softwareReleaseScheduleService.getSoftwareReleaseScheduleRepository()
				.findSoftwareReleaseSchedules(params);
	}
}
