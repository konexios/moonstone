package com.arrow.selene.service;

import java.util.List;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.SeleneConstants;
import com.arrow.selene.dao.MessageDao;
import com.arrow.selene.data.Message;

public class MessageService extends ServiceAbstract {

	private static class SingletonHolder {
		private static final MessageService SINGLETON = new MessageService();
	}

	public static MessageService getInstance() {
		return SingletonHolder.SINGLETON;
	}

	private boolean databaseLogging;
	private MessageDao messageDao;

	protected MessageService() {
		super();
		messageDao = MessageDao.getInstance();
		try {
			databaseLogging = Boolean
			        .valueOf(System.getProperty(SeleneConstants.ENV_DATABASE_LOGGING, Boolean.TRUE.toString()));
		} catch (Exception e) {
			databaseLogging = false;
		}
	}

	protected MessageDao getMessageDao() {
		return messageDao;
	}

	public void init() {
		messageDao = MessageDao.getInstance();
	}

	public List<Message> findMessageByDeviceId(int deviceId) {
		return messageDao.findByDeviceId(deviceId);
	}

	public List<Message> findMessageByClassName(String className) {
		return messageDao.findByClassName(className);
	}

	public List<Message> findMessageByMethodName(String methodName) {
		return messageDao.findByMethodName(methodName);
	}

	public List<Message> findMessageByObjectName(String objectName) {
		return messageDao.findByObjectName(objectName);
	}

	public List<Message> findMessageByObjectId(String objectId) {
		return messageDao.findByObjectId(objectId);
	}

	public List<Message> findMessagesBefore(long timestamp) {
		return messageDao.findByTimestampBefore(timestamp);
	}

	public List<Message> findAll() {
		return getMessageDao().findAll();
	}

	public Message findOne() {
		List<Message> all = messageDao.findAll();
		if (all.size() > 1) {
			throw new RuntimeException("ERROR: more than one message record found!");
		}
		if (all.size() == 1) {
			return all.get(0);
		} else {
			return null;
		}
	}

	public Message findById(long id) {
		return messageDao.findById(id);
	}

	public void deleteMessageBefore(long timestamp) {
		messageDao.findAll();
		findMessagesBefore(timestamp).forEach(message -> messageDao.delete(message.getId()));
	}

	public Message save(Message message) {
		if (databaseLogging) {
			Validate.notNull(message, "message is null");
			messageDao.insert(message);
		}
		return message;
	}
}
