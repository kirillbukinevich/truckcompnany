/**
 * Created by Viktor Dobroselsky on 04.11.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DispatcherOfferDetailsController', DispatcherOfferDetailsController);

    DispatcherOfferDetailsController.$inject = ['$stateParams', '$uibModalInstance', 'entity', 'Offer'];

    function DispatcherOfferDetailsController ($stateParams, $uibModalInstance, entity, Offer) {
        var vm = this;

        vm.clear = clear;
        vm.offer = entity;
        console.log(vm.offer);
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
    }
})();
