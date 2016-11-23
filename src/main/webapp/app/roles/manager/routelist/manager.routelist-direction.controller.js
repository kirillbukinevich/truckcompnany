/**
 * Created by Dmitry on 17.11.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('ManagerRoutelistDirectionController', ManagerRoutelistDirectionController);

    ManagerRoutelistDirectionController.$inject = ['$scope', '$stateParams', 'Waybill', 'Checkpoint'];

    function ManagerRoutelistDirectionController($scope, $stateParams, Waybill, Checkpoint) {
        var vm = this;
        //vm.routelist = RouteList.query();
        vm.waybill = getWaybill(Waybill, $stateParams);
        console.log(vm.waybill.routeList.id);
        vm.checkpoints = Checkpoint.query({id: 1});
        console.log(vm.checkpoints);


        /*        vm.waybills = Waybill.query(function () {
                    angular.forEach(vm.waybills, function (value) {
                        RouteList.get({id: value.routeList.id}, function (result) {
                            vm.routeList = result;
                            console.log(vm.routeList);
                        });
                        vm.checkpoints = Checkpoint.query({id: value.routeList.id}, function () {
                            var i = 0;
                            angular.forEach(vm.checkpoints, function (value) {
                                console.log(value);
                                console.log("!!!!!" + " " + value.name);
                                vm.checkpointNames[i] = {location: value.name};
                                i++;
                                // {location: 'ozarichi', stopover: true}
                            });
                        });
                    });
                });*/

  /*      $scope.wayPoints = Checkpoint.query({id: value.routeList.id}, function () {
            var i = 0;
            angular.forEach(vm.checkpoints, function (value) {
                console.log(value);
                console.log("!!!!!" + " " + value.name);
                vm.checkpointNames[i] = {location: value.name};
                i++;
            });
        });*/

        function getWaybill(Waybill, $stateParams) {
            return Waybill.get({id: $stateParams.id});
        }

        //vm.wayPoints = Checkpoint.query();

        $scope.wayPoints = [];

        $scope.addNewWayPoint = function () {
            $scope.wayPoints.push({'location': ''});
        };

        $scope.removeWayPoint = function () {
            var lastItem = $scope.wayPoints.length - 1;
            $scope.wayPoints.splice(lastItem);
        };

        vm.wayPoints = $scope.wayPoints;
    }
})();
