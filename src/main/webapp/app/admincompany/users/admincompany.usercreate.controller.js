/**
 * Created by Vladimir on 30.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('AdmincompanyUserCreateController', AdmincompanyUserCreateController);

    AdmincompanyUserCreateController.$inject = ['$stateParams', '$state','Company', 'Upload','$http'];

    function AdmincompanyUserCreateController ($stateParams, $state, Company, Upload, $http) {
        var vm = this;
        vm.create = create;


        vm.user = {};
        vm.user.authorities = [];
        vm.user.authorities[0] = 'ROLE_DISPATCHER';
        console.log(vm.user)
        vm.error = false;
        vm.messageError = '';

        function create(){
            console.log("Create new employee");
            console.log(vm.user);
            $http({
                method: 'POST',
                url: '/api/company/employee',
                data: vm.user
            }).then(function successCallback(response) {
                vm.error = false;
                $state.go('admincompany.users');
            }, function errorCallback(response) {
                console.log("Can not create new employee");
                vm.error = true;
                vm.messageError = "Problem with saving new employee."
            });
        }


    }
})();
