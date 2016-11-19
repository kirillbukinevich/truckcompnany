/**
 * Created by Dmitry on 17.11.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('ManagerWaybillDetailsController', ManagerWaybillDetailsController);

    ManagerWaybillDetailsController.$inject = ['Principal', '$stateParams', '$uibModalInstance', 'entity', 'Waybill'];

    function ManagerWaybillDetailsController(Principal, $stateParams, $uibModalInstance, entity, Waybill) {
        var vm = this;

        vm.clear = clear;
        vm.waybill = entity;

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
            });
        }

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }
    }
})();
