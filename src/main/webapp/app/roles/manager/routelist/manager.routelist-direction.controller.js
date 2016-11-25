/**
 * Created by Dmitry on 17.11.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('ManagerRoutelistDirectionController', ManagerRoutelistDirectionController);

    ManagerRoutelistDirectionController.$inject = ['$scope', '$stateParams', '$http', 'RouteList', 'NgMap'];

    function ManagerRoutelistDirectionController($scope, $stateParams, $http, RouteList, NgMap) {
        var vm = this;
        vm.confirmRoutelist = confirmRoutelist;
        vm.wayPoints = [];
        $http.get('api/waybills/' + $stateParams.id).then(function (data) {
            vm.waybill = data.data;
            vm.checkpoints = $http.get('api/checkpoint/' + vm.waybill.routeList.id).then(function (checkpointArray) {
                console.log(checkpointArray.data);
                console.log(NgMap.getMap());
                var i = 0;
                angular.forEach(checkpointArray.data, function (value) {
                    vm.wayPoints[i] = {location: value.name};
                    i++;
                });
            });
        });

        $scope.wayPoints = vm.wayPoints;
        $scope.addNewWayPoint = function () {
            $scope.wayPoints.push({'location': ''});
        };

        $scope.removeWayPoint = function () {
            var lastItem = $scope.wayPoints.length - 1;
            $scope.wayPoints.splice(lastItem);
        };

        function confirmRoutelist() {
            console.log(vm.waybill.routeList.state);
            vm.waybill.routeList.state = 'DELIVERED';
            RouteList.update(vm.waybill.routeList);
        }
    }
})();
