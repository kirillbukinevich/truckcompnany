/**
 * Created by Viktor Dobroselsky on 28.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DispatcherWaybillsController', DispatcherWaybillsController);

    DispatcherWaybillsController.$inject = ['$stateParams', 'Waybill'];

    function DispatcherWaybillsController ($stateParams, Waybill) {
        var vm = this;
        vm.waybills = Waybill.query();
        console.log(vm.waybills);
    }
})();
