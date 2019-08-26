package moonstone.selene.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import moonstone.acn.client.model.TelemetryItemType;
import moonstone.selene.data.Telemetry;

public class TelemetryDao extends DaoAbstract<Telemetry> {

	private static final class SingletonHolder {
		static final TelemetryDao SINGLETON = new TelemetryDao();
	}

	public static TelemetryDao getInstance() {
		return SingletonHolder.SINGLETON;
	}

	private TelemetryDao() {
	}

	public List<Telemetry> findByDeviceId(long deviceId) {
		return DaoManager.getInstance().execute(connection -> {
			try (PreparedStatement stmt = connection.prepareStatement(getQueries().getProperty("findByDeviceId"))) {
				stmt.setLong(1, deviceId);
				try (ResultSet rset = stmt.executeQuery()) {
					List<Telemetry> result = new ArrayList<>(rset.getFetchSize());
					while (rset.next()) {
						result.add(populate(rset));
					}
					return result;
				}
			}
		});
	}

	public Telemetry findLastTelemetryById(long deviceId) {
		return DaoManager.getInstance().execute(connection -> {
			try (PreparedStatement stmt = connection
					.prepareStatement(getQueries().getProperty("findLastTelByDeviceId"))) {
				stmt.setLong(1, deviceId);
				try (ResultSet rset = stmt.executeQuery()) {
					return rset.next() ? populate(rset) : null;
				}
			}
		});

	}

	public List<Telemetry> findByTimestampBefore(long timestamp) {
		return DaoManager.getInstance().execute(connection -> {
			try (PreparedStatement stmt = connection
					.prepareStatement(getQueries().getProperty("findByTimestampBefore"))) {
				stmt.setLong(1, timestamp);
				try (ResultSet rset = stmt.executeQuery()) {
					List<Telemetry> result = new ArrayList<>(rset.getFetchSize());
					while (rset.next()) {
						result.add(populate(rset));
					}
					return result;
				}
			}
		});
	}

	@Override
	protected int commonStatement(PreparedStatement stmt, Telemetry entity, int idx) throws SQLException {
		if (entity.getBoolValue() != null) {
			stmt.setBoolean(++idx, entity.getBoolValue());
		} else {
			stmt.setNull(++idx, Types.BOOLEAN);
		}
		if (entity.getDateValue() != null) {
			stmt.setString(++idx, entity.getDateValue().toString());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		if (entity.getDatetimeValue() != null) {
			stmt.setString(++idx, entity.getDatetimeValue().toString());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		if (entity.getFloatValue() != null) {
			stmt.setDouble(++idx, entity.getFloatValue());
		} else {
			stmt.setNull(++idx, Types.DOUBLE);
		}
		if (entity.getIntValue() != null) {
			stmt.setLong(++idx, entity.getIntValue());
		} else {
			stmt.setNull(++idx, Types.BIGINT);
		}
		if (entity.getName() != null) {
			stmt.setString(++idx, entity.getName());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		if (entity.getStrValue() != null) {
			stmt.setString(++idx, entity.getStrValue());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		stmt.setLong(++idx, entity.getTimestamp());
		if (entity.getType() != null) {
			stmt.setInt(++idx, entity.getType().ordinal());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		if (entity.getDeviceId() != null) {
			stmt.setLong(++idx, entity.getDeviceId());
		} else {
			stmt.setNull(++idx, Types.BIGINT);
		}
		return idx;
	}

	@Override
	protected Telemetry populate(ResultSet rset) throws SQLException {
		Telemetry entity = new Telemetry();
		entity.setId(rset.getLong("ID"));
		entity.setBoolValue(rset.getBoolean("BOOLVALUE"));
		String dateValue = rset.getString("DATEVALUE");
		if (dateValue != null) {
			entity.setDateValue(LocalDate.parse(dateValue));
		}
		String dateTimeValue = rset.getString("DATETIMEVALUE");
		if (dateTimeValue != null) {
			entity.setDatetimeValue(LocalDateTime.parse(dateTimeValue));
		}
		entity.setFloatValue(rset.getDouble("FLOATVALUE"));
		entity.setIntValue(rset.getLong("INTVALUE"));
		entity.setName(rset.getString("NAME"));
		entity.setStrValue(rset.getString("STRVALUE"));
		entity.setTimestamp(rset.getLong("TIMESTAMP"));
		entity.setType(TelemetryItemType.values()[rset.getInt("TYPE")]);
		entity.setDeviceId(rset.getLong("DEVICEID"));
		return entity;
	}
}
