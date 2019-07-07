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
	return function(input) {
		if (input!=null)
			input = input.toLowerCase();
			return input.substring(0,1).toUpperCase()+input.substring(1);
	};
});


function _isJson(item) {
	item = typeof item !== "string" ? JSON.stringify(item) : item;

	try {
		item = JSON.parse(item);
	} catch (e) {
		return false;
	}

	if (typeof item === "object" && item !== null) {
		return true;
	}

	return false;
}

filters.filter("prettyJSON", function () {
	return function(item) {
		if (_isJson(item)) {
			return angular.toJson(JSON.parse(item), true); // here "true" is pretty or not
		} else {
			return item;
		}
	};
});

filters.filter("pluckUniq", function () {
	return function(list, name) {
		var result = [];
		if (list && list.length > 0 && typeof name == 'string') {
			for(var i=0; i<list.length; i++) {
				if (list[i] && list[i][name] && result.indexOf(list[i][name]) < 0) {
					result.push(list[i][name]);
				}
			}
		}
		return result;
	};
});

filters.filter("camelCaseLabel", function () {
	return function(value) {
		if (value && value.length > 0) {
			// capitilize the first letter
			value = value.substring(0, 1).toUpperCase() + value.substring(1);
			// split by upper case letters including them, then join by space
			value = value.split(/(?=[A-Z])/).join(' ');
		}
		return value;
	};
});

filters.filter("bigDigFormatter", function () {
	return function(value) {

		var exp, dig, suffixes = ['K', 'M', 'G', 'T', 'P', 'E'];

		if(window.isNaN(value) || value === null) {
			return value;
		}

		if(value < 10000){
			return value;
		}

		exp = Math.floor( Math.log(value) / Math.log(1000));
		dig = value / Math.pow(1000, exp);

		return (Math.floor(dig * 10) / 10) + suffixes[exp - 1];
	};
});
