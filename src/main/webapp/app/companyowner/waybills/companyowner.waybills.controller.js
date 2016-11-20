(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('CompanyownerWaybillsController', CompanyownerWaybillsController);

    CompanyownerWaybillsController.$inject = ['$stateParams', 'Waybill', 'User'];

    function CompanyownerWaybillsController ($stateParams, Waybill, User) {
        var vm = this;
        vm.waybills = Waybill.query(function(){
            console.log(vm.waybills);
        });


    }
})();
