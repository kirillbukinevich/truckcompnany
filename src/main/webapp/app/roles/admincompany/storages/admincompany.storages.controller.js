/**
 * Created by Vladimir on 30.10.2016.
 */

(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('AdmincompanyStoragesController', AdmincompanyStoragesController);

    AdmincompanyStoragesController.$inject = ['$stateParams', '$state','Company', 'Upload','$http'];

    function AdmincompanyStoragesController ($stateParams, $state, Company, Upload, $http) {
        var vm = this;

        vm.load = load;
        vm.toggleStatus = toggleStatus;
        vm.storages = [];
        vm.error = false;
        vm.messageError = '';
        vm.load();


        function load () {
            console.log('AdmincompanyStoragesController');
            $http({
                method: 'GET',
                url: '/api/storages'
            }).then(function successCallback(response) {
                vm.error = false;
                vm.storages = response.data;
                console.log(vm.storages);
            }, function errorCallback(response) {
                vm.error = true;
                vm.messageError = 'Problems with connection.'
            });
        }

        function toggleStatus(storage){
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
        }

    }
})();
