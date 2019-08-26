package moonstone.selene.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;

import moonstone.selene.data.Message;
import moonstone.selene.data.MessageSeverity;

public class MessageDao extends DaoAbstract<Message> {

	private static final class SingletonHolder {
		static final MessageDao SINGLETON = new MessageDao();
	}

	public static MessageDao getInstance() {
		return SingletonHolder.SINGLETON;
	}

	private MessageDao() {
	}

	public List<Message> findByDeviceId(long deviceId) {
		return DaoManager.getInstance().execute(connection -> {
			try (PreparedStatement stmt = connection.prepareStatement(getQueries().getProperty("findByDevicedId"))) {
				stmt.setLong(1, deviceId);
				try (ResultSet rset = stmt.executeQuery()) {
					List<Message> result = new ArrayList<>(rset.getFetchSize());
					while (rset.next()) {
						result.add(populate(rset));
					}
					return result;
				}
			}
		});
	}

	public List<Message> findByClassName(String className) {
		Validate.notNull(className);
		return DaoManager.getInstance().execute(connection -> {
			try (PreparedStatement stmt = connection.prepareStatement(getQueries().getProperty("findByClassName"))) {
				stmt.setString(1, className);
				try (ResultSet rset = stmt.executeQuery()) {
					List<Message> result = new ArrayList<>(rset.getFetchSize());
					while (rset.next()) {
						result.add(populate(rset));
					}
					return result;
				}
			}
		});
	}

	public List<Message> findByMethodName(String methodName) {
		Validate.notNull(methodName);
		return DaoManager.getInstance().execute(connection -> {
			try (PreparedStatement stmt = connection.prepareStatement(getQueries().getProperty("findByMethodName"))) {
				stmt.setString(1, methodName);
				try (ResultSet rset = stmt.executeQuery()) {
					List<Message> result = new ArrayList<>(rset.getFetchSize());
					while (rset.next()) {
						result.add(populate(rset));
					}
					return result;
				}
			}
		});
	}

	public List<Message> findByObjectName(String objectName) {
		return DaoManager.getInstance().execute(connection -> {
			try (PreparedStatement stmt = connection.prepareStatement(getQueries().getProperty("findByObjectName"))) {
				stmt.setString(1, objectName);
				try (ResultSet rset = stmt.executeQuery()) {
					List<Message> result = new ArrayList<>(rset.getFetchSize());
					while (rset.next()) {
						result.add(populate(rset));
					}
					return result;
				}
			}
		});
	}

	public List<Message> findByObjectId(String objectId) {
		return DaoManager.getInstance().execute(connection -> {
			try (PreparedStatement stmt = connection.prepareStatement(getQueries().getProperty("findByObjectId"))) {
				stmt.setString(1, objectId);
				try (ResultSet rset = stmt.executeQuery()) {
					List<Message> result = new ArrayList<>(rset.getFetchSize());
					while (rset.next()) {
						result.add(populate(rset));
					}
					return result;
				}
			}
		});
	}

	public List<Message> findByTimestampBefore(long timestamp) {
		return DaoManager.getInstance().execute(connection -> {
			try (PreparedStatement stmt = connection
			        .prepareStatement(getQueries().getProperty("findByTimestampBefore"))) {
				stmt.setLong(1, timestamp);
				try (ResultSet rset = stmt.executeQuery()) {
					List<Message> result = new ArrayList<>(rset.getFetchSize());
					while (rset.next()) {
						result.add(populate(rset));
					}
					return result;
				}
			}
		});
	}

	@Override
	protected int commonStatement(PreparedStatement stmt, Message entity, int idx) throws SQLException {
		if (entity.getClassName() != null) {
			stmt.setString(++idx, entity.getClassName());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		if (entity.getMessage() != null) {
			stmt.setString(++idx, entity.getMessage());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		if (entity.getMethodName() != null) {
			stmt.setString(++idx, entity.getMethodName());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		if (entity.getObjectId() != null) {
			stmt.setString(++idx, entity.getObjectId());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		if (entity.getObjectName() != null) {
			stmt.setString(++idx, entity.getObjectName());
		} else {
			stmt.setNull(++idx, Types.VARCHAR);
		}
		stmt.setInt(++idx, entity.getSeverity().ordinal());
		stmt.setLong(++idx, entity.getTimestamp());
		stmt.setLong(++idx, entity.getDeviceId());
		return idx;
	}

	@Override
	protected Message populate(ResultSet rset) throws SQLException {
		Message entity = new Message();
		entity.setId(rset.getLong("ID"));
		entity.setClassName(rset.getString("CLASSNAME"));
		entity.setMessage(rset.getString("MESSAGE"));
		entity.setMethodName(rset.getString("METHODNAME"));
		entity.setObjectId(rset.getString("OBJECTID"));
		entity.setObjectName(rset.getString("OBJECTNAME"));
		entity.setSeverity(MessageSeverity.values()[rset.getInt("SEVERITY")]);
		entity.setTimestamp(rset.getLong("TIMESTAMP"));
		entity.setDeviceId(rset.getLong("DEVICEID"));
		return entity;
	}
}
