package com.arrow.selene.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.arrow.selene.data.Azure;

public class AzureDao extends DaoAbstract<Azure> {

	private static final class SingletonHolder {
		static final AzureDao SINGLETON = new AzureDao();
	}

	public static AzureDao getInstance() {
		return SingletonHolder.SINGLETON;
	}

	private AzureDao() {
	}

	@Override
	protected int commonStatement(PreparedStatement stmt, Azure entity, int idx) throws SQLException {
		if (entity.getAccessKey() != null) {
			stmt.setString(++idx, entity.getAccessKey());
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
		return idx;
	}

	@Override
	protected Azure populate(ResultSet rset) throws SQLException {
		Azure entity = new Azure();
		entity.setId(rset.getLong("ID"));
		entity.setAccessKey(rset.getString("ACCESSKEY"));
		entity.setCreatedTs(rset.getLong("CREATEDTS"));
		entity.setEnabled(rset.getBoolean("ENABLED"));
		entity.setHost(rset.getString("HOST"));
		entity.setModifiedTs(rset.getLong("MODIFIEDTS"));
		return entity;
	}
}
