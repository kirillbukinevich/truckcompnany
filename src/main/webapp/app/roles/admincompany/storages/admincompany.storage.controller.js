/**
 * Created by Vladimir on 30.10.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('AdmincompanyStorageController', AdmincompanyStorageController);

    AdmincompanyStorageController.$inject = ['$stateParams', '$state', 'Company', 'Upload', '$http'];

    function AdmincompanyStorageController($stateParams, $state, Company, Upload, $http) {
        var vm = this;

        vm.load = load;
        vm.update = update;
        vm.storage = {};
        vm.error = false;
        vm.messageError = '';

        vm.load($stateParams.id);


        function load(id) {
            console.log('AdmincompanyStorageController');
            $http({
                method: 'GET',
                url: '/api/storages/' + id,
            }).then(function successCallback(response) {
                vm.error = false;
                vm.storage = response.data;
                vm.storage.activated = String(vm.storage.activated);
                console.log(vm.storage);
            }, function errorCallback(response) {
                vm.error = true;
                vm.messageError = "Problem with connection."
                console.log("ERROR GET STORAGE")
            });
        }

        function update(){
            console.log("UPDATE")
            $http({
                method: 'PUT',
                url: '/api/storages',
                data: vm.storage
            }).then(function successCallback(response) {
                vm.error = false;
                vm.storage = response.data;
                console.log(vm.storage);
                $state.go('admincompany.storages');
            }, function errorCallback(response) {
                vm.error = true;
                vm.messageError = "Problem with update."
                console.log("ERROR GET STORAGE")
            });
        }


    }
})();
