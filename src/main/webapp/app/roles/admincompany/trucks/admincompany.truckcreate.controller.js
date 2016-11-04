/**
 * Created by Vladimir on 30.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('AdmincompanyTruckCreateController', AdmincompanyTruckCreateController);

    AdmincompanyTruckCreateController.$inject = ['$stateParams', '$state','$http'];

    function AdmincompanyTruckCreateController ($stateParams, $state,  $http) {
        var vm = this;

        vm.create = create;
        vm.truck = {};

        function create(){
            $http({
                method: 'POST',
                url: '/api/trucks',
                data: vm.truck,
            }).then(function successCallback(response) {
                vm.error = false;
                vm.truck = response.data;
                console.log(vm.truck);
            }, function errorCallback(response) {
                vm.error = true;
                vm.messageError = "Problem with connection."
                console.log("ERROR POST TRUCK")
            });
        }
    }
})();
