package moonstone.selene.databus;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.Validate;

import moonstone.acn.client.utils.Utils;
import moonstone.selene.databus.DatabusAbstract;
import moonstone.selene.service.ConfigService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDatabus extends DatabusAbstract {

	private JedisPool jedisPool;
	private RedisThread redisThread;
	private AtomicReference<byte[][]> keys = new AtomicReference<>(new byte[0][]);

	public RedisDatabus() {
		logInfo(getClass().getSimpleName(), "...");
	}

	@Override
	public void start() {
		String method = "start";
		Validate.isTrue(!isStopped(), "databus is already stopped");
		if (redisThread == null) {
			jedisPool = new JedisPool();
			redisThread = new RedisThread();
			redisThread.setName("RedisDatabus");
			redisThread.start();
			logInfo(method, "started!");
		}
	}

	@Override
	public void stop() {
		String method = "stop";
		setStopped(true);
		if (jedisPool != null) {
			try {
				logInfo(method, "destroying jedisPool ...");
				jedisPool.destroy();
			} catch (Throwable ignored) {
			}
			jedisPool = null;
		}
		Utils.shutdownThread(redisThread);
		redisThread = null;
	}

	@Override
	public void send(String queue, byte[] message) {
		String method = "send";
		if (jedisPool == null || isStopped()) {
			return;
		}
		try (Jedis jedis = jedisPool.getResource()) {
			logDebug(method, "publishing to queue: %s, message size: %d", queue, message == null ? 0 : message.length);
			Long size = jedis.rpush(queue.getBytes(StandardCharsets.UTF_8), message);
			if (size > getMaxBuffer()) {
				logInfo(method, "list is too large, trimming now ...");
				jedis.ltrim(queue.getBytes(StandardCharsets.UTF_8), size - getMaxBuffer(), -1L);
			}
		} catch (Exception e) {
			logError(method, "error queuing message", e);
		}
	}

	@Override
	protected void checkAndCreateQueue(String queue) {
		Set<byte[]> keySet = new HashSet<>();
		for (String key : queueToListeners.keySet()) {
			keySet.add(key.getBytes(StandardCharsets.UTF_8));
		}
		if (!keySet.isEmpty()) {
			keys.set(keySet.toArray(new byte[keySet.size()][]));
		}
	}

	private class RedisThread extends Thread {
		@Override
		public void run() {
			String method = "RedisListener.run";
			if (jedisPool == null) {
				logError(method, "jedisPool is null");
			} else {
				while (!isStopped()) {
					if (keys.get().length == 0) {
						long peekInterval = ConfigService.getInstance().getSeleneProperties().getDatabusPeekInterval();
						logInfo(method, "no queues to poll, sleeping for %d milliseconds", peekInterval);
						Utils.sleep(peekInterval);
					} else {
						try (Jedis jedis = jedisPool.getResource()) {
							List<byte[]> result = jedis.blpop(10, keys.get());
							if (result != null && !result.isEmpty()) {
								String queue = new String(result.get(0), StandardCharsets.UTF_8);
								byte[] message = result.get(1);
								notifyListener(queue, message);
							}
						} catch (Exception e) {
							if (!isStopped()) {
								logError(method, "error polling messages", e);
							}
						}
					}
				}
			}
			logInfo(method, "exiting ...");
		}
	}
}
