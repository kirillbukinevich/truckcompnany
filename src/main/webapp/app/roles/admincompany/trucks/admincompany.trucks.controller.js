/**
 * Created by Vladimir on 30.10.2016.
 */

(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('AdmincompanyTrucksController', AdmincompanyTrucksController);

    AdmincompanyTrucksController.$inject = ['Truck','$state','pagingParams', 'ParseLinks'];

    function AdmincompanyTrucksController (Truck, $state, pagingParams, ParseLinks) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.transition = transition;
        vm.changeItemsPerPage = changeItemsPerPage;


        vm.availableItemsPerPage = [5, 10, 15, 20];
        vm.page = 1;
        vm.itemsPerPage = pagingParams.size;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        console.log(vm.predicate);
        console.log(vm.reverse);

        vm.trucks = [];
        vm.error = false;
        vm.messageError = '';
        vm.loadPage();


        function loadPage () {
            Truck.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);

        }

        function onSuccess(data, headers){
            vm.error = false;
            vm.trucks = data;

            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.page = pagingParams.page;
            vm.users = data;
        }

        function onError(error){
            vm.error = true;
            vm.messageError = 'Problems with connection.'
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                size:  vm.itemsPerPage,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
            });
        }

        function changeItemsPerPage(){
            $state.transitionTo($state.$current, {
                page: 1,
                size:  vm.itemsPerPage,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
            });
        }

        function sort () {
            var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
            if (vm.predicate !== 'id') {
                result.push('id');
            }
            return result;
        }

    }
})();
