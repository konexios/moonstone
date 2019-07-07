(function() {
    'use strict';

    var ListPage = function() {

        this.content = element(by.css('.content-wrapper-fluid'));
        this.activeUibModal = element(by.css('.modal[modal-render="true"] .modal-dialog'));

        this.panel = this.content.element(by.css('.panel.panel-content'));
        this.pageHeader = this.content.element(by.css('.content-title h1'));
        this.panelHeader = this.panel.element(by.css('h3.panel-title'));
        this.firstActionButton = this.content.element(by.css('.content-toolbar button:nth-child(1)'));
        //var toolbar = this.content.element(by.model('vm.toolbar').first());
        this.getButtonByText = function(text) {
            return this.content.element(by.cssContainingText('.content-toolbar .btn', text));
        };


        // active modal header
        this.activeModalHeader = this.activeUibModal.element(by.css('.modal-header h3.modal-title'));
        this.activeModalCancel = this.activeUibModal.element(by.css('.modal-footer button:nth-child(2)'));
        this.activeModalFirstTab = this.activeUibModal.element(by.css('.modal-header .nav-pills>li:nth-child(1)'));

    };

    module.exports = new ListPage();
})();