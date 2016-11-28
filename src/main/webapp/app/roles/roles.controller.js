/**
 * Created by Vladimir on 30.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('RolesController', RolesController);

    RolesController.$inject = ['Principal', '$state'];

    function RolesController (Principal) {
        var vm = this;

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                var role = (vm.account.authorities != undefined) ? vm.account.authorities[0] : '';
                vm.role = getNameRoleForUser(role);
                console.log(account);
                console.log(vm.role);
            });
        }

        function getNameRoleForUser(role){
            switch (role){
                case 'ROLE_SUPERADMIN': return 'Superadmin';
                case 'ROLE_DISPATCHER': return 'Dispatcher';
                case 'ROLE_ADMIN': return 'Company admin';
                case 'ROLE_DRIVER': return  'Driver';
                case 'ROLE_MANAGER': return  'Manager';
                default:  return "Anonymous role";
            }
        }
    }
})();
