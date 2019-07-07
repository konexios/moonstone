function bytesToString(bytes) {
	var str = "";
	for(var i = 0; i < bytes.length; i ++) {
		str += String.fromCharCode(bytes[i]);
	}
	return str;
}

var payload = bytesToString(data.payload);

regex = /Last Reboot Time:\s*(.+)/;
value = regex.exec(payload)[1];
iotParams.setString("lastRebootTime", value);

regex = /(CPU\d+):(.+)/mg;
while (value = regex.exec(payload)) {
	iotParams.setFloat(value[1], value[2], "%.2f");
}

regex = /Load Average:\s*(.+)/;
value = regex.exec(payload)[1];
iotParams.setFloat("cpuLoadAverage", value, "%.2f");

regex = /(\/.*) \| used (\d+)% free space ([\d\.]+[A-Z])/mg;
while (value = regex.exec(payload)) {
	iotParams.setFloat(value[1], value[2], "%.2f");
}

regex = /Total physical memory:(.+)/;
value = regex.exec(payload)[1];
iotParams.setFloat("totalPhysicalMemory", value, "%.2f");

regex = /Used physical memory:(.+)/;
value = regex.exec(payload)[1];
iotParams.setFloat("usedPhysicalMemory", value, "%.2f");

regex = /Free physical memory:(.+)/;
value = regex.exec(payload)[1];
iotParams.setFloat("freePhysicalMemory", value, "%.2f");

regex = /Percent of free physical memory:(\d+)%/;
value = regex.exec(payload)[1];
iotParams.setFloat("freePhysicalMemoryPercent", value, "%.2f");

regex = /Total swap memory:(.+)/;
value = regex.exec(payload)[1];
iotParams.setFloat("totalSwapMemory", value, "%.2f");

regex = /Used swap memory:(.+)/;
value = regex.exec(payload)[1];
iotParams.setFloat("usedSwapMemory", value, "%.2f");

regex = /Free swap memory:(.+)/;
value = regex.exec(payload)[1];
iotParams.setFloat("freeSwapMemory", value, "%.2f");

regex = /Percent of free swap memory:(\d+)%/;
value = regex.exec(payload)[1];
iotParams.setFloat("freeSwapMemoryPercent", value, "%.2f");


data.setParsedIotParameters(iotParams);