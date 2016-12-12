/**
 * Created by Dmitry on 11.11.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('ManagerWaybillsController', ManagerWaybillsController);

    ManagerWaybillsController.$inject = ['Waybill', '$uibModal', '$scope', '$location', '$http'];

    function ManagerWaybillsController(Waybill, $uibModal, $scope, $location, $http) {
        var vm = this;

        vm.waybills = Waybill.query(function (data) {
            renameVariables(data);
        });

        function renameVariables(data) {
            console.log(data);
            for (var i = 0; i < data.length; i++) {
                data[i].driverFirstName = data[i].driver.firstName;
                data[i].driverLastName = data[i].driver.lastName;
                data[i].dispatcherFirstName = data[i].dispatcher.firstName;
                data[i].dispatcherLastName = data[i].dispatcher.lastName;
            }
        }

        vm.isModeSearch = false;

        /*        $scope.$on('waybillState', function (event, data) {
         console.log(data);
         vm.waybills[data.id - 1] = data;
         });*/

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
        };

        vm.searchWaybill = function (event) {
            if (vm.searchQuery.trim() == "") {
                vm.isModeSearch = false;
                vm.waybills = Waybill.query(function (data) {
                    renameVariables(data);
                });
            } else {
                vm.isModeSearch = true;
                $http({
                    method: 'GET',
                    url: '/api/_search/waybills/' + vm.searchQuery
                }).then(
                    function (response) {
                        console.log("Success");
                        console.log(response.data);
                        console.log(vm.waybills);
                        vm.waybills = response.data;
                    },
                    function () {
                        console.log("Error")
                    }
                )
            }
        }

    }
})();
