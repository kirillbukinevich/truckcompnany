/**
 * Created by Viktor Dobroselsky on 31.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DispatcherDriversController', DispatcherDriversController);

    DispatcherDriversController.$inject = ['$stateParams', 'Driver'];

    function DispatcherDriversController ($stateParams, Driver) {
        var vm = this;
        vm.drivers = Driver.query();
        console.log(vm.drivers);
    }
})();
