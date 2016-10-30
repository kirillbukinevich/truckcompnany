/**
 * Created by Vladimir on 23.10.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('SuperadminCompaniesController', SuperadminCompaniesController);

    SuperadminCompaniesController.$inject = ['$stateParams', 'Company', '$http'];

    function SuperadminCompaniesController($stateParams, Company, $http) {
        var vm = this;


        vm.deleteCompany = deleteCompany;
        vm.deleteSelectedCompany = deleteSelectedCompany;
        vm.toggleStatus = toggleStatus;
        vm.checkedAll = false;
        vm.companies = Company.query();
        vm.selected = [];


        vm.changeStateCheckbox = function () {
            console.log("changeState")
            for (var i in vm.selected){
                vm.selected[i] = vm.checkedAll;
            }
        }

        function deleteCompany(id) {
            console.log(id);
            Company.delete({id: id}, function () {
                console.log("Delete Company with id: " + id);
                vm.companies = Company.query();
            });
        }

        function deleteSelectedCompany() {
            console.log("DELETE SELECTED FUNCTION");
            var idDelete = [];

            for (var i in vm.selected){

                if (vm.selected[i] == true){
                    idDelete.push(vm.companies[i].id);
                }
            }
            if (idDelete.length > 0){
                $http({
                    method: 'POST',
                    url: '/api/companies/deleteArray',
                    data: idDelete,
                }).then(function successCallback(response) {
                    vm.companies = Company.query();
                    vm.selected = [];
                    console.log(vm.companies)
                }, function errorCallback(response) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                });
            }
        }


        function toggleStatus(id) {
            $http({
                method: 'GET',
                url: '/api/change_company_status/' + id,
            }).then(function successCallback(response) {
                console.log("Satus changed");
                for (var i in vm.companies) {
                    if (vm.companies[i].id == id) {
                        if (vm.companies[i].status == 'ACTIVE') {
                            vm.companies[i].status = 'DEACTIVATE'
                        }
                        else {
                            vm.companies[i].status = 'ACTIVE'
                        }
                    }
                }
            }, function errorCallback(response) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
            });
        }


    }
})();
