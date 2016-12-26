(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DriverGoodsController', DriverGoodsController);

    DriverGoodsController.$inject = ['Waybill', '$scope','$timeout','entity'];

    function DriverGoodsController(Waybill, $scope,$timeout,entity) {
        $scope.sortType = 'name'; // set the default sort type
        $scope.sortReverse = false;  // set the default sort order
        var vm = this;
        vm.goods1 = [];

        vm.waybill  = entity;

        // $timeout(function () {
        //     console.log(vm.waybill.id);
        // }, 2500);




        vm.updateState = function () {
            if (vm.waybill.state === "CHECKED") {
                console.log(vm.waybill.id);
                Waybill.get({id: vm.waybill.id}, function (data) {
                    vm.waybill = data;
                });
            }
        };

        vm.checkJob = function checkJob() {
            for (var j in vm.waybills) {
                if (vm.waybills[j].state === "CHECKED" || vm.routeList.state === "TRANSPORTATION") {
                    return false;
                }
            }
            return true;
        }
    }
})();
