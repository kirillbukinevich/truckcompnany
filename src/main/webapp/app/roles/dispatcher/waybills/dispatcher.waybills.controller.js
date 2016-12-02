/**
 * Created by Viktor Dobroselsky on 28.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DispatcherWaybillsController', DispatcherWaybillsController);

    DispatcherWaybillsController.$inject = ['$stateParams', 'Waybill', 'pagingParams', '$state'];

    function DispatcherWaybillsController ($stateParams, Waybill, pagingParams, $state) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.transition = transition;
        vm.changeItemsPerPage = changeItemsPerPage;

        vm.availableItemsPerPage = [5, 10, 15, 20];
        vm.page = 1;
        vm.itemsPerPage = pagingParams.size;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;

        vm.waybills = [];

        vm.loadPage();

        function loadPage() {
            Waybill.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage
            }, onSuccess, onError);
        }

        function onSuccess(data, headers){
            vm.error = false;
            vm.waybills = data;
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

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                size:  vm.itemsPerPage,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
