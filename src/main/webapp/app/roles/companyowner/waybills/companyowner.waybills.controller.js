(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('CompanyownerWaybillsController', CompanyownerWaybillsController);

    CompanyownerWaybillsController.$inject = ['$state', 'Waybill', 'pagingParams', '$uibModal', '$scope'];

    function CompanyownerWaybillsController ($state, Waybill, pagingParams, $uibModal, $scope) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.transition = transition;
        vm.changeItemsPerPage = changeItemsPerPage;

        vm.itemsPerPage = pagingParams.size;
        vm.availableItemsPerPage = [5, 10, 15, 20];
        vm.page = 1;



        vm.loadPage();

        vm.showModalWaybillWriteOff = function (id) {
            vm.modalConfirmWaybill = $uibModal.open({
                templateUrl: 'app/roles/companyowner/waybills/companyowner.waybill-writeoff.html',
                controller: 'CompanyOwnerWaybillWriteOffController',
                controllerAs: 'vm',
                scope: $scope,
                backdrop: 'static',
                size: 'lg',
                resolve: {
                    entity: ['Waybill', function (Waybill) {
                        return Waybill.get({id: id});
                    }]
                }
            });
        };



        function loadPage() {
            Waybill.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
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
