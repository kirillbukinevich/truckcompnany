/**
 * Created by Dmitry on 17.11.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('ManagerRoutelistDirectionController', ManagerRoutelistDirectionController);

    ManagerRoutelistDirectionController.$inject = ['$scope', '$stateParams', 'NgMap', '$timeout', 'Checkpoint', 'Waybill', '$http'];

    function ManagerRoutelistDirectionController($scope, $stateParams, NgMap, $timeout, Checkpoint, Waybill, $http) {
        var vm = this;

        vm.getDistance = function getDistance() {
            NgMap.getMap().then(function (map) {
                vm.dist = 0;
                $timeout(function() {
                    angular.forEach(map.directionsRenderers[0].directions.routes[0].legs, function (value) {
                        var strDist = value.distance.value;
                        vm.dist += strDist/1000;
                    });
                    vm.dist = parseInt(vm.dist);
                }, 600);
            });
        };

        $scope.render = false;
        $timeout(function () {
            $scope.render = true;
        }, 500);

        $scope.calcDistance = vm.getDistance;
        vm.confirmRoutelist = confirmRoutelist;
        vm.wayPoints = [];

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
            vm.waybill.routeList.state = 'TRANSPORTATION';
            vm.waybill.routeList.distance = vm.dist;

            console.log(vm.wayPoints);
            console.log(vm.checkpoints);

            var i = 0;
            vm.checkpoints = [];
            angular.forEach(vm.wayPoints, function (value) {
                vm.checkpoints[i] = {name: value.location};
                i++;
            });

            Waybill.update(vm.waybill);
            $http.post('api/checkpoints/' + vm.waybill.routeList.id, vm.checkpoints);
        }
    }
})();
