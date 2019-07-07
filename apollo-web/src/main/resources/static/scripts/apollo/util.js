(function (root, namespace) {
  function Pagination(props) {
    this.reset(props);
  }

  Pagination.prototype.reset = function (props) {
    $.extend(true, this, {
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

  Pagination.prototype.update = function (data) {
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
   * @return pagination
   */
  Pagination.prototype.changeItemsPerPage = function (numberOfItems) {
    this.pageIndex = 0;
    this.itemsPerPage = numberOfItems;
    return this;
  };

  /**
   * Decrease the page index by 1
   * @return pagination
   */
  Pagination.prototype.previousPage = function () {
    this.pageIndex--;
    return this;
  };

  /**
   * Set the page index to the passed in pageNumber
   * @param {Number} pageNumber
   * @return pagination
   */
  Pagination.prototype.gotoPage = function (pageNumber) {
    this.pageIndex = pageNumber;
    return this;
  };

  /**
   * Increase the page index by 1
   * @return pagination
   */
  Pagination.prototype.nextPage = function () {
    this.pageIndex++;
    return this;
  };

  Pagination.prototype.sortByColumn = function (property) {
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

  // exports
  root[namespace] = {
    Pagination: Pagination
  };
})(window, 'Util');