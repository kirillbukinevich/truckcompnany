(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DriverHistoryController', DriverHistoryController);


    DriverHistoryController.$inject = ['Waybill', 'pagingParams', '$state', '$http', '$stateParams'];

    function DriverHistoryController (Waybill, pagingParams, $state, $http, $stateParams) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.transition = transition;
        vm.changeItemsPerPage = changeItemsPerPage;

        vm.itemsPerPage = pagingParams.size;
        vm.availableItemsPerPage = [5, 10, 15, 20];
        vm.page = 1;

        vm.loadPage();


        function loadPage() {
           $http({
                method: 'GET',
                url: '/api/waybills/driver_history',
                params: {
                    size: vm.itemsPerPage,
                    page: pagingParams.page - 1,
                }
            }).then(onSuccess,onError)

        }



        function onSuccess(response){
            vm.error = false;
            vm.waybills = response.data;
            console.log(vm.waybills);
            vm.totalItems = response.headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.page = pagingParams.page;
        }

        function onError(error){
            vm.error = true;
            vm.messageError = 'Problems with connection.'
        }


        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                size:  vm.itemsPerPage,
            });
        }

        function changeItemsPerPage(){
            $state.transitionTo($state.$current, {
                page: 1,
                size:  vm.itemsPerPage,
            });
        }


    }


})();
