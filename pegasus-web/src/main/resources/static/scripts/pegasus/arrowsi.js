var arrowsi = {};

arrowsi.Date = function() {

	this.formatDate = function(date) {
		var month = (date.getMonth() + 1);
		month = (month < 10 ? "0" + month : month);
		var day = date.getDate();
		day = (day < 10 ? "0" + day : day);
		
		var hours = date.getHours();
		var minutes = date.getMinutes();
		var ampm = hours >= 12 ? "PM" : "AM";
		hours = hours % 12;
		hours = hours ? hours : 12; // the hour "0" should be "12"
		minutes = minutes < 10 ? "0" + minutes : minutes;
		var strTime = hours + ":" + minutes + " " + ampm;

		return month + "/" + day + "/" + date.getFullYear() + " " + strTime;
	},
	
	this.now = function() {
		return new Date();
	},
	
	this.beginningOf = function(date) {
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		date.setMilliseconds(0);
		
		return date;
	},
	
	this.today = function() {
		return this.now();
	},
	
	this.beginningOfToday = function() {
		return this.beginningOf(this.today());
	},
	
	this.tomorrow = function() {
		var tomorrow = this.today();
		tomorrow.setDate(tomorrow.getDate() + 1);
		return tomorrow;
	},
	
	this.beginningOfTomorrow = function() {
		return this.beginningOf(this.tomorrow());
	}
};
var DateUtil = new arrowsi.Date();

arrowsi.Table = function() {
	
	this.columnHeader = function(label, value, sortable) {
		var column = {
			label: label,
			value: value,
			sortable: sortable
		};
		
		return column;
	}
};
var TableUtil = new arrowsi.Table();

arrowsi.Pagination = function() {

	this.reset = function(pagination) {
		pagination = {
			itemsPerPage: 10,
			pageIndex: 0,
			first: false,
			last: false,
			totalItems: 0,
			totalPages: 0,
			content: null,
			sort: {
				direction: "ASC",
				property: null
			}
		};
		
		return pagination;
	},
	
	this.create = function() {
		return this.reset({});		
	},
	
	this.update = function(pagination, data) {
		
		pagination.first = data.first;
		pagination.last = data.last;
		pagination.totalPages = data.totalPages;
		pagination.totalItems = data.totalElements;
		// content
		pagination.content = data.content;
		
		return pagination;
	},
	
	/**
	 * Change the number of items to be displayed
	 * @param {Number} numberOfItems 
	 * @return pagination
	 */
	this.changeItemsPerPage = function(pagination, numberOfItems) {
		pagination.pageIndex = 0;
		pagination.itemsPerPage = numberOfItems;
		
		return pagination;
	},
	
	/**
	 * Decrease the page index by 1
	 * @return pagination
	 */
	this.previousPage = function(pagination) {
		pagination.pageIndex--;
		
		return pagination;
	},

	/**
	 * Set the page index to the passed in pageNumber
	 * @param {Number} pageNumber
	 * @return pagination
	 */
	this.gotoPage = function(pagination, pageNumber) {
		pagination.pageIndex = pageNumber;
		
		return pagination;
	},
	
	/**
	 * Increase the page index by 1
	 * @return pagination
	 */
	this.nextPage = function(pagination) {
		pagination.pageIndex++;
		
		return pagination;		
	},
	
	this.sort = function(pagination, property) {
		if (pagination.sort.property === property) {
			// change sort direction
			pagination.sort.direction = (pagination.sort.direction === "ASC" ? "DESC" : "ASC");
		} else {
			// change sort field
			pagination.sort.property = property;
			// change sort direction
			pagination.sort.direction = "ASC";
		}

		// reset page index
		pagination.pageIndex = 0;
		
		return pagination;
	}
};
var PaginationUtil = new arrowsi.Pagination();

arrowsi.Parser = function() {
	/**
	 * 
	 */
	this.parseUrlForHid = function(url) {
		var index = url.indexOf("?");
		if (index == -1)
			index = url.indexOf("%3F");
		var hasParams = (index != -1);
		var length = (hasParams ? index : url.length);
		var beginIndex = url.lastIndexOf("/");
		var hid = url.substring(beginIndex + 1, length);
		
		// console.log("index: " + index + " hasParams: " + hasParams + " length: " + length + " beginIndex: " + beginIndex + " hid: " + hid);
		
		return hid;
	},
	
	/**
	 * 
	 */
	this.parseInstantMessage = function(message) {
		
		var result = message;
		
		var hasHyperlink = (message.toLowerCase().indexOf("<http://") != -1);
		var hasSecureHyperlink = (message.toLowerCase().indexOf("<https://") != -1);
		
		if (hasHyperlink)
			result = this.handleHyperlink("<http://", result);
		if (hasSecureHyperlink)
			result = this.handleHyperlink("<https://", result);
		
		return result;
	},
	
	/**
	 * 
	 */
	this.handleHyperlink = function(protocol, message) {
		
		var result = message;
		
		var hyperlinkIndices = this.getIndicesOf(protocol, message, false);
		if (hyperlinkIndices.length > 0) {
			var urls = [];

			for(var i = 0; i < hyperlinkIndices.length; i++) {
				var segment = this.getMessageSegment(message, hyperlinkIndices, i);
				var endIdx = segment.indexOf(">");
				var url = segment.substring(1, endIdx);
				urls.push(url);
			}
			
			for (var u = 0; u < urls.length; u++) {
				var url = urls[u];
				message = message.replace("<" + url + ">", "");
				message = message.replace(url, "<a href=\"" + url + "\" target=\"_blank\">" + url + "</a>");		 				
			}
			
			result = message;
		}
		
		return result;
	},
	
	/**
	 * 
	 */
	this.getMessageSegment = function(message, indices, currentIndex) {
		var endIndex = message.length;
		var nextIndex = currentIndex + 1;
		if (nextIndex < indices.length)
			endIndex = indices[nextIndex];
		
		return message.substring(indices[currentIndex], endIndex);
	},
	
	/**
	 * 
	 */
	this.getIndicesOf = function(searchStr, str, caseSensitive) {
	    var startIndex = 0, searchStrLen = searchStr.length;
	    var index, indices = [];
	    if (!caseSensitive) {
	        str = str.toLowerCase();
	        searchStr = searchStr.toLowerCase();
	    }
	    while ((index = str.indexOf(searchStr, startIndex)) > -1) {
	        indices.push(index);
	        startIndex = index + searchStrLen;
	    }
	    return indices;
	}
};
var ParseUtil = new arrowsi.Parser();