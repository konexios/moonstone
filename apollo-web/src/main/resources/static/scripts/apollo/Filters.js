filters.filter("msToTime", function() {
	return function(input) {
	    var milliseconds = parseInt((input%1000)/100)
        , seconds = parseInt((input/1000)%60)
        , minutes = parseInt((input/(1000*60))%60)
        , hours = parseInt((input/(1000*60*60))%24);

	    hours = (hours < 10) ? "0" + hours : hours;
	    minutes = (minutes < 10) ? "0" + minutes : minutes;
	    seconds = (seconds < 10) ? "0" + seconds : seconds;

    	return hours + ":" + minutes + ":" + seconds + "." + milliseconds;
	};
});

filters.filter("capitalize", function() {
	return function(input, scope) {
		if (input!=null)
			input = input.toLowerCase();
			return input.substring(0,1).toUpperCase()+input.substring(1);
	}
});