
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('CompanyOwnerWaybillWriteOffController', CompanyOwnerWaybillWriteOffController);

    CompanyOwnerWaybillWriteOffController.$inject = ['Principal', 'entity', '$uibModalInstance', 'Waybill', '$http', '$scope'];

    function CompanyOwnerWaybillWriteOffController(Principal, entity, $uibModalInstance, Waybill, $http, $scope) {
        var vm = this;

        vm.clear = clear;
        vm.waybill = entity;
        getAccount();

        console.log(vm.waybill);
        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.fullName = account.firstName + ' ' + account.lastName;
            });
        }


        function clear() {
            $uibModalInstance.close();
        }

    }
})();
