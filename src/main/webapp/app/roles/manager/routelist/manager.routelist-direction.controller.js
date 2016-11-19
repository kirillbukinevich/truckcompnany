/**
 * Created by Dmitry on 17.11.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('ManagerRoutelistsDirectionController', ManagerRoutelistsDirectionController);

    ManagerRoutelistsDirectionController.$inject = ['$stateParams', 'RouteList'];

    function ManagerRoutelistsDirectionController($stateParams, RouteList) {
        var vm = this;
        vm.routelists = RouteList.query();
        vm.wayPoints = [{
            location: 'Минск',
            stopover: true
        }, {
            location: 'Витебск',
            stopover: true
        }];

        console.log("OLOLO");
    }
})();
