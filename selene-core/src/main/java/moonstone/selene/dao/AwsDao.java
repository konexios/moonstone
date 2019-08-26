package moonstone.selene.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import moonstone.selene.data.Aws;

public class AwsDao extends DaoAbstract<Aws> {

	private static final class SingletonHolder {
		static final AwsDao SINGLETON = new AwsDao();
	}

	public static AwsDao getInstance() {
		return SingletonHolder.SINGLETON;
	}

	private AwsDao() {
	}

	@Override
	protected int commonStatement(PreparedStatement stmt, Aws entity, int idx) throws SQLException {
		if (entity.getClientCert() != null) {
			stmt.setString(++idx, entity.getClientCert());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		stmt.setLong(++idx, entity.getCreatedTs());
		stmt.setBoolean(++idx, entity.isEnabled());
		if (entity.getHost() != null) {
			stmt.setString(++idx, entity.getHost());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		stmt.setLong(++idx, entity.getModifiedTs());
		stmt.setInt(++idx, entity.getPort());
		if (entity.getPrivateKey() != null) {
			stmt.setString(++idx, entity.getPrivateKey());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		if (entity.getRootCert() != null) {
			stmt.setString(++idx, entity.getRootCert());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		return idx;
	}

	@Override
	protected Aws populate(ResultSet rset) throws SQLException {
		Aws entity = new Aws();
		entity.setId(rset.getLong("ID"));
		entity.setClientCert(rset.getString("CLIENTCERT"));
		entity.setCreatedTs(rset.getLong("CREATEDTS"));
		entity.setEnabled(rset.getBoolean("ENABLED"));
		entity.setHost(rset.getString("HOST"));
		entity.setModifiedTs(rset.getLong("MODIFIEDTS"));
		entity.setPort(rset.getInt("PORT"));
		entity.setPrivateKey(rset.getString("PRIVATEKEY"));
		entity.setRootCert(rset.getString("ROOTCERT"));
		return entity;
	}
}
