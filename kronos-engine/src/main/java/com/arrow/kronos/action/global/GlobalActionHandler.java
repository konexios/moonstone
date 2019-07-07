package com.arrow.kronos.action.global;

import com.arrow.kronos.data.action.GlobalAction;

public interface GlobalActionHandler {

	void handle(DataMessageModel data, GlobalAction globalAction);

}
