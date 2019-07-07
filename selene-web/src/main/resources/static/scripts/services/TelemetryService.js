services.factory("TelemetryService", ["$http", function ($http) {

  function getTelemetry(deviceId) {
    return $http.get("/api/selene/telemetries/" + deviceId + "/device");
  }

  return {
    getTelemetry: getTelemetry
  };
}
]);
