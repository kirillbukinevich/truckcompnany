/**
 * Created by Vladimir on 10.11.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .factory('UserUtilService', UserUtilService);



    function UserUtilService () {

        function getRoleForUI(role){
            switch (role){
                case 'ROLE_SUPERADMIN': return 'Superadmin';
                case 'ROLE_DISPATCHER': return 'Dispatcher';
                case 'ROLE_ADMIN': return 'Admin';
                case 'ROLE_DRIVER': return  'Driver';
                case 'ROLE_MANAGER': return  'Manager';
                case 'ROLE_COMPANYOWNER': return  'Owner';
                default:  return "Anonymous role";
            }
        }

        return {
            getRoleForUI : getRoleForUI,
        };
    }
})();
