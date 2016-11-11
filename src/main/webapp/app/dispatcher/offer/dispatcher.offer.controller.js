/**
 * Created by Viktor Dobroselsky on 04.11.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DispatcherOfferController', DispatcherOfferController);

    DispatcherOfferController.$inject = ['$stateParams', 'Offer'];

    function DispatcherOfferController ($stateParams, Offer) {
        var vm = this;
        vm.offers = Offer.query();
        vm.cancelOffer = cancelOffer;

        console.log(vm.offers);

        function cancelOffer(offer) {
            offer.state = "CANCELED";
            Offer.update(offer);
        }
    }
})();
