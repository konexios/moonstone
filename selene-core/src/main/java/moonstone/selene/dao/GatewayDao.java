package moonstone.selene.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import moonstone.acn.client.model.CloudPlatform;
import moonstone.selene.data.Gateway;

public class GatewayDao extends DaoAbstract<Gateway> {

	private static final class SingletonHolder {
		static final GatewayDao SINGLETON = new GatewayDao();
	}

	public static GatewayDao getInstance() {
		return SingletonHolder.SINGLETON;
	}

	private GatewayDao() {
	}

	@Override
	protected int commonStatement(PreparedStatement stmt, Gateway entity, int idx) throws SQLException {
		if (entity.getApiKey() != null) {
			stmt.setString(++idx, entity.getApiKey());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		stmt.setInt(++idx, entity.getCloudPlatform().ordinal());
		stmt.setLong(++idx, entity.getCreatedTs());
		stmt.setBoolean(++idx, entity.isEnabled());
		if (entity.getExternalId() != null) {
			stmt.setString(++idx, entity.getExternalId());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		stmt.setLong(++idx, entity.getHeartBeatIntervalMs());
		if (entity.getHid() != null) {
			stmt.setString(++idx, entity.getHid());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		if (entity.getIotConnectUrl() != null) {
			stmt.setString(++idx, entity.getIotConnectUrl());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		stmt.setLong(++idx, entity.getModifiedTs());
		if (entity.getName() != null) {
			stmt.setString(++idx, entity.getName());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		if (entity.getProperties() != null) {
			stmt.setString(++idx, entity.getProperties());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		stmt.setInt(++idx, entity.getPurgeMessagesIntervalDays());
		stmt.setInt(++idx, entity.getPurgeTelemetryIntervalDays());
		if (entity.getSecretKey() != null) {
			stmt.setString(++idx, entity.getSecretKey());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		if (entity.getTopology() != null) {
			stmt.setString(++idx, entity.getTopology());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		if (entity.getUid() != null) {
			stmt.setString(++idx, entity.getUid());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		return idx;
	}

	@Override
	protected Gateway populate(ResultSet rset) throws SQLException {
		Gateway entity = new Gateway();
		entity.setId(rset.getLong("ID"));
		entity.setApiKey(rset.getString("APIKEY"));
		entity.setCloudPlatform(CloudPlatform.values()[rset.getInt("CLOUDPLATFORM")]);
		entity.setCreatedTs(rset.getLong("CREATEDTS"));
		entity.setEnabled(rset.getBoolean("ENABLED"));
		entity.setExternalId(rset.getString("EXTERNALID"));
		entity.setHeartBeatIntervalMs(rset.getLong("HEARTBEATINTERVALMS"));
		entity.setHid(rset.getString("HID"));
		entity.setIotConnectUrl(rset.getString("IOTCONNECTURL"));
		entity.setModifiedTs(rset.getLong("MODIFIEDTS"));
		entity.setName(rset.getString("NAME"));
		entity.setProperties(rset.getString("PROPERTIES"));
		entity.setPurgeMessagesIntervalDays(rset.getInt("PURGEMESSAGESINTERVALDAYS"));
		entity.setPurgeTelemetryIntervalDays(rset.getInt("PURGETELEMETRYINTERVALDAYS"));
		entity.setSecretKey(rset.getString("SECRETKEY"));
		entity.setTopology(rset.getString("TOPOLOGY"));
		entity.setUid(rset.getString("UID"));
		return entity;
	}
}
