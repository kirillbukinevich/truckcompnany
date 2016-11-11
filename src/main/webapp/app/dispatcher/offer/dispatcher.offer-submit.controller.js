/**
 * Created by Viktor Dobroselsky on 09.11.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DispatcherOfferSubmitController', DispatcherOfferSubmitController);

    DispatcherOfferSubmitController.$inject = ['$stateParams', 'Offer', 'Driver', 'Truck'];

    function DispatcherOfferSubmitController ($stateParams, Offer, Driver, Truck) {
        var vm = this;

        vm.offer = Offer.get({id : $stateParams.id});
        vm.drivers = Driver.query();
        vm.driver = {};
        vm.trucks = Truck.query();

        console.log(vm.offer);
    }
})();
