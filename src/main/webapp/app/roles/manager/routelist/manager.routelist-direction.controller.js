/**
 * Created by Dmitry on 17.11.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('ManagerRoutelistDirectionController', ManagerRoutelistDirectionController);

    ManagerRoutelistDirectionController.$inject = ['NgMap', '$scope', '$stateParams', '$timeout', 'Checkpoint', 'Waybill', '$http', '$rootScope'];

    function ManagerRoutelistDirectionController(NgMap, $scope, $stateParams, $timeout, Checkpoint, Waybill, $http, $rootScope) {
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
        vm.confirmRoutelist = confirmRoutelist;
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

        vm.addNewWayPoint = function () {
            vm.wayPoints.push({'location': ''});
        };

        vm.removeWayPoint = function (item) {
            vm.wayPoints.splice(item, 1);
            vm.getDistance();
        };

        function confirmRoutelist() {
            vm.getDistance();
            vm.waybill.routeList.state = 'TRANSPORTATION';
            vm.waybill.routeList.distance = vm.dist;

            console.log(vm.waybill);
            console.log(vm.checkpoints);
            console.log(vm.wayPoints);

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
