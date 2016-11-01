(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('CompanyownerRouteListsController', CompanyownerRouteListsController);


    CompanyownerRouteListsController.$inject = ['$stateParams', 'RouteList', 'Storage', 'Truck', 'Waybill'];

    function CompanyownerRouteListsController ($stateParams, RouteList, Storage, Truck, Waybill) {
        var vm = this;
        vm.routeLists = RouteList.query(function(){
            angular.forEach(vm.routeLists, function(value){
                value.leavingStorage = Storage.get({id: value.leavingStorageId});
                value.arrivalStorage = Storage.get({id: value.arrivalStorageId});
                value.truck = Truck.get({id: value.truckId});
                value.waybill = Waybill.get({id: value.waybillId});
            });
        });
        console.log(vm.routeLists);

    }
})();
