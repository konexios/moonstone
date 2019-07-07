function ListController(paginationConfig, listConfig) {
    this.pagination = new Util.Pagination(paginationConfig);
    this.toolbar = this.toolbar || [];
    this.allItemIds = [];
    angular.extend(this, {
        pageTitle: '',
        pageSubTitle: '',
        resultTitle: '',
        hideResultIcon: false,
        noResultsMsg: null,
        columnHeaders: [],
        tfootTopic: '',
        canAdd: false,
        canEdit: false,
        canBulkEdit: false, // enables or disabled Bulk Edit button
        openFilter: undefined,
        openDetails: undefined,
        buttons: [],
        bulkEdit: false,
        disabledSelectionButton: false,
        selectedItemIds: [],
        btnDropdownTitle: 'Bulk Actions',
        btnDropdownActions: []
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
    if (this.canBulkEdit) {
        this.toolbar.push({
            caption: 'Bulk Edit',
            icon: 'fa fa-clone',
            onClick: this.toggleBulkEditMode.bind(this)
        });
    }
    this.buttons.forEach(function(button) {
        this.toolbar.push(button);
    }, this);
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

ListController.prototype.setColumnHeaders = function(columnHeaders) {
    this.columnHeaders = columnHeaders;
};

ListController.prototype.update = function(data, documentIds) {
    this.pagination.update(data);
    if (!documentIds || !(documentIds instanceof Array) || documentIds.length <= 0) {
        this.allItemIds = [];
        this.selectedItemIds = [];
    } else {
        this.allItemIds = documentIds;
        this.selectedItemIds = this.selectedItemIds.filter(function(id) {
            return documentIds.indexOf(id) >= 0;
        });
    }
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

ListController.prototype.displayState = function(index, column) {
    if (index === 0 && this.canEdit) {
        return 'edit';
    } else if (column.cellAction) {
        return 'action';
    } else {
        return 'text';
    }
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

ListController.prototype.getCaption = function(caption) {
    return typeof caption == 'function' ? caption() : caption;
};

ListController.prototype.isSelected = function(item) {
    return this.selectedItemIds.indexOf(item.id) >= 0;
};

ListController.prototype.toggleSelection = function(item) {
    var index = this.selectedItemIds.indexOf(item.id);
    if (index >= 0) {
        this.selectedItemIds.splice(index, 1);
    } else {
        this.selectedItemIds.push(item.id);
    }
};

ListController.prototype.canSelect = function(item) {
    return true;
};

ListController.prototype.toggleBulkEditMode = function() {
    this.bulkEdit = !this.bulkEdit;
};

ListController.prototype.selectThisPage = function() {
    this.selectedItemIds = this.pagination.content.map(function(item) {
        return item.id;
    });
};

ListController.prototype.selectAllItems = function() {
    this.selectedItemIds = [].concat(this.allItemIds);
};

ListController.prototype.clearSelection = function() {
    this.selectedItemIds = [];
};
