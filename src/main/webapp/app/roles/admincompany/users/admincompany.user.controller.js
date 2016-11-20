/**
 * Created by Vladimir on 30.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('AdmincompanyUserController', AdmincompanyUserController);

    AdmincompanyUserController.$inject = ['$stateParams', '$state','Company', 'Upload','$http'];

    function AdmincompanyUserController ($stateParams, $state, Company, Upload, $http) {
        var vm = this;

        vm.load = load;
        vm.update = update;

        vm.dateOptions = {
            maxDate: new Date()
        };
        vm.format = 'yyyy/MM/dd';
        vm.altInputFormats = ['M!/d!/yyyy'];


        vm.openDatePopup = function(){
            vm.isOpenDatePopup = true;
        }

        vm.user = {};
        vm.error = false;

        vm.load($stateParams.id);


        function load (id) {
            $http({
                method: 'GET',
                url: '/api/company/employee/' + id,
            }).then(function successCallback(response) {
                vm.user = response.data;
                vm.user.birthDate = new Date(vm.user.birthDate);
                console.log(vm.user)
            }, function errorCallback(response) {
                console.log("ERROR GET EMPLOYEE")
            });

        }

        function update(){
            console.log("UPDATE")
            $http({
                method: 'PUT',
                url: '/api/company/employee',
                data: vm.user,
            }).then(function successCallback(response) {
                console.log('SUCCESS');
                $state.go('admincompany.users');
            }, function errorCallback(resp) {
                vm.error = true;
                switch(resp.headers('X-truckCompanyApp-error')){
                    case 'error.userexists': {
                        vm.messageError = 'Login already in use';
                        break;
                    }
                    case 'error.emailexists':{
                        vm.messageError = 'Email already in use.';
                        break;
                    }
                }
            });
        }
    }
})();
