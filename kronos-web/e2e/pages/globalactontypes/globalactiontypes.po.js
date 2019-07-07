(function() {
    'use strict';

    var GlobalActionTypesPage = function() {

        this.addActionModal = element(by.css('.modal.in .modal-dialog'));

        this.requiredLabels = this.addActionModal.all(by.cssContainingText('.modal-body .form-group label', '*'));
        this.deisplayedErrors = this.addActionModal.all(by.css('.modal-body .form-group .alert-danger'));
        
        this.errorAlert = this.addActionModal.element(by.css('.modal-body .form-group .alert-danger p[ng-message="required"]'));

        this.submitButton = this.addActionModal.element(by.css('.modal-footer button[type="submit"]'));

    };

    module.exports = new GlobalActionTypesPage();
})();