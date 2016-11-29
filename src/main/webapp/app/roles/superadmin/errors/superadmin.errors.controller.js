/**
 * Created by Vladimir on 23.10.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('SuperadminErrorsController', SuperadminErrorsController);

    SuperadminErrorsController.$inject = ['$stateParams', '$http', '$state'];

    function SuperadminErrorsController($stateParams, $http, $state) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.sendAgain = sendAgain;
        vm.deleteError = deleteError;

        /*vm.deleteCompany = deleteCompany;
         vm.deleteSelectedCompany = deleteSelectedCompany;
         vm.toggleStatus = toggleStatus;
         vm.transition = transition;
         vm.changeItemsPerPage = changeItemsPerPage;

         vm.checkedAll = false;

         vm.availableItemsPerPage = [5, 10, 15, 20];
         vm.page = 1;
         vm.itemsPerPage = pagingParams.size;
         */
        vm.mailErrors = [];
        vm.selected = [];

        vm.error = false;
        vm.messageError = '';
        vm.loadPage();

        function loadPage() {
            console.log("Attention")
            $http({
                method: "GET",
                url: "/api/templates/errors"
            }).then(onSuccess, onError);
        }

        function onSuccess(response) {
            vm.error = false;
            vm.mailErrors = response.data;
            console.log(vm.mailErrors)

            /*
             vm.totalItems = headers('X-Total-Count');
             vm.queryCount = vm.totalItems;
             vm.page = pagingParams.page;*/
        }

        function onError(error) {
            vm.error = true;
            vm.messageError = 'Problems with connection.'
        }

        function sendAgain(idError) {
            $http({
                method: "GET",
                url: "/api/templates/sendagain/" + idError
            }).then(
                function () {
                    console.log("Message send again success");
                }, function () {
                    console.log("Error")
                });
        }

        function deleteError(idError){
            $http({
                method: "DELETE",
                url: "/api/templates/errors/" + idError
            }).then(
                function () {
                    loadPage();
                }, function () {
                    console.log("Error")
                });
        }


        /*
         function transition () {
         $state.transitionTo($state.$current, {
         page: vm.page,
         size:  vm.itemsPerPage,
         });
         }

         function changeItemsPerPage(){
         $state.transitionTo($state.$current, {
         page: 1,
         size:  vm.itemsPerPage,
         });
         }

         vm.changeStateCheckbox = function () {
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
         }*/


    }
})();
