package moonstone.selene.device.cellular;

import java.util.ArrayList;
import java.util.List;

import moonstone.acn.client.IotParameters;
import moonstone.acn.client.model.TelemetryItemType;
import moonstone.selene.data.Telemetry;
import moonstone.selene.engine.DeviceDataAbstract;

public class CellularModemDataImpl extends DeviceDataAbstract implements CellularModemData {

	private String modemPath;
	private String operatorCode;
	private String operatorName;
	private Long registrationState;
	private Long signalQuality;
	private Boolean signalValid;
	private Long state;
	private String error;
	private String locationMcc;
	private String locationMnc;
	private String locationLac;
	private String locationCi;

	@Override
	public IotParameters writeIoTParameters() {
		IotParameters result = new IotParameters();
		if (modemPath != null)
			result.setString("modemPath", modemPath);
		if (operatorCode != null)
			result.setString("operatorCode", operatorCode);
		if (operatorName != null)
			result.setString("operatorName", operatorName);
		if (registrationState != null)
			result.setLong("registrationState", registrationState);
		if (signalQuality != null)
			result.setLong("signalQuality", signalQuality);
		if (signalValid != null)
			result.setBoolean("signalValid", signalValid);
		if (state != null)
			result.setLong("state", state);
		if (error != null)
			result.setString("error", error);
		if (locationMcc != null)
			result.setString("locationMcc", locationMcc);
		if (locationMnc != null)
			result.setString("locationMnc", locationMnc);
		if (locationLac != null)
			result.setString("locationLac", locationLac);
		if (locationCi != null)
			result.setString("locationCi", locationCi);

		return result;
	}

	@Override
	public List<Telemetry> writeTelemetries() {
		List<Telemetry> result = new ArrayList<>();
		if (modemPath != null)
			result.add(writeStringTelemetry(TelemetryItemType.String, "modemPath", modemPath));
		if (operatorCode != null)
			result.add(writeStringTelemetry(TelemetryItemType.String, "operatorCode", operatorCode));
		if (operatorName != null)
			result.add(writeStringTelemetry(TelemetryItemType.String, "operatorName", operatorName));
		if (registrationState != null)
			result.add(writeIntTelemetry("registrationState", registrationState));
		if (signalQuality != null)
			result.add(writeIntTelemetry("signalQuality", signalQuality));
		if (signalValid != null)
			result.add(writeBooleanTelemetry("signalValid", signalValid));
		if (state != null)
			result.add(writeIntTelemetry("state", state));
		if (error != null)
			result.add(writeStringTelemetry(TelemetryItemType.String, "error", error));
		if (locationMcc != null)
			result.add(writeStringTelemetry(TelemetryItemType.String, "locationMcc", locationMcc));
		if (locationMnc != null)
			result.add(writeStringTelemetry(TelemetryItemType.String, "locationMnc", locationMnc));
		if (locationLac != null)
			result.add(writeStringTelemetry(TelemetryItemType.String, "locationLac", locationLac));
		if (locationCi != null)
			result.add(writeStringTelemetry(TelemetryItemType.String, "locationCi", locationCi));
		return result;
	}

	public String getModemPath() {
		return modemPath;
	}

	public void setModemPath(String modemPath) {
		this.modemPath = modemPath;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Long getRegistrationState() {
		return registrationState;
	}

	public void setRegistrationState(Long registrationState) {
		this.registrationState = registrationState;
	}

	public Long getSignalQuality() {
		return signalQuality;
	}

	public void setSignalQuality(Long signalQuality) {
		this.signalQuality = signalQuality;
	}

	public Boolean getSignalValid() {
		return signalValid;
	}

	public void setSignalValid(Boolean signalValid) {
		this.signalValid = signalValid;
	}

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getLocationMcc() {
		return locationMcc;
	}

	public void setLocationMcc(String locationMcc) {
		this.locationMcc = locationMcc;
	}

	public String getLocationMnc() {
		return locationMnc;
	}

	public void setLocationMnc(String locationMnc) {
		this.locationMnc = locationMnc;
	}

	public String getLocationLac() {
		return locationLac;
	}

	public void setLocationLac(String locationLac) {
		this.locationLac = locationLac;
	}

	public String getLocationCi() {
		return locationCi;
	}

	public void setLocationCi(String locationCi) {
		this.locationCi = locationCi;
	}
}
