/**
 * Created by Dmitry on 12.11.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('ManagerRoutelistsController', ManagerRoutelistsController);

    ManagerRoutelistsController.$inject = ['$stateParams', 'RouteList'];

    function ManagerRoutelistsController($stateParams, RouteList) {
        var vm = this;
        vm.routelists = RouteList.query();
    }
})();
