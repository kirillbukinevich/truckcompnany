
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('CompanyownerRouteListsController', CompanyownerRouteListsController);

    CompanyownerRouteListsController.$inject = ['$stateParams', 'RouteList'];

    function CompanyownerRouteListsController ($stateParams, RouteList) {
        var vm = this;
        vm.routeLists = RouteList.query();
        console.log(vm.routeLists);

    }
})();
