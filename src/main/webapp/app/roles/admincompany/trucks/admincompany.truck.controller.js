/**
 * Created by Vladimir on 30.10.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('AdmincompanyTruckController', AdmincompanyTruckController);

    AdmincompanyTruckController.$inject = ['$stateParams', '$state', '$http'];

    function AdmincompanyTruckController($stateParams, $state, $http) {
        var vm = this;

        vm.load = load;
        vm.update = update;
        vm.truck = {};
        vm.error = false;
        vm.messageError = '';

        vm.load($stateParams.id);


        function load(id) {
            console.log('AdmincompanyTruckController');
            $http({
                method: 'GET',
                url: '/api/trucks/' + id,
            }).then(function successCallback(response) {
                vm.error = false;
                vm.truck = response.data;
                console.log(vm.truck);
                console.log(moment("2010-01-01T05:06:07", ["YYYY", moment.ISO_8601]).format("YYYY").toString());
            }, function errorCallback(response) {
                vm.error = true;
                vm.messageError = "Problem with connection."
                console.log("ERROR GET STORAGE")
            });
        }

        function update(){
            console.log("UPDATE TRUCK")
            $http({
                method: 'PUT',
                url: '/api/trucks',
                data: vm.truck
            }).then(function successCallback(response) {
                vm.error = false;
                vm.truck = response.data;
                console.log(vm.truck);
                $state.go('admincompany.trucks');
            }, function errorCallback(response) {
                vm.error = true;
                vm.messageError = "Problem with update."
                console.log("ERROR GET STORAGE")
            });
        }


    }
})();
