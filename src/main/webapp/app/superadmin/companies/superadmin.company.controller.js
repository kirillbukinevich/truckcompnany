/**
 * Created by Vladimir on 23.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('SuperadminCompanyController', SuperadminCompanyController);

    SuperadminCompanyController.$inject = ['$stateParams', '$state','Company'];

    function SuperadminCompanyController ($stateParams, $state, Company) {
        var vm = this;

        vm.load = load;
        vm.company = {};

        vm.load($stateParams.id);

        function load (id) {
            Company.get({id: id}, function(result) {
                vm.company = result;
                console.log(vm.company);
            });
        }

        vm.update = function(id){
            console.log("Update Company: " + id);
            console.log(vm.company);
            Company.update(
                {id:  vm.company.id,
                 name: vm.company.name},
                function(result){
                    console.log("Update company with id = " + id);
                    $state.go('superadmin.companies');
                }
            )
        }


    }
})();
