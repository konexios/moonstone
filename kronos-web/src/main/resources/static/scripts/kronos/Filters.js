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


filters.filter("unique", function () {
    return function (items, filterOn) {

        if (filterOn === false) {
            return items;
        }

        if ((filterOn || angular.isUndefined(filterOn)) && angular.isArray(items)) {
            var hashCheck = {}, newItems = [];

            var extractValueToCompare = function (item) {
                if (angular.isObject(item) && angular.isString(filterOn)) {
                    return item[filterOn];
                } else {
                    return item;
                }
            };

            angular.forEach(items, function (item) {
                var valueToCheck, isDuplicate = false;

                for (var i = 0; i < newItems.length; i++) {
                    if (angular.equals(extractValueToCompare(newItems[i]), extractValueToCompare(item))) {
                        isDuplicate = true;
                        break;
                    }
                }
                if (!isDuplicate) {
                    newItems.push(item);
                }

            });
            items = newItems;
        }
        return items;
    };
});

filters.filter("duration", function () {
    return function (durationInMs) {
        var days, hours, mins, secs;
        durationInMs = Math.abs(+durationInMs);

        if (!isNaN(durationInMs)) {
            if (durationInMs < 1000) {
                return durationInMs+'ms';
            } else if (durationInMs < 10000) {
                secs = Math.floor(durationInMs / 100) / 10;
                return secs+'s';
            } else if (durationInMs < 60000) {
                secs = Math.floor(durationInMs / 1000);
                return secs+'s';
            } else if (durationInMs < 600000) {
                mins = Math.floor(durationInMs / 60000);
                secs = Math.floor((durationInMs - mins*60000) / 1000);
                return mins+'m'+(secs > 0 ? ' '+secs+'s' : '');
            } else if (durationInMs < 3600000) {
                mins = Math.floor(durationInMs / 60000);
                return mins+'m';
            } else if (durationInMs < 36000000) {
                hours = Math.floor(durationInMs / 3600000);
                mins = Math.floor((durationInMs - hours*3600000) / 60000);
                return hours+'h'+(mins > 0 ? ' '+mins+'m' : '');
            } else if (durationInMs < 86400000) {
                hours = Math.floor(durationInMs / 3600000);
                return hours+'h';
            } else if (durationInMs < 864000000) {
                days = Math.floor(durationInMs / 8640000)/10;
                return days+'d';
            } else {
                days = Math.floor(durationInMs / 86400000);
                return days+'d';
            }
        }
        return '';
    };
});