/**
 * Created by Dmitry on 17.11.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('ManagerRoutelistDirectionController', ManagerRoutelistDirectionController);

    ManagerRoutelistDirectionController.$inject = ['$scope', '$stateParams', '$http', 'RouteList', 'NgMap', '$timeout'];

    function ManagerRoutelistDirectionController($scope, $stateParams, $http, RouteList, NgMap, $timeout) {
        var vm = this;

        vm.getDistance = function getDistance() {
            NgMap.getMap().then(function (map) {
                vm.dist = 0;
                angular.forEach(map.directionsRenderers[0].directions.routes[0].legs, function (value) {
                    var strDist = value.distance.value;
                    vm.dist += strDist/1000;
                });
                vm.dist = vm.dist.toFixed(1);
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


        $http.get('api/waybills/' + $stateParams.id).then(function (data) {
            vm.waybill = data.data;
            vm.checkpoints = $http.get('api/checkpoint/' + vm.waybill.routeList.id).then(function (checkpointArray) {
                var i = 0;
                angular.forEach(checkpointArray.data, function (value) {
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
            console.log(vm.waybill.routeList.state);
            vm.waybill.routeList.state = 'DELIVERED';
            RouteList.update(vm.waybill.routeList);
        }
    }
})();
