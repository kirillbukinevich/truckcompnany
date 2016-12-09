/**
 * Created by Viktor Dobroselsky on 04.11.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DispatcherOfferController', DispatcherOfferController);

    DispatcherOfferController.$inject = ['$stateParams', 'Offer', 'pagingParams', '$state', 'Generator'];

    function DispatcherOfferController ($stateParams, Offer, pagingParams, $state, Generator) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.cancelOffer = cancelOffer;
        vm.transition = transition;
        vm.changeItemsPerPage = changeItemsPerPage;
        vm.loadAll = loadAll;

        vm.generateOffer = generateOffer;

        vm.availableItemsPerPage = [5, 10, 15, 20];
        vm.page = 1;
        vm.itemsPerPage = pagingParams.size;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;

        vm.offers = [];
        vm.loadPage();

        function loadPage() {
            Offer.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage
            }, onSuccess, onError);
        }

        function generateOffer() {
            Generator.generate();
            vm.loadPage();
        }

        function onSuccess(data, headers){
            vm.error = false;
            vm.offers = data;
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.page = pagingParams.page;
        }

        function onError(error){
            vm.error = true;
            vm.messageError = 'Problems with connection.'
        }


        function changeItemsPerPage(){
            $state.transitionTo($state.$current, {
                page: 1,
                size:  vm.itemsPerPage,
            });
        }

        console.log(vm.offers);

        function cancelOffer(offer) {
            offer.state = "CANCELED";
            Offer.update(offer);
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                size:  vm.itemsPerPage,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        function sort () {
            var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
            if (vm.predicate !== 'id') {
                result.push('id');
            }
            return result;
        }

        function loadAll () {
            User.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
        }
    }
})();
