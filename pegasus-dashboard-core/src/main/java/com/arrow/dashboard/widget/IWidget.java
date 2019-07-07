package com.arrow.dashboard.widget;

public interface IWidget {

	public WidgetStates getState();

	public void onCreateState();

	public void onLoadingState();

	public void onReadyState();

	public void onRunningState();

	public void onStoppedState();
	
	public void onErrorState();
	
	public void onCorrectionState();
	
	public void onDestroyState();
}