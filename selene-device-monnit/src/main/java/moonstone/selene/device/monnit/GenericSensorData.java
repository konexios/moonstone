package moonstone.selene.device.monnit;

import com.monnit.mine.MonnitMineAPI.SensorMessage;

public class GenericSensorData extends SensorDataAbstract {

    public GenericSensorData(SensorMessage sensorMessage) {
        super(sensorMessage);
    }

	@Override
	public String toString() {
		return "GenericSensorData [toString()=" + super.toString() + "]";
	}
}
