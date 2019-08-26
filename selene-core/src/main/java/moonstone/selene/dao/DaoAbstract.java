package moonstone.selene.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import moonstone.acs.AcsSystemException;
import moonstone.acs.Loggable;
import moonstone.selene.data.BaseEntity;
import moonstone.selene.data.EntityAbstract;

public abstract class DaoAbstract<T extends EntityAbstract> extends Loggable {
	private Properties queries = new Properties();
	private Map<String, String> addColumnMap = new HashMap<>();

	public DaoAbstract() {
		try {
			queries.load(getClass().getResourceAsStream("/dao/" + getClass().getSimpleName() + ".properties"));
			for (String property : queries.stringPropertyNames()) {
				if (property.startsWith("add.column.")) {
					addColumnMap.put(property.substring(11).trim(), queries.getProperty(property).trim());
				}
			}
		} catch (Exception e) {
			throw new AcsSystemException("error loading queries", e);
		}
	}

	protected int updateStatement(PreparedStatement stmt, T entity) throws SQLException {
		int idx = 0;
		idx = commonStatement(stmt, entity, idx);
		stmt.setLong(++idx, entity.getId());
		return idx;
	}

	protected int insertStatement(PreparedStatement stmt, T entity) throws SQLException {
		int idx = 0;
		long sequence = SequenceDao.getInstance().getNextSequence();
		stmt.setLong(++idx, sequence);
		entity.setId(sequence);
		return commonStatement(stmt, entity, idx);
	}

	protected abstract int commonStatement(PreparedStatement stmt, T entity, int idx) throws SQLException;

	public List<T> findAll() {
		return DaoManager.getInstance().execute(connection -> {
			try (ResultSet rset = connection.createStatement().executeQuery(queries.getProperty("findAll"))) {
				List<T> result = new ArrayList<>(rset.getFetchSize());
				while (rset.next()) {
					result.add(populate(rset));
				}
				return result;
			}
		});
	}

	public T findById(long id) {
		return DaoManager.getInstance().execute(connection -> {
			try (PreparedStatement stmt = connection.prepareStatement(queries.getProperty("findById"))) {
				stmt.setLong(1, id);
				try (ResultSet rset = stmt.executeQuery()) {
					return rset.next() ? populate(rset) : null;
				}
			}
		});
	}

	public T insert(T entity) {
		return DaoManager.getInstance().execute(connection -> {
			if (BaseEntity.class.isAssignableFrom(entity.getClass())) {
				BaseEntity be = (BaseEntity) entity;
				long now = Instant.now().toEpochMilli();
				if (be.getCreatedTs() == 0) {
					be.setCreatedTs(now);
				}
				if (be.getModifiedTs() == 0) {
					be.setModifiedTs(now);
				}
			}
			try (PreparedStatement stmt = connection.prepareStatement(queries.getProperty("insert"))) {
				insertStatement(stmt, entity);
				stmt.executeUpdate();
				return entity;
			}
		});
	}

	public T update(T entity) {
		return DaoManager.getInstance().execute(connection -> {
			if (BaseEntity.class.isAssignableFrom(entity.getClass())) {
				BaseEntity be = (BaseEntity) entity;
				long now = Instant.now().toEpochMilli();
				if (be.getCreatedTs() == 0) {
					be.setCreatedTs(now);
				}
				be.setModifiedTs(now);
			}
			try (PreparedStatement stmt = connection.prepareStatement(queries.getProperty("update"))) {
				updateStatement(stmt, entity);
				stmt.executeUpdate();
				return entity;
			}
		});
	}

	public int delete(long id) {
		return DaoManager.getInstance().execute(connection -> {
			try (PreparedStatement stmt = connection.prepareStatement(queries.getProperty("delete"))) {
				stmt.setLong(1, id);
				return stmt.executeUpdate();
			}
		});
	}

	public Properties getQueries() {
		return queries;
	}

	public Map<String, String> getAddColumnMap() {
		return addColumnMap;
	}

	protected abstract T populate(ResultSet result) throws SQLException;
}
