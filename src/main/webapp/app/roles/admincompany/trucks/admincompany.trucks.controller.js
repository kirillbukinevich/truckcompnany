/**
 * Created by Vladimir on 30.10.2016.
 */

(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('AdmincompanyTrucksController', AdmincompanyTrucksController);

    AdmincompanyTrucksController.$inject = ['$stateParams', '$state','$http'];

    function AdmincompanyTrucksController ($stateParams, $state, $http) {
        var vm = this;

        vm.load = load;
        //vm.toggleStatus = toggleStatus;
        vm.trucks = [];
        vm.error = false;
        vm.messageError = '';
        vm.load();


        function load () {
            console.log('AdmincompanyTrucksController');
            $http({
                method: 'GET',
                url: '/api/trucks'
            }).then(function successCallback(response) {
                vm.error = false;
                vm.trucks = response.data;
                console.log(vm.trucks);
            }, function errorCallback(response) {
                vm.error = true;
                vm.messageError = 'Problems with connection.'
            });
        }

        /*
        function toggleStatus(truck){
            $http({
                method: 'GET',
                url: '/api/storages/change_status/' + storage.id,
            }).then(function successCallback(response) {
                vm.error = false;
                storage.activated = !storage.activated;
            }, function errorCallback(response) {
                vm.error = true;
                vm.messageError = 'Problems occured during status changing.'
            });
        }*/

    }
})();
