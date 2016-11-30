/**
 * Created by Vladimir on 23.10.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('SuperadminErrorsController', SuperadminErrorsController);

    SuperadminErrorsController.$inject = ['$stateParams', '$http', '$state', '$uibModal', '$scope'];

    function SuperadminErrorsController($stateParams, $http, $state, $uibModal, $scope) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.sendAgain = sendAgain;
        //vm.deleteError = deleteError;

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
        vm.checkedAll = false;
        vm.selected = [];


        vm.error = false;
        vm.messageError = '';
        vm.loadPage();

        function loadPage() {
            console.log("Attention")
            vm.selected = [];
            vm.checkedAll = false;
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

        vm.sendEmails = function () {
            $http({
                method: "GET",
                url: "/api/templates/sendbirthdaycards"
            }).then(
                function () {
                    console.log("Task was executed");
                }, function () {
                    console.log("Error")
                });
        }

        function sendAgain(idError, event) {
            console.log(event.currentTarget)
            event.currentTarget.setAttribute('disabled', 'disabled');
            $http({
                method: "GET",
                url: "/api/templates/sendagain/" + idError
            }).then(
                function () {
                    console.log("Message send again success");
                    loadPage();
                }, function () {
                    console.log("Error");
                    event.currentTarget.removeAttribute('disabled');
                });
        }

        $scope.deleteError = function()  {
            $http({
                method: "DELETE",
                url: "/api/templates/errors/" + vm.selectedError.id
            }).then(
                function () {
                    loadPage();
                }, function () {
                    console.log("Error")
                });
            vm.modalDelete.close();
        }

        $scope.deleteErrors = function () {
            console.log("Delete errors");
            var idDelete = [];


            for (var i in vm.selected) {
                if (vm.selected[i] == true) {
                    idDelete.push(vm.mailErrors[i].id);
                }
            }

            if (idDelete.length > 0) {
                $http({
                    method: 'POST',
                    url: '/api/templates/errors/deleteArray',
                    data: idDelete,
                }).then(function successCallback(response) {
                    /*var page = (idDelete.length == vm.selected.length) && (pagingParams.page == Math.ceil(vm.totalItems / vm.itemsPerPage)) ? pagingParams.page - 2 : pagingParams.page - 1;
                    Storage.query({
                        page: page < 0 ? 0 : page,
                        size: vm.itemsPerPage
                    }, onSuccess, onError);*/
                    loadPage();
                }, function errorCallback(response) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                });
            }
            vm.modalDelete.close();
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
         }*/

        vm.changeStateCheckbox = function () {
            for (var i in vm.selected) {
                vm.selected[i] = vm.checkedAll;
            }
        }

        vm.showModalDeleteError = function (error) {
            $scope.deleteOneError = true;
            vm.selectedError = error;
            vm.modalDelete = $uibModal.open({
                templateUrl: 'app/roles/superadmin/errors/superadmin.modaldeleteerror.html',
                scope: $scope,
                size: 'sm',
            });
        }
        vm.showModalDeleteErrors = function () {
            $scope.deleteOneError = false;
            vm.modalDelete = $uibModal.open({
                templateUrl: 'app/roles/superadmin/errors/superadmin.modaldeleteerror.html',
                scope: $scope,
                size: 'sm',
            });
        }

        $scope.closeModalDelete = function () {
            vm.modalDelete.close();
        }



        /*
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
