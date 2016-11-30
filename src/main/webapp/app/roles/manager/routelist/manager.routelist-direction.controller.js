/**
 * Created by Dmitry on 17.11.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('ManagerRoutelistDirectionController', ManagerRoutelistDirectionController);

    ManagerRoutelistDirectionController.$inject = ['$scope', '$stateParams', 'RouteList', 'NgMap', '$timeout', 'Checkpoint', 'Waybill'];

    function ManagerRoutelistDirectionController($scope, $stateParams, RouteList, NgMap, $timeout, Checkpoint, Waybill) {
        var vm = this;

        vm.getDistance = function getDistance() {
            NgMap.getMap().then(function (map) {
                vm.dist = 0;
                angular.forEach(map.directionsRenderers[0].directions.routes[0].legs, function (value) {
                    var strDist = value.distance.value;
                    vm.dist += strDist/1000;
                });
                vm.dist = parseInt(vm.dist);
                console.log(vm.dist);
            });
        };

        $scope.calcDistance = vm.getDistance;
        vm.confirmRoutelist = confirmRoutelist;
        vm.wayPoints = [];

        $scope.render = false;
        $timeout(function () {
            $scope.render = true;
        }, 500);

        vm.waybill = Waybill.get({id: $stateParams.id}, function () {
            vm.checkpoints = Checkpoint.query({id: vm.waybill.routeList.id}, function () {
                var i = 0;
                angular.forEach(vm.checkpoints, function (value) {
                    vm.wayPoints[i] = {location: value.name};
                    i++;
                });
            });
            vm.getDistance();
        });

        $scope.wayPoints = vm.wayPoints;
        $scope.addNewWayPoint = function () {
            $scope.wayPoints.push({'location': ''});
        };

        $scope.removeWayPoint = function () {
            var lastItem = $scope.wayPoints.length - 1;
            $scope.wayPoints.splice(lastItem);
            vm.getDistance();
        };

        function confirmRoutelist() {
            vm.getDistance();
            vm.waybill.routeList.state = 'DELIVERED';
            vm.waybill.routeList.distance = vm.dist;
            console.log(vm.waybill);
            Waybill.update(vm.waybill);
        }
    }
})();
