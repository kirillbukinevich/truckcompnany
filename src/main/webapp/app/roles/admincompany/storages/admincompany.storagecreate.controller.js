/**
 * Created by Vladimir on 30.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('AdmincompanyStorageCreateController', AdmincompanyStorageCreateController);

    AdmincompanyStorageCreateController.$inject = ['$stateParams', '$state','Company', 'Upload','$http'];

    function AdmincompanyStorageCreateController ($stateParams, $state, Company, Upload, $http) {
        var vm = this;

        vm.create = create;
        vm.storage = {};
        vm.storage.activated = "true";

        function create(){
            $http({
                method: 'POST',
                url: '/api/storages',
                data: vm.storage,
            }).then(function successCallback(response) {
                vm.error = false;
                vm.storage = response.data;
                console.log(vm.storage);
            }, function errorCallback(response) {
                vm.error = true;
                vm.messageError = "Problem with connection."
                console.log("ERROR POST STORAGE")
            });
        }
    }
})();
