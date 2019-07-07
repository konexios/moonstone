// payload example
// {"temperature":"6.2"}

function bytesToString(bytes) {
	var str = "";
	for(var i = 0; i < bytes.length; i ++) {
		str += String.fromCharCode(bytes[i]);
	}
	return str;
}

function celsiusToFahrenheit(temp) {
	return temp * 9 / 5 + 32;
}

var payload = bytesToString(data.payload);
var obj = JSON.parse(payload);
var tempC = obj.temperature;
var tempF = celsiusToFahrenheit(tempC);
iotParams.setString("payload", payload);
iotParams.setFloat("tempC", tempC, "%.2f");
iotParams.setFloat("tempF", tempF, "%.2f");
data.setParsedIotParameters(iotParams);