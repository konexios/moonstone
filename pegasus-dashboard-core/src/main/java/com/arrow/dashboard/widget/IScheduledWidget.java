package com.arrow.dashboard.widget;

public interface IScheduledWidget extends IWidget {
	
	public void start();
	
	public void run();
	
	public void stop();
	
	public void destroy();
}