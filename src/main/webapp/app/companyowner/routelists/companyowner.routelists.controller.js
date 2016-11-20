(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('CompanyownerRouteListsController', CompanyownerRouteListsController);


    CompanyownerRouteListsController.$inject = ['RouteList'];

    function CompanyownerRouteListsController (RouteList) {
        var vm = this;
        vm.routeLists = RouteList.query(function(){
            console.log(vm.routeLists);
        });


    }
})();
