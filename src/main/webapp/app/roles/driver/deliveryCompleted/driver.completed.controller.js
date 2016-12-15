(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DriverCompletedController', DriverCompletedController);

    DriverCompletedController.$inject = ['$state', 'Goods1', 'Waybill', 'RouteList', '$http', '$scope', '$timeout'];

    function DriverCompletedController($state, Goods1, Waybill, RouteList, $http, $scope, $timeout) {
        $scope.sortType = 'name'; // set the default sort type
        $scope.sortReverse = false;  // set the default sort order
        var vm = this;
        vm.routeList = {};
        vm.goods1 = [];


        vm.waybills = [];
        vm.waybill = {};
        Waybill.query(function (data) {
            vm.waybills = data;
            var indexDriverGood = 0;
            angular.forEach(vm.waybills, function (value) {
                if (value.state === "CHECKED" || value.routeList.state === "TRANSPORTATION") {
                    vm.waybill = value;
                    RouteList.get({id: value.routeList.id}, function (result) {
                        vm.routeList = result;
                        console.log(vm.routeList.state);
                        console.log(vm.waybill.state);
                    });
                    Goods1.query({id: value.id}, function (data) {
                        vm.goods1 = data;
                    });
                }
            });

        });

        vm.update = function () {
            for (var i in vm.goods1) {

                if (vm.goods1[i] != true) {
                    vm.goods1[i].state = "UNCHECKED_DELIVERED";
                }
            }
            $http({
                method: 'PUT',
                url: '/api/goods',
                data: vm.goods1
            }).then(function successCallback(response) {
                console.log("date changed");

            });
            vm.routeList.state = "DELIVERED";
            vm.routeList.arrivalDate = Date.now();
            $http({
                method: 'PUT',
                url: '/api/routelists',
                data: vm.routeList
            }).then(function successCallback(response) {
                console.log("date changed");
                $state.go('driver.routelist');
            });
        };

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
