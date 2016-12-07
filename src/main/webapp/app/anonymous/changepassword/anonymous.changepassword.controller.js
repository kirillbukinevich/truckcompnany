/**
 * Created by Vladimir on 27.10.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('ChangePassword', ChangePassword);

    ChangePassword.$inject = ['$stateParams', '$http', 'Auth', '$state'];

    function ChangePassword($stateParams, $http, Auth, $state) {
        var vm = this;
        vm.changePassword = changePassword;
        vm.key = $stateParams.key;
        vm.isError = false;

        //Auth.logout();


        $http({
            method: 'GET',
            url: '/api/isvalidkey/' + vm.key,
        }).then(function successCallback(response) {
            vm.isValidKey = true;
            console.log(vm.isValidKey)
        }, function errorCallback(response) {
            vm.isValidKey = false;
            console.log(vm.isValidKey)
        });


        function changePassword() {
            if (vm.password != vm.confirmpassword) {
                vm.isError = true;
            } else {
                $http({
                    method: 'POST',
                    url: '/api/change_inital_password',
                    data: {
                        key: vm.key,
                        newPassword: vm.password,
                    }
                }).then(function successCallback(response) {
                    console.log("Evrethisn is good. Password was changed.");
                    Auth.logout();
                    $state.go("home");
                }, function errorCallback(response) {

                });
            }
        }


        console.log($stateParams.key);


    }
})();
