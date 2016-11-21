/**
 * Created by Dmitry on 17.11.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('ManagerRoutelistsDirectionController', ManagerRoutelistsDirectionController);

    ManagerRoutelistsDirectionController.$inject = ['$scope', 'RouteList', '$stateParams', 'Waybill'];

    function ManagerRoutelistsDirectionController($scope, RouteList, $stateParams, Waybill) {
        var vm = this;
        vm.routelist = RouteList.query();
        vm.waybill = getWaybill(Waybill, $stateParams);


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
