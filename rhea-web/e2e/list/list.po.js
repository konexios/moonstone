(function() {
    'use strict';

    var ListPage = function() {

        // page content
        this.content = element(by.css('.content-wrapper-fluid'));
        this.panel = this.content.element(by.css('.panel.panel-content'));
        this.activeUibModal = element(by.css('.modal[modal-render="true"] .modal-dialog'));



        // page header
        this.pageHeader = this.content.element(by.css('.content-title h1'));

        // panel header
        this.panelHeader = this.panel.element(by.css('h3.panel-title'));



        // search button
        this.searchButton = this.content.element(by.css('.content-toolbar button:nth-child(1)'));

        // add button
        this.addButton = this.content.element(by.css('.content-toolbar button:nth-child(2)'));


        // active modal header
        this.activeModalHeader = this.activeUibModal.element(by.css('.modal-header h3.modal-title'));
        this.activeModalCancel = this.activeUibModal.element(by.css('.modal-footer button:nth-child(2)'));
        this.activeModalFirstTab = this.activeUibModal.element(by.css('.modal-header .nav-pills>li:nth-child(1)'));

    };

    module.exports = new ListPage();
})();