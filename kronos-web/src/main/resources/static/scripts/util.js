(function(root, namespace) {
    function Pagination(props) {
        this.reset(props);
    }

    Pagination.prototype.reset = function(props) {
        angular.merge(this, {
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
        }, props);
        return this;
    };

    Pagination.prototype.update = function(data) {
        this.first = data.first;
        this.last = data.last;
        this.totalPages = data.totalPages;
        this.totalItems = data.totalElements;
        this.content = data.content;
        return this;
    };

    /**
     * Change the number of items to be displayed
     * @param {Number} numberOfItems
     * @return Pagination
     */
    Pagination.prototype.changeItemsPerPage = function(numberOfItems) {
        this.pageIndex = 0;
        this.itemsPerPage = numberOfItems;
        return this;
    };

    /**
     * Decrease the page index by 1
     * @return Pagination
     */
    Pagination.prototype.previousPage = function() {
        this.pageIndex--;
        return this;
    };

    /**
     * Set the page index to the passed in pageNumber
     * @param {Number} pageNumber
     * @return Pagination
     */
    Pagination.prototype.gotoPage = function(pageNumber) {
        this.pageIndex = pageNumber;
        return this;
    };

    /**
     * Increase the page index by 1
     * @return Pagination
     */
    Pagination.prototype.nextPage = function() {
        this.pageIndex++;
        return this;
    };

    Pagination.prototype.sortByColumn = function(property) {
        if (this.sort.property === property) {
            // change sort direction
            this.sort.direction = (this.sort.direction === "ASC" ? "DESC" : "ASC");
        } else {
            // change sort field
            this.sort.property = property;
            // change sort direction
            this.sort.direction = "ASC";
        }
        // reset page index
        this.pageIndex = 0;
        return this;
    };

    // getTimezone
    function getTimezone() {
        var tz = null;
        try {
            tz = Intl.DateTimeFormat().resolvedOptions().timeZone;
        } catch(e) {
            try {
                // fallback with GMT offset
                var offset = (new Date()).getTimezoneOffset(); // in minutes
                if (offset == 0) {
                    tz = "GMT";
                } else {
                    var sign = offset > 0 ? "-" : "+"; // must reverse
                    offset = Math.abs(offset);
                    var hours = Math.floor(offset/60);
                    hours = (hours < 10 ? "0" : "") + hours;
                    var mins = offset%60;
                    mins = (mins < 10 ? "0" : "") + mins;
                    tz = "GMT"+sign+hours+":"+mins;
                }
            } catch(e) {
                // ignore, use server default timezone (UTC)
            }
        }
        return tz;
    }

    // exports
    root[namespace] = {
        Pagination: Pagination,
        getTimezone: getTimezone
    };
})(window, 'Util');
