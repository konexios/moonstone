package com.arrow.dashboard.widget;

import org.springframework.util.Assert;

import com.arrow.dashboard.exception.MessagingException;
import com.arrow.dashboard.messaging.SimpleTopicProvider;
import com.arrow.dashboard.widget.annotation.OnCorrection;
import com.arrow.dashboard.widget.annotation.OnCreate;
import com.arrow.dashboard.widget.annotation.OnDestroy;
import com.arrow.dashboard.widget.annotation.OnError;
import com.arrow.dashboard.widget.annotation.OnLoading;
import com.arrow.dashboard.widget.annotation.OnReady;
import com.arrow.dashboard.widget.annotation.OnRunning;
import com.arrow.dashboard.widget.annotation.OnStopped;
import com.arrow.dashboard.widget.annotation.messaging.TopicProvider;

public abstract class WidgetAbstract extends WidgetEntityAbstract implements IWidget {

	private volatile WidgetStates state = WidgetStates.Initialize;

	@TopicProvider("/widget-state")
	protected SimpleTopicProvider stateTopicProvider;

	@TopicProvider("/widget-error")
	protected SimpleTopicProvider errorTopicProvider;

	protected WidgetAbstract() {
	}

	@Override
	public WidgetStates getState() {
		return state;
	}

	@OnCreate
	public void onCreateState() {
		Assert.isTrue(state == WidgetStates.Initialize, "Invalid state! Must be in Initialize. state: " + state);

		String method = "onCreateState";
		logInfo(method, "...");
		state = WidgetStates.Create;
		logInfo(method, "state: %s", state);

		sendStateMessage();
	}

	@OnLoading
	public void onLoadingState() {
		Assert.isTrue((state == WidgetStates.Initialize || state == WidgetStates.Create),
		        "Invalid state! Must be in Initialize or Create state. state: " + state);

		String method = "onLoadingState";
		logInfo(method, "...");
		state = WidgetStates.Loading;
		logInfo(method, "state: %s", state);

		sendStateMessage();
	}

	@OnReady
	public void onReadyState() {
		Assert.isTrue((state == WidgetStates.Loading || state == WidgetStates.Stopped),
		        "Invalid state! Must be in Loading or Stopped state. state: " + state);

		String method = "onReadyState";
		logInfo(method, "...");
		state = WidgetStates.Ready;
		logInfo(method, "state: %s", state);

		sendStateMessage();
	}

	@OnRunning
	public void onRunningState() {
		Assert.isTrue((state == WidgetStates.Ready || state == WidgetStates.Stopped),
		        "Invalid state! Must be in Ready or Stopped state. state: " + state);

		String method = "onRunningState";
		logInfo(method, "...");
		state = WidgetStates.Running;
		logInfo(method, "state: %s", state);

		sendStateMessage();
	}

	@OnStopped
	public void onStoppedState() {
		Assert.isTrue((state == WidgetStates.Running), "Invalid state! Must be in Running state. state: " + state);

		String method = "onStoppedState";
		logInfo(method, "...");
		state = WidgetStates.Stopped;
		logInfo(method, "state: %s", state);

		sendStateMessage();
	}

	@OnError
	public void onErrorState() {
		Assert.isTrue((state == WidgetStates.Ready || state == WidgetStates.Correction),
		        "Invalid state! Must be in Ready or Correction state. state: " + state);

		String method = "onErrorState";
		logInfo(method, "...");
		state = WidgetStates.Error;
		logInfo(method, "state: %s", state);

		sendMessage(errorTopicProvider, new WidgetData().withState(getState()));

		sendStateMessage();
	}

	@OnCorrection
	public void onCorrectionState() {
		Assert.isTrue((state == WidgetStates.Error), "Invalid state! Must be in Error state. state: " + state);

		String method = "onCorrectionState";
		logInfo(method, "...");
		state = WidgetStates.Correction;
		logInfo(method, "state: %s", state);

		sendStateMessage();
	}

	@OnDestroy
	public void onDestroyState() {
		Assert.isTrue(
		        (state == WidgetStates.Create || state == WidgetStates.Ready || state == WidgetStates.Running
		                || state == WidgetStates.Error),
		        "Invalid state! Must be in Create, Ready, Running or Error state. state: " + state);

		String method = "onDestroyState";
		logInfo(method, "...");
		state = WidgetStates.Destroyed;
		logInfo(method, "state: %s", state);

		sendStateMessage();
	}

	public boolean isCreating() {
		return state == WidgetStates.Create;
	}

	public boolean isLoading() {
		return state == WidgetStates.Loading;
	}

	public boolean isReady() {
		return state == WidgetStates.Ready;
	}

	public boolean isRunning() {
		return state == WidgetStates.Running;
	}

	public boolean isStopped() {
		return state == WidgetStates.Stopped;
	}

	public boolean isError() {
		return state == WidgetStates.Error;
	}

	public boolean isCorrection() {
		return state == WidgetStates.Correction;
	}

	public boolean isDestroyed() {
		return state == WidgetStates.Destroyed;
	}

	private void sendStateMessage() {
		sendMessage(stateTopicProvider, new WidgetData().withState(state));
	}

	protected void sendMessage(SimpleTopicProvider topicProvider, WidgetData message) {
		try {
			topicProvider.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}