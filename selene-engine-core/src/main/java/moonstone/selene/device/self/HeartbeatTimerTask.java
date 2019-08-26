package moonstone.selene.device.self;

import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.Validate;

import moonstone.selene.Loggable;
import moonstone.selene.data.Gateway;

class HeartbeatTimerTask extends TimerTask {
    private static final Loggable LOGGER = new Loggable();
    private Gateway gateway;
    private AtomicBoolean running = new AtomicBoolean(false);

    public HeartbeatTimerTask(Gateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void run() {
        String method = "HeartbeatTimerTask.run";
        if (running.compareAndSet(false, true)) {
            Validate.notNull(gateway, "gateway is null");
            LOGGER.logInfo(method, "sending heartbeat ...");
            try {
                SelfModule.getInstance().getAcnClient().getGatewayApi().heartbeat(gateway.getHid());
                LOGGER.logInfo(method, "heartbeat sent successfully");
            } catch (Throwable throwable) {
                LOGGER.logError(method, "failed to send heartbeat", throwable);
            }
            running.set(false);
        }
    }
}
