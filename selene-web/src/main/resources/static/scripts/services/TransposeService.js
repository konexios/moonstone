services.factory("TransposeService", ["$http", function ($http) {
	var tranObj =null;
	var regTransposeObj = null;
	var telTransposeObj = null;
	var stateTransposeObj = null;
		
	function set(data){
		tranObj = data;
	}
	
	function get(){
		return tranObj;
	}
	
	function setRegTransposeObj(data){
		regTransposeObj = data;
	}
	
	function getRegTransposeObj(){
		return regTransposeObj;
	}
	
	function setTelTransposeObj(data){
		telTransposeObj = data;
	}
	
	function getTelTransposeObj(){
		return telTransposeObj;
	}
	
	function setStateTransposeObj(data){
		stateTransposeObj = data;
	}
	
	function getStateTransposeObj(){
		return stateTransposeObj;
	}
	
	return {
		set:set,
	    get:get,
	    setRegTransposeObj :setRegTransposeObj,
	    getRegTransposeObj : getRegTransposeObj,
	    setTelTransposeObj : setTelTransposeObj,
	    getTelTransposeObj : getTelTransposeObj,
	    setStateTransposeObj : setStateTransposeObj,
	    getStateTransposeObj: getStateTransposeObj
	  }
	}
]);
