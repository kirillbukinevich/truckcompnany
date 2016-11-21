/**
 * Created by Dmitry on 17.11.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('ManagerWaybillDetailsController', ManagerWaybillDetailsController);

    ManagerWaybillDetailsController.$inject = ['Principal', '$uibModalInstance', 'entity'];

    function ManagerWaybillDetailsController(Principal, $uibModalInstance, entity) {
        var vm = this;

        vm.clear = clear;
        vm.waybill = entity;

        getAccount();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.fullName = account.firstName + ' ' + account.lastName;
            });
        }

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }
    }
})();
