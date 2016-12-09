/**
 * Created by Dmitry on 12.11.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('ManagerRoutelistController', ManagerRoutelistController);

    ManagerRoutelistController.$inject = ['RouteList'];

    function ManagerRoutelistController(RouteList) {
        var vm = this;
        vm.routeLists = RouteList.query();
    }
})();
