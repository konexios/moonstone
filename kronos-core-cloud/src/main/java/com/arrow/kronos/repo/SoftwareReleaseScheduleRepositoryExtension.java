package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.kronos.data.SoftwareReleaseSchedule;
import com.arrow.pegasus.repo.DistinctCountResult;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface SoftwareReleaseScheduleRepositoryExtension extends RepositoryExtension<SoftwareReleaseSchedule> {

    public Page<SoftwareReleaseSchedule> findSoftwareReleaseSchedules(Pageable pageable,
            SoftwareReleaseScheduleSearchParams params);

    public List<SoftwareReleaseSchedule> findSoftwareReleaseSchedules(SoftwareReleaseScheduleSearchParams params);

    public long findSoftwareReleaseScheduleCount(SoftwareReleaseScheduleSearchParams params);

    public Page<SoftwareReleaseSchedule> findSoftwareReleaseSchedules(Pageable pageable,
            SoftwareReleaseScheduleStatusSearchParams params);

    public List<SoftwareReleaseSchedule> findSoftwareReleaseSchedulesByObjectIds(String... objectIds);

    public List<DistinctCountResult> countDistinctCreatedBy(SoftwareReleaseScheduleSearchParams params);

    public List<DistinctCountResult> countDistinctDeviceTypeId(SoftwareReleaseScheduleSearchParams params);

    public List<DistinctCountResult> countDistinctStatus(SoftwareReleaseScheduleSearchParams params);
}
