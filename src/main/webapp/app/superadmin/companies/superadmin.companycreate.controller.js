/**
 * Created by Vladimir on 23.10.2016.
 */
/**
 * Created by Vladimir on 23.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('SuperadminCompanyCreateController', SuperadminCompanyCreateController);

    SuperadminCompanyCreateController.$inject = ['$state', 'Company'];

    function SuperadminCompanyCreateController ($state, Company) {
        var vm = this;
        vm.company = {};
        vm.admin = {};

        vm.create = function(){
            console.log("Create new company with name = " + vm.company.name);
            console.log(vm.admin);
            Company.save(
                {
                    name : vm.company.name,
                    login: vm.admin.login,
                    email: vm.admin.email,
                    password: vm.admin.password
                },
                function () {
                    console.log("Create new company");
                    $state.go('superadmin.companies');
            })
        }



    }
})();
