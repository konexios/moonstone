package com.arrow.kronos;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.arrow.kronos.action.global.DataMessageModel;
import com.arrow.kronos.action.global.GlobalActionHandler;
import com.arrow.kronos.action.global.GlobalActionHandlerFactory;
import com.arrow.kronos.data.action.GlobalAction;
import com.arrow.kronos.data.action.GlobalActionType;
import com.arrow.pegasus.service.RabbitListenerAbstract;

import moonstone.acn.MqttConstants;
import moonstone.acs.JsonUtils;

@Component
public class GlobalActionRabbitListener extends RabbitListenerAbstract implements CommandLineRunner {

	private static final String GLOBAL_ACTION_QUEUE = "kronos.action";
	private static final String GLOBAL_ACTION_ROUTE = "kronos.action";

	@Value("${GlobalActionRabbitListener.enabled:true}")
	private boolean enabled;
	@Value("${GlobalActionRabbitListener.numThreads:10}")
	private int numThreads;

	@Autowired
	private KronosEngineContext context;
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Override
	public void run(String... args) throws Exception {
		String method = "run";
		if (enabled) {
			setApplicationEventPublisher(applicationEventPublisher);
			init();
			start();
		} else {
			logWarn(method, "%s is DISABLED!", getClass().getSimpleName());
		}
	}

	private void init() {
		String method = "init";
		logDebug(method, "...");
		TopicExchange topicExchange = new TopicExchange(MqttConstants.DEFAULT_RABBITMQ_EXCHANGE);
		getRabbitAdmin().declareExchange(topicExchange);
		Queue queue = new Queue(GLOBAL_ACTION_QUEUE, true, false, true);
		getRabbitAdmin().declareQueue(queue);
		setQueues(new String[] { GLOBAL_ACTION_QUEUE });
		getRabbitAdmin().declareBinding(BindingBuilder.bind(queue).to(topicExchange).with(GLOBAL_ACTION_ROUTE));
	}

	@Override
	public void receiveMessage(byte[] message, String queueName) {
		String method = "receiveMessage";
		logDebug(method, "...");
		blockDispatch(() -> {
			logDebug(method, "%s %s", queueName, new String(message));

			DataMessageModel data = JsonUtils.fromJsonBytes(message, DataMessageModel.class);
			Assert.notNull(data, "dataMessage is null");

			GlobalAction globalAction = getGlobalAction(data);
			Assert.notNull(globalAction, "globalAction is not found");

			if (globalAction.isEnabled()) {
				GlobalActionType globalActionType = context.getKronosCache()
				        .findGlobalActionTypeById(globalAction.getGlobalActionTypeId());
				Assert.notNull(globalActionType,
				        "globalActionType is not found for globalAction id: %s" + globalAction.getId());
				if (globalActionType.isEnabled()) {
					GlobalActionHandler handler = GlobalActionHandlerFactory.create(globalActionType);
					context.getSpringContext().getAutowireCapableBeanFactory().autowireBean(handler);
					handler.handle(data, globalAction);
				} else {
					logWarn(method, "globalActionType is disabled for globalAction id: %s", globalAction.getId());
				}
			} else {
				logWarn(method, "globalAction is disabled, id: %s", globalAction.getId());
			}

		});
	}

	private GlobalAction getGlobalAction(DataMessageModel data) {
		GlobalAction globalAction = null;
		if (data.getGlobalActionHid() != null) {
			globalAction = context.getKronosCache().findGlobalActionByHid(data.getGlobalActionHid());
		} else if (data.getGlobalActionId() != null) {
			globalAction = context.getKronosCache().findGlobalActionById(data.getGlobalActionId());
		} else if (data.getApplicationId() != null && data.getSystemName() != null) {
			globalAction = context.getKronosCache().findGlobalActionBySystemName(data.getApplicationId(),
			        data.getSystemName());
		}
		return globalAction;
	}

	@Override
	protected int getNumWorkerThreads() {
		return numThreads;
	}

	@Override
	protected void onMessageListenerContainerRestart() {
		init();
	}
}
