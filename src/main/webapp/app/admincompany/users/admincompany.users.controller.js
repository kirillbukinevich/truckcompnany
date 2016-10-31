/**
 * Created by Vladimir on 30.10.2016.
 */

(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('AdmincompanyUsersController', AdmincompanyUsersController);

    AdmincompanyUsersController.$inject = ['$stateParams', '$state','Company', 'Upload','$http'];

    function AdmincompanyUsersController ($stateParams, $state, Company, Upload, $http) {
        var vm = this;

        vm.load = load;
        vm.toggleStatus = toggleStatus;
        vm.users = [];
        vm.load();


        function load () {
            $http({
                method: 'GET',
                url: '/api/company/employee'
            }).then(function successCallback(response) {
                vm.users = response.data;
                console.log(vm.users)
            }, function errorCallback(response) {

            });
        }

        function toggleStatus(user){
            $http({
                method: 'GET',
                url: '/api/user/change_status/' + user.id,
            }).then(function successCallback(response) {
                user.activated = !user.activated;
            });
        }
    }
})();
