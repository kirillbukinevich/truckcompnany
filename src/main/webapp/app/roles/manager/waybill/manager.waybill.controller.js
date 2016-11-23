/**
 * Created by Dmitry on 11.11.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('ManagerWaybillsController', ManagerWaybillsController);

    ManagerWaybillsController.$inject = ['Waybill', '$uibModal', '$scope', '$location'];

    function ManagerWaybillsController(Waybill, $uibModal, $scope, $location) {
        var vm = this;
        vm.waybills = Waybill.query();

        vm.showModalWaybillDetails = function (id) {
            vm.modalConfirmWaybill = $uibModal.open({
                templateUrl: 'app/roles/manager/waybill/manager.waybill-details.html',
                controller: 'ManagerWaybillDetailsController',
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

        $scope.closeModalWaybillDetails = function (id) {
            vm.modalConfirmWaybill.close();
            $location.path('/manager/routelist-for-waybill/' + id);
        }
    }
})();
