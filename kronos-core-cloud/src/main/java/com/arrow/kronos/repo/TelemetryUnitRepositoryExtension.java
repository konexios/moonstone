package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.arrow.kronos.data.TelemetryUnit;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface TelemetryUnitRepositoryExtension extends RepositoryExtension<TelemetryUnit> {
    Page<TelemetryUnit> findTelemetryUnits(Pageable pageable, KronosDocumentSearchParams params);
    Page<TelemetryUnit> findTelemetryUnits(Pageable pageable, TelemetryUnitSearchParams params);
    List<TelemetryUnit> findTelemetryUnits(TelemetryUnitSearchParams params);
    List<TelemetryUnit> findTelemetryUnits(TelemetryUnitSearchParams params, Sort sort);
}
