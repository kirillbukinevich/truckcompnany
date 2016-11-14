/**
 * Created by Viktor Dobroselsky on 14.11.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DispatcherWaybillDetailsController', DispatcherWaybillDetailsController);

    DispatcherWaybillDetailsController.$inject = ['$stateParams', '$uibModalInstance', 'entity', 'Waybill'];

    function DispatcherWaybillDetailsController ($stateParams, $uibModalInstance, entity, Waybill) {
        var vm = this;

        vm.clear = clear;
        vm.waybill = entity;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        console.log(vm.waybill);
    }
})();
