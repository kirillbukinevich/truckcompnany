/**
 * Created by Dmitry on 17.11.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('ManagerWaybillDetailsController', ManagerWaybillDetailsController);

    ManagerWaybillDetailsController.$inject = ['$filter', 'Principal', 'entity', '$uibModalInstance', 'Waybill', '$http', '$timeout'];

    function ManagerWaybillDetailsController($filter, Principal, entity, $uibModalInstance, Waybill, $http, $timeout) {
        var vm = this;
        vm.clear = clear;
        vm.waybill = entity;
        vm.changeWaybillState = changeWaybillState;
        getAccount();
        vm.now = new Date();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.fullName = account.firstName + ' ' + account.lastName;
            });
        }

        $timeout(function () {
            console.log(vm.waybill);
            angular.forEach(vm.waybill.goods, function (item) {
                if (item.state == 'UNCHECKED') {
                    item.acceptedNumber = item.uncheckedNumber;
                }
            });
        }, 500);

        function changeWaybillState(id) {
            console.log(vm.waybill.dateChecked);

            vm.waybill.dateChecked = new Date();
            vm.waybill.manager = vm.account;
            vm.waybill.routeList = null;

            // if (id == 1) {
            vm.waybill.state = 'CHECKED';
            // } else {
            //     vm.waybill.state = 'REJECTED';
            // }

            angular.forEach(vm.waybill.goods, function (value) {
                value.state = 'ACCEPTED';
            });
            $http.put('/api/goods', vm.waybill.goods);
            Waybill.update(vm.waybill);
            console.log(vm.waybill);

        }

        function clear() {
            $uibModalInstance.close();
        }
    }
})();
