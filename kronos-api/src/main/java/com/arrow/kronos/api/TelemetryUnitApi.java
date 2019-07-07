package com.arrow.kronos.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acn.client.model.TelemetryUnitModel;
import com.arrow.acs.client.model.ListResultModel;
import com.arrow.kronos.data.TelemetryUnit;
import com.arrow.kronos.repo.TelemetryUnitSearchParams;
import com.arrow.kronos.service.TelemetryUnitService;
import com.arrow.pegasus.ProductSystemNames;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/v1/kronos/telemetries/units")
public class TelemetryUnitApi extends BaseApiAbstract {

	@Autowired
	private TelemetryUnitService telemetryUnitService;

	@ApiOperation(value = "find telemetry units")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ListResultModel<TelemetryUnitModel> findAllBy(
	        @ApiParam(value = "system name", required = false) @RequestParam(name = "systemName", required = false) String systemName,
	        @ApiParam(value = "name", required = false) @RequestParam(name = "name", required = false) String name,
	        @ApiParam(value = "enabled", required = false) @RequestParam(name = "enabled", required = false) String enabled,
	        @ApiParam(value = "sort field", required = false) @RequestParam(name = "sortField", required = false) String sortField,
	        @ApiParam(value = "sort direction", required = false) @RequestParam(name = "sortDirection", required = false) String sortDirection) {

		getValidatedAccessKey(ProductSystemNames.KRONOS);

		TelemetryUnitSearchParams params = new TelemetryUnitSearchParams();
		if (StringUtils.hasText(systemName)) {
			params.setSystemName(systemName);
		}
		if (StringUtils.hasText(name)) {
			params.setName(name);
		}
		if (StringUtils.hasText(enabled)) {
			params.setEnabled(Boolean.valueOf(enabled));
		}
		Sort sort = null;
		if (StringUtils.hasText(sortField)) {
			Direction direction = sortDirection != null ? Direction.valueOf(sortDirection) : Direction.ASC;
			sort = new Sort(direction, sortField);
		}

		List<TelemetryUnitModel> data = telemetryUnitService.getTelemetryUnitRepository()
		        .findTelemetryUnits(params, sort).stream().map(this::buildTelemetryUnitModel)
		        .collect(Collectors.toCollection(ArrayList::new));
		return new ListResultModel<TelemetryUnitModel>().withData(data).withSize(data.size());
	}

	private TelemetryUnitModel buildTelemetryUnitModel(TelemetryUnit telemetryUnit) {
		if (telemetryUnit == null) {
			return null;
		}
		TelemetryUnitModel result = buildModel(new TelemetryUnitModel(), telemetryUnit);
		result.setSystemName(telemetryUnit.getSystemName());
		return result;
	}
}
