(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('CompanyownerWaybillsController', CompanyownerWaybillsController);

    CompanyownerWaybillsController.$inject = ['$stateParams', 'Waybill', 'User'];

    function CompanyownerWaybillsController ($stateParams, Waybill, User) {
        var vm = this;
        vm.waybills = Waybill.query(function(){
            angular.forEach(vm.waybills, function(value){
                value.dispatcher = User.get({login: value.dispatcherLogin});
                value.driver = User.get({login: value.driverLogin});
            });
        });
        console.log(vm.waybills);

    }
})();
