/**
 * Created by Vladimir on 23.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('SuperadminCompaniesController', SuperadminCompaniesController);

    SuperadminCompaniesController.$inject = ['$stateParams', 'Company'];

    function SuperadminCompaniesController ($stateParams, Company) {
        var vm = this;
        vm.companies = Company.query();
        console.log(vm.companies);
    }
})();
