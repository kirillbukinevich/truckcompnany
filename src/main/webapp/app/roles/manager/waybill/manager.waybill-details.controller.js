/**
 * Created by Dmitry on 17.11.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('ManagerWaybillDetailsController', ManagerWaybillDetailsController);

    ManagerWaybillDetailsController.$inject = ['Principal', 'entity', 'Waybill', '$uibModalInstance'];

    function ManagerWaybillDetailsController(Principal, entity, Waybill, $uibModalInstance) {
        var vm = this;

        vm.clear = clear;
        vm.waybill = entity;
        vm.changeWaybillState = changeWaybillState;

        getAccount();

        console.log(vm.waybill);
        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.fullName = account.firstName + ' ' + account.lastName;
            });
        }

        function changeWaybillState() {
            vm.waybill.manager = vm.account;
            vm.waybill.routeList = null;
            vm.waybill.writeOffAct = null;
            vm.waybill.state = 'CHECKED';
            Waybill.update(vm.waybill);
        }

        function clear() {
            $uibModalInstance.close();
        }
    }
})();
