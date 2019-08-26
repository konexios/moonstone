package moonstone.selene.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import moonstone.selene.data.Ibm;

public class IbmDao extends DaoAbstract<Ibm> {

	private static final class SingletonHolder {
		static final IbmDao SINGLETON = new IbmDao();
	}

	public static IbmDao getInstance() {
		return SingletonHolder.SINGLETON;
	}

	private IbmDao() {
	}

	@Override
	protected int commonStatement(PreparedStatement stmt, Ibm entity, int idx) throws SQLException {
		if (entity.getAuthMethod() != null) {
			stmt.setString(++idx, entity.getAuthMethod());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		if (entity.getAuthToken() != null) {
			stmt.setString(++idx, entity.getAuthToken());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		stmt.setLong(++idx, entity.getCreatedTs());
		stmt.setBoolean(++idx, entity.isEnabled());
		if (entity.getGatewayId() != null) {
			stmt.setString(++idx, entity.getGatewayId());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		if (entity.getGatewayType() != null) {
			stmt.setString(++idx, entity.getGatewayType());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		stmt.setLong(++idx, entity.getModifiedTs());
		if (entity.getOrganizationId() != null) {
			stmt.setString(++idx, entity.getOrganizationId());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		return idx;
	}

	@Override
	protected Ibm populate(ResultSet rset) throws SQLException {
		Ibm entity = new Ibm();
		entity.setId(rset.getLong("ID"));
		entity.setAuthMethod(rset.getString("AUTHMETHOD"));
		entity.setAuthToken(rset.getString("AUTHTOKEN"));
		entity.setCreatedTs(rset.getLong("CREATEDTS"));
		entity.setEnabled(rset.getBoolean("ENABLED"));
		entity.setGatewayId(rset.getString("GATEWAYID"));
		entity.setGatewayType(rset.getString("GATEWAYTYPE"));
		entity.setModifiedTs(rset.getLong("MODIFIEDTS"));
		entity.setOrganizationId(rset.getString("ORGANIZATIONID"));
		return entity;
	}
}
