package com.arrow.selene.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.arrow.acs.AcsSystemException;
import com.arrow.selene.data.Sequence;

public class SequenceDao extends DaoAbstract<Sequence> {

	private static final class SingletonHolder {
		static final SequenceDao SINGLETON = new SequenceDao();
	}

	public static SequenceDao getInstance() {
		return SingletonHolder.SINGLETON;
	}

	private SequenceDao() {
	}

	@Override
	protected Sequence populate(ResultSet result) throws SQLException {
		throw new SQLException("method is not implemented");
	}

	@Override
	protected int updateStatement(PreparedStatement stmt, Sequence entity) throws SQLException {
		throw new SQLException("method is not implemented");
	}

	@Override
	protected int insertStatement(PreparedStatement stmt, Sequence entity) throws SQLException {
		throw new SQLException("method is not implemented");
	}

	@Override
	protected int commonStatement(PreparedStatement stmt, Sequence entity, int idx) throws SQLException {
		return idx;
	}

	public synchronized long getNextSequence() {
		long sequence = DaoManager.getInstance().execute(connection -> {
			try (Statement stmt = connection.createStatement();
			        ResultSet rset = stmt.executeQuery(getQueries().getProperty("getNextSequence"))) {
				if (rset.next()) {
					return rset.getLong("SEQ_COUNT");
				} else {
					throw new AcsSystemException(DaoManager.TABLE_SEQUENCE + " table has bad data!");
				}
			}
		});
		DaoManager.getInstance().execute(connection -> {
			try (PreparedStatement stmt = connection.prepareStatement(getQueries().getProperty("updateNextSequence"))) {
				stmt.setLong(1, sequence + 1);
				stmt.executeUpdate();
				return null;
			}
		});
		return sequence;
	}

	// public synchronized long getNextSequence() {
	// return DaoManager.getInstance().execute(connection -> {
	// try (Statement stmt =
	// connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
	// ResultSet.CONCUR_UPDATABLE); ResultSet rset = stmt.executeQuery(
	// "SELECT * FROM SEQUENCE WHERE SEQ_NAME = 'SEQ_GEN' FOR UPDATE")) {
	// if (rset.next()) {
	// long result = rset.getLong("SEQ_COUNT");
	// rset.updateLong("SEQ_COUNT", result + 1);
	// rset.updateRow();
	// return result;
	// } else {
	// throw new AcsSystemException("SEQUENCE table has bad data!");
	// }
	// }
	// });
	// }
}
