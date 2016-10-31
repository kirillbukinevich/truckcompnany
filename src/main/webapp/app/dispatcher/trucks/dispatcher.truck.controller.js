/**
 * Created by Viktor Dobroselsky on 29.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DispatcherTrucksController', DispatcherTrucksController);

    DispatcherTrucksController.$inject = ['$stateParams', 'Truck'];

    function DispatcherTrucksController ($stateParams, Truck) {
        var vm = this;
        vm.trucks = Truck.query();
        console.log(vm.trucks);
    }
})();
