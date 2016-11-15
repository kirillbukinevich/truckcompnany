/**
 * Created by Dmitry on 11.11.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('ManagerWaybillsController', ManagerWaybillsController);

    ManagerWaybillsController.$inject = ['$stateParams', 'Waybill'];

    function ManagerWaybillsController($stateParams, Waybill) {
        var vm = this;
        vm.waybills = Waybill.query();
    }
})();
