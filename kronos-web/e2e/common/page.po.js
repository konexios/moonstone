(function() {
    'use strict';

    // abstract page
    var Page = function() {

        // active modal
        this.activeUibModal = element(by.css('.modal.in .modal-dialog'));   //[modal-render="true"]

        this.activeModalHeader = this.activeUibModal.element(by.css('.modal-header h3.modal-title'));
        this.activeModalCancel = this.activeUibModal.element(by.css('.modal-footer button:nth-child(2)'));

        this.firstAppLink = this.activeUibModal.element(by.css('.modal-body .panel ul.list-group a:nth-child(1)'));

        this.errorModal = element(by.css('.modal#errorModal'));
    };

    module.exports = new Page();
})();