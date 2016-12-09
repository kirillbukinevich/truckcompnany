/**
 * Created by Dmitry on 12.11.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('ManagerRoutelistController', ManagerRoutelistController);

    ManagerRoutelistController.$inject = ['RouteList', '$scope'];

    function ManagerRoutelistController(RouteList, $scope) {
        var vm = this;
        vm.routelists = RouteList.query();
    }
})();
