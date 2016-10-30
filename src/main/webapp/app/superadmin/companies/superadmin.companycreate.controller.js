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
        vm.logo = null;
        vm.error = false;



        vm.create = function(){
            console.log("Create new company with name = " + vm.company.name);

            vm.company['users'] = [vm.admin];
            Company.save(
                vm.company,
                function () {
                    console.log("Create new company");
                    $state.go('superadmin.companies');
                },
                function(resp){
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
                        default: {
                            vm.messageError = 'You don\'t fill all fields.';
                        }
                    }
                });
        }



    }
})();
