package com.arrow.selene.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.arrow.selene.data.Device;

public class DeviceDao extends DaoAbstract<Device> {

	private static final class SingletonHolder {
		static final DeviceDao SINGLETON = new DeviceDao();
	}

	public static DeviceDao getInstance() {
		return SingletonHolder.SINGLETON;
	}

	private DeviceDao() {
	}

	public Device findByTypeAndUid(String type, String uid) {
		return DaoManager.getInstance().execute(connection -> {
			try (PreparedStatement stmt = connection.prepareStatement(getQueries().getProperty("findByTypeAndUid"))) {
				stmt.setString(1, type);
				stmt.setString(2, uid);
				try (ResultSet rset = stmt.executeQuery()) {
					return rset.next() ? populate(rset) : null;
				}
			}
		});
	}

	public Device findByUid(String uid) {
		return DaoManager.getInstance().execute(connection -> {
			PreparedStatement findByUid = connection.prepareStatement(getQueries().getProperty("findByUid"));
			findByUid.setString(1, uid);
			try (PreparedStatement stmt = findByUid; ResultSet rset = stmt.executeQuery()) {
				return rset.next() ? populate(rset) : null;
			}
		});
	}

	public List<Device> findByType(String type) {
		return DaoManager.getInstance().execute(connection -> {
			try (PreparedStatement stmt = connection.prepareStatement(getQueries().getProperty("findByType"))) {
				stmt.setString(1, type);
				try (ResultSet rset = stmt.executeQuery()) {
					List<Device> result = new ArrayList<>(rset.getFetchSize());
					while (rset.next()) {
						result.add(populate(rset));
					}
					return result;
				}
			}
		});
	}

	@Override
	protected int commonStatement(PreparedStatement stmt, Device entity, int idx) throws SQLException {
		stmt.setLong(++idx, entity.getCreatedTs());
		stmt.setBoolean(++idx, entity.isEnabled());
		if (entity.getExternalId() != null) {
			stmt.setString(++idx, entity.getExternalId());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		if (entity.getHid() != null) {
			stmt.setString(++idx, entity.getHid());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		if (entity.getInfo() != null) {
			stmt.setString(++idx, entity.getInfo());
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
		if (entity.getStates() != null) {
			stmt.setString(++idx, entity.getStates());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		if (entity.getType() != null) {
			stmt.setString(++idx, entity.getType());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		if (entity.getUid() != null) {
			stmt.setString(++idx, entity.getUid());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		if (entity.getUserHid() != null) {
			stmt.setString(++idx, entity.getUserHid());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		if (entity.getGatewayId() != null) {
			stmt.setLong(++idx, entity.getGatewayId());
		} else {
			stmt.setNull(++idx, Types.BIGINT);
		}
		if (entity.getStatus() != null) {
			stmt.setString(++idx, entity.getStatus());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		return idx;
	}

	@Override
	protected Device populate(ResultSet rset) throws SQLException {
		Device entity = new Device();
		entity.setId(rset.getLong("ID"));
		entity.setCreatedTs(rset.getLong("CREATEDTS"));
		entity.setEnabled(rset.getBoolean("ENABLED"));
		entity.setExternalId(rset.getString("EXTERNALID"));
		entity.setHid(rset.getString("HID"));
		entity.setInfo(rset.getString("INFO"));
		entity.setModifiedTs(rset.getLong("MODIFIEDTS"));
		entity.setName(rset.getString("NAME"));
		entity.setProperties(rset.getString("PROPERTIES"));
		entity.setStates(rset.getString("STATES"));
		entity.setType(rset.getString("TYPE"));
		entity.setUid(rset.getString("UID"));
		entity.setUserHid(rset.getString("USERHID"));
		entity.setGatewayId(rset.getLong("GATEWAYID"));
		entity.setStatus(rset.getString("STATUS"));
		return entity;
	}
}
