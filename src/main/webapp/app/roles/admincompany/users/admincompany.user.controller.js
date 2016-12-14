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

        var maxDate = moment(new Date()).subtract(1, "d").toDate();

        vm.dateOptions = {
            maxDate: maxDate,
            initDate: maxDate,
        };

        vm.format = 'dd/MM/yyyy';
        vm.altInputFormats = ['d!/M!/yyyy'];


        vm.openDatePopup = function(){
            vm.isOpenDatePopup = true;
        }

        vm.user = {};
        vm.error = false;

        vm.load($stateParams.id);


        function load (id) {
            console.log("Load: " + moment("1995-12-25").format("YYYY-mm-dd"));
            $http({
                method: 'GET',
                url: '/api/company/employee/' + id,
            }).then(function successCallback(response) {
                vm.user = response.data;
                var birthday = moment(vm.user.birthDate);
                console.log(vm.user.birthDate);
                vm.user.birthDate =new Date(birthday.year(), birthday.month(), birthday.date());
            }, function errorCallback(response) {
                console.log("ERROR GET EMPLOYEE")
            });

        }

        function update(){
            console.log("UPDATE")
            vm.user.birthDate = moment(vm.user.birthDate).format('YYYY-MM-DD').toString();
            console.log(vm.user);

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
