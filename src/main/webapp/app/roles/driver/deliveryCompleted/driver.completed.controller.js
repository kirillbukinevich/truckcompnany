(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DriverCompletedController', DriverCompletedController);

    DriverCompletedController.$inject = ['$stateParams', 'Goods1', 'Waybill', 'Checkpoint',
        'RouteList', '$http', '$location', '$scope'];

    function DriverCompletedController($stateParams, Goods1, Waybill, Checkpoint, RouteList, $http, $location, $scope) {
        $scope.sortType = 'name'; // set the default sort type
        $scope.sortReverse = false;  // set the default sort order
        var vm = this;
        vm.routeList = {};
        vm.goods1 = [];


        vm.waybills = [];
        vm.waybill ={};
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
            $http({
                method: 'PUT',
                url: '/api/routelists',
                data: vm.routeList
            });
            $location.path("/driver/routelist");

        };

    }
})();
