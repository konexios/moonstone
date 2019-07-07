package com.arrow.selene.web.api;

import com.arrow.selene.Loggable;
import com.arrow.selene.service.AwsService;
import com.arrow.selene.service.AzureService;
import com.arrow.selene.service.DeviceService;
import com.arrow.selene.service.IbmService;
import com.arrow.selene.service.MessageService;
import com.arrow.selene.web.service.ConfigService;
import com.arrow.selene.web.service.GatewayService;
import com.arrow.selene.web.service.TelemetryService;

public abstract class BaseApi extends Loggable {

	private AwsService awsService;
	private AzureService azureService;
	private DeviceService deviceService;
	private GatewayService gatewayService;
	private IbmService ibmService;
	private MessageService messageService;
	private TelemetryService telemetryService;

	public BaseApi() {
		awsService = AwsService.getInstance();
		azureService = AzureService.getInstance();
		gatewayService = GatewayService.getInstance();
		ibmService = IbmService.getInstance();
		messageService = MessageService.getInstance();
		deviceService = DeviceService.getInstance();
	}

	public void createConfigService() {
		ConfigService.createInstance();
	}
	
	public ConfigService getConfigService() {
		return ConfigService.getInstance();
	}

	public void createTelemetryService() {
		telemetryService = TelemetryService.getInstance();
	}

	public AwsService getAwsService() {
		return awsService;
	}

	public AzureService getAzureService() {
		return azureService;
	}

	public DeviceService getDeviceService() {
		return deviceService;
	}

	public GatewayService getGatewayService() {
		return gatewayService;
	}

	public IbmService getIbmService() {
		return ibmService;
	}

	public MessageService getMessageService() {
		return messageService;
	}

	public TelemetryService getTelemetryService() {
		return telemetryService;
	}

}
