function ListController(paginationConfig, listConfig) {
    this.pagination = new Util.Pagination(paginationConfig);
    this.toolbar = this.toolbar || [];
    angular.extend(this, {
        pageTitle: '',
        pageSubTitle: '',
        resultTitle: '',
        columnHeaders: [],
        tfootTopic: '',
        canAdd: false,
        canEdit: false,
        openFilter: undefined,
        openDetails: undefined
    }, listConfig);

    if (this.openFilter) {
        this.toolbar.push({
            caption: 'Search',
            icon: 'fa fa-search',
            onClick: this.openFilter
        });
    }
    if (this.canAdd) {
        this.toolbar.push({
            caption: 'Add',
            icon: 'fa fa-plus',
            onClick: this.openDetails
        });
    }
    this.tooltipIndex = -1;
}

// this function must be overridden in the controller
ListController.prototype.find = function() {
    console.error('ListController: find function has not been overriden');
};

/**
 * Change the column to sort on and/or toggles ASC and DESC if the column currently set is the same column being passed in
 * @param {String} column
 * @return void
 */
ListController.prototype.sortColumn = function(column) {
    this.pagination.sortByColumn(column);
    this.find();
};
/* Change the column to sort on and/or toggles ASC and DESC if the column currently set is the same column being passed in
 * @param {String} column
 * @return void
 */
ListController.prototype.getHeaderLabel = function (headers, current) {
    var res = '';
    angular.forEach(headers, function (el) {
        if (el.value == current)
            res = el.label;
    });
    return res;
};
/**
 * Change the column to sort on and/or toggles ASC and DESC if the column currently set is the same column being passed in
 * @param {String} column
 * @return void
 */
ListController.prototype.getHeaderLabel = function (headers, current) {
    var res = '';
    angular.forEach(headers, function (el) {
        if (el.value == current)
            res = el.label;
    });
    return res;
};

/**
 * Change the number of items to be displayed and re-queries the last search
 * @param {Number} numberOfItems
 * @return void
 */
ListController.prototype.changeItemsPerPage = function(numberOfItems) {
    if (this.pagination.itemsPerPage != numberOfItems) {
        this.pagination.changeItemsPerPage(numberOfItems);
        this.find();
    }
};

/**
 * Decrease the page index by 1 and re-query the last search
 * @return void
 */
ListController.prototype.previousPage = function() {
    this.pagination.previousPage();
    this.find();
};

/**
 * Set the page index to the passed in pageNumber and re-query the last search
 * @param {Number} pageNumber
 * @return void
 */
ListController.prototype.gotoPage = function(pageNumber) {
    this.pagination.gotoPage(pageNumber);
    this.find();
};

/**
 * Increase the page index by 1 and re-query the last search
 * @return void
 */
ListController.prototype.nextPage = function() {
    this.pagination.nextPage();
    this.find();
};

ListController.prototype.update = function(data) {
    this.pagination.update(data);
};

ListController.prototype.updateItemById = function(id, itemData) {
    for(var i=0; i<this.pagination.content.length; i++) {
        if(this.pagination.content[i].id == id) {
            this.pagination.content[i] = itemData;
            break;
        }
    }
};

ListController.prototype.getCellText = function(item, column) {
    var props = column.value.split('.');
    for(var i=0; i<props.length; i++) {
        if (typeof item != 'object' || item == null) break;
        item = item[props[i]];
    }
    if(column.renderFunc && typeof column.renderFunc == 'function') {
        return column.renderFunc(item);
    }
    return item;
};

ListController.prototype.hideTooltip = function() {
    this.tooltipIndex = -1;
};

ListController.prototype.showTooltip = function(index) {
    this.tooltipIndex = index;
};

ListController.prototype.isTooltipOpen = function(index) {
    return this.tooltipIndex == index;
};
