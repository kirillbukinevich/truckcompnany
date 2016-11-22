(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('AdmincompanyTruckCreateController', AdmincompanyTruckCreateController);

    AdmincompanyTruckCreateController.$inject = ['$state','$http'];

    function AdmincompanyTruckCreateController ($state,  $http) {
        var vm = this;

        vm.create = create;
        vm.truck = {};
        vm.truck.status = "READY";
        vm.truck.type = "BODY";

        function create(){
            $http({
                method: 'POST',
                url: '/api/trucks',
                data: vm.truck
            }).then(function (response) {
                vm.error = false;

                vm.truck = response.data;
                $state.go('admincompany.trucks');
            }, function () {
                console.log("error");
                vm.error = true;
                vm.messageError = "Problem with connection."
            });
        }
    }
})();
