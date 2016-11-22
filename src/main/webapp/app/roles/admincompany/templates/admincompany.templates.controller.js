/**
 * Created by Vladimir on 09.11.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('AdmincompanyTemplatesController', AdmincompanyTemplatesController);

    AdmincompanyTemplatesController.$inject = ['$stateParams', '$state', '$scope', '$http', '$location', 'Template', 'UserUtilService', '$uibModal', 'pagingParams'];

    function AdmincompanyTemplatesController($stateParams, $state, $scope, $http, $location, Template, UserUtilService, $uibModal, pagingParams) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.transition = transition;
        vm.changeItemsPerPage = changeItemsPerPage;

        vm.templates = [];
        vm.checkedAll = false;

        vm.availableItemsPerPage = [2, 10, 20];
        vm.page = 1;
        vm.itemsPerPage = pagingParams.size;

        vm.loadPage();

        function loadPage() {
            Template.query({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage
                },
                onSuccess, onError);
        }

        function onSuccess(data, headers) {

            vm.error = false;
            vm.templates = data;

            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.page = pagingParams.page;
            console.log("total items = " + vm.totalItems)


        }

        function onError() {
            console.log("Error");
        }

        vm.getRole = function (role) {
            return UserUtilService.getRoleForUI(role);
        }

        vm.showModalDeleteTemplate = function (template) {
            vm.selectedTemplate = template;
            vm.modalDelete = $uibModal.open({
                templateUrl: 'app/roles/admincompany/templates/admincompany.deletetemplate.html',
                scope: $scope,
                size: 'sm',
            });
        }

        vm.deleteSelectedTemplates = function () {
            console.log("Delete templates");
            var idDelete = [];

            for (var i in vm.selected) {
                if (vm.selected[i] == true) {
                    idDelete.push(vm.templates[i].id);
                }
            }

            if (idDelete.length > 0) {
                $http({
                    method: 'POST',
                    url: '/api/templates/deleteArray',
                    data: idDelete,
                }).then(function successCallback(response) {
                    console.log("Success")
                    Template.query({}, onSuccess, onError);
                    vm.selected = [];
                    vm.checkedAll = false;
                }, function errorCallback(response) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                });
            }


        }


        $scope.deleteTemplate = function () {
            Template.delete({id: vm.selectedTemplate.id}, function () {
                Template.query({}, onSuccess, onError);
                vm.selected = [];
                vm.checkedAll = false;
            });
            vm.modalDelete.close();
        }

        $scope.closeModalDeleteTemplate = function (template) {
            console.log(template);
            vm.modalDelete.close();
        }

        vm.changeStateCheckbox = function () {
            for (var i in vm.selected) {
                vm.selected[i] = vm.checkedAll;
            }
        }

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


    }
})();
