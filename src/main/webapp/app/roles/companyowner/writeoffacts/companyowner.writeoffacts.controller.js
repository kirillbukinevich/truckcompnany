(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('CompanyownerWriteOffActsController', CompanyownerWriteOffActsController);

    CompanyownerWriteOffActsController.$inject = ['$http'];

    function CompanyownerWriteOffActsController ($http) {
        var vm = this;

        vm.loadData = loadData;

        vm.loadData();

        function loadData(){
            $http({
                method: 'GET',
                url: '/api/waybills/with_stolen_goods',
                /*params: {
                 startDate: !!vm.datePicker.startDate? vm.datePicker.startDate.toISOString() : null,
                 endDate: !!vm.datePicker.endDate? vm.datePicker.endDate.toISOString() : null
                 }*/
            }).then(function successCallback(response) {
                console.log("Data load successfully");
                vm.waybills = response.data;
            }, function errorCallback(response) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
            });
        }


    }
})();
