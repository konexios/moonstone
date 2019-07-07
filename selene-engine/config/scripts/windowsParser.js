function bytesToString(bytes) {
	var str = "";
	for(var i = 0; i < bytes.length; i ++) {
		str += String.fromCharCode(bytes[i]);
	}
	return str;
}

var payload = bytesToString(data.payload);

regex = /LastBootUpTime=(.*)/;
value = regex.exec(payload)[1];
time = value.replace(/(\d{4})(\d{2})(\d{2})(\d{2})(\d{2}).*/, "$1-$2-$3 $4:$5");
iotParams.setString("lastRebootTime", time);

regex = /Name=(\d+)\s+PercentIdleTime=(\d+)/mg;
while (value = regex.exec(payload)) {
	iotParams.setFloat("CPU" + value[1], 100 - value[2], "%.2f");
}

regex = /LoadPercentage=\s*(.*)/;
value = regex.exec(payload)[1];
iotParams.setFloat("cpuLoadAverage", value, "%.2f");

regex = /(\d+)\s+(\w:)\s+(\d+)/mg;
while (value = regex.exec(payload)) {
	iotParams.setFloat(value[2], 100 - value[1] / value[3] * 100 , "%.2f");
}

regex = /TotalVisibleMemorySize=(.+)/;
value = regex.exec(payload)[1];
var total = value / 1024 / 1024;
iotParams.setFloat("totalPhysicalMemory", total, "%.2f");

regex = /FreePhysicalMemory=(.+)/;
value = regex.exec(payload)[1];
var free = value / 1024 / 1024;
iotParams.setFloat("freePhysicalMemory", free, "%.2f");

iotParams.setFloat("usedPhysicalMemory", total - free, "%.2f");

iotParams.setFloat("freePhysicalMemoryPercent", free / total * 100, "%.2f");

data.setParsedIotParameters(iotParams);


regex = /TotalSwapSpaceSize=(.*)/;
value = regex.exec(payload);

var percent;
if(value == null) {
	total = 0;
	free = 0;
	percent = 0;
} else {
	total = value[1] / 1024 / 1024;

	regex = /FreeSpaceInPagingFiles=(.+)/;
	value = regex.exec(payload)[1];
	free = value / 1024 / 1024;

	percent = free / total * 100;
}

iotParams.setFloat("totalSwapMemory", total, "%.2f");
iotParams.setFloat("freeSwapMemory", free, "%.2f");
iotParams.setFloat("usedSwapMemory", total - free, "%.2f");
iotParams.setFloat("freeSwapMemoryPercent", percent, "%.2f");

data.setParsedIotParameters(iotParams);