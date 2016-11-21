/**
 * Created by Dmitry on 17.11.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('ManagerWaybillDetailsController', ManagerWaybillDetailsController);

    ManagerWaybillDetailsController.$inject = ['Principal', '$uibModalInstance', 'entity', 'Waybill'];

    function ManagerWaybillDetailsController(Principal, $uibModalInstance, entity, Waybill) {
        var vm = this;

        vm.clear = clear;
        vm.waybill = entity;
        vm.confirmWaybill = confirmWaybill;

        getAccount();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.fullName = account.firstName + ' ' + account.lastName;
            });
        }

        function confirmWaybill() {
            vm.waybill.manager = vm.account;
            vm.waybill.routeList = null;
            vm.waybill.state = 'CHECKED';
            Waybill.update(vm.waybill);
            clear();
        }

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }
    }
})();
