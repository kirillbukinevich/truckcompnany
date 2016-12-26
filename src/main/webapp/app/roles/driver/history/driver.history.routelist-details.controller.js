(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DriverHistoryRoutelistDetailsController', DriverHistoryRoutelistDetailsController);

    DriverHistoryRoutelistDetailsController.$inject = ['NgMap', '$scope', '$stateParams', '$timeout', 'Checkpoint', 'Waybill'];

    function DriverHistoryRoutelistDetailsController(NgMap, $scope, $stateParams, $timeout, Checkpoint, Waybill) {
        var vm = this;

        vm.getDistance = function getDistance() {
            NgMap.getMap().then(function (map) {
                vm.map = map;
                vm.dist = 0;
                $timeout(function () {
                    angular.forEach(map.directionsRenderers[0].directions.routes[0].legs, function (value) {
                        var strDist = value.distance.value;
                        vm.dist += strDist / 1000;
                    });
                    vm.dist = parseInt(vm.dist);
                }, 1000);
            });
        };

        $scope.render = false;
        $timeout(function () {
            $scope.render = true;
        }, 500);

        $scope.calcDistance = vm.getDistance;
        vm.wayPoints = [];

        $timeout(function () {
            Waybill.get({id: $stateParams.id}, function (data) {
                vm.waybill = data;
                Checkpoint.query({id: vm.waybill.routeList.id}, function (checkpooint) {
                    vm.checkpoints = checkpooint;
                    var i = 0;
                    angular.forEach(vm.checkpoints, function (value) {
                        vm.wayPoints[i] = {location: value.name};
                        i++;
                    });
                });
                console.log(vm.waybill);
                vm.getDistance();
            });
        }, 1000);
    }
})();
