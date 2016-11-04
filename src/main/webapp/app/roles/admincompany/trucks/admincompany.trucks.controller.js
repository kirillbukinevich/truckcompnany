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


        vm.trucks = [];
        vm.error = false;
        vm.messageError = '';
        vm.loadPage();


        function loadPage () {
            Truck.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
            }, onSuccess, onError);

        }

        function onSuccess(data, headers){
            vm.error = false;
            vm.trucks = data;

            /*vm.links = ParseLinks.parse(headers('link'));*/
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.page = pagingParams.page;
            vm.users = data;
        }

        function onError(error){
            // AlertService.error(error.data.message);
            vm.error = true;
            vm.messageError = 'Problems with connection.'
        }

        function transition () {
            console.log('trunsient')
            $state.transitionTo($state.$current, {
                page: vm.page,
                size:  vm.itemsPerPage,
                //search: vm.currentSearch
            });
        }

        function changeItemsPerPage(){
            $state.transitionTo($state.$current, {
                page: 1,
                size:  vm.itemsPerPage,
                //search: vm.currentSearch
            });
        }


    }
})();
