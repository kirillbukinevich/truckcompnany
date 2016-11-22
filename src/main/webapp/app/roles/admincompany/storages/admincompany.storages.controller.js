/**
 * Created by Vladimir on 30.10.2016.
 */

(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('AdmincompanyStoragesController', AdmincompanyStoragesController);

    AdmincompanyStoragesController.$inject = ['$stateParams', '$state', 'Company', 'Upload', '$http', 'pagingParams', '$uibModal', '$scope', 'Storage'];

    function AdmincompanyStoragesController($stateParams, $state, Company, Upload, $http, pagingParams, $uibModal, $scope, Storage) {
        var vm = this;


        vm.load = load;
        vm.toggleStatus = toggleStatus;
        vm.transition = transition;
        vm.changeItemsPerPage = changeItemsPerPage;



        vm.checkedAll = false;
        vm.selected = [];
        vm.searchQuery = "";
        vm.isModeSearch = false;

        vm.storages = [];
        vm.error = false;
        vm.messageError = '';

        vm.availableItemsPerPage = [2, 10, 20];
        vm.page = 1;
        vm.itemsPerPage = pagingParams.size;
        vm.itemsPerPage = pagingParams.size;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;

        vm.load();

        function load() {
            Storage.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
        }

        function onSuccess(data, headers) {
            vm.error = false;
            vm.storages = data;

            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.page = pagingParams.page;
            console.log("total items = " + vm.totalItems)

        }

        function onError() {
            console.log("Error");
        }

        function toggleStatus(storage) {
            $http({
                method: 'GET',
                url: '/api/storages/change_status/' + storage.id,
            }).then(function successCallback(response) {
                vm.error = false;
                storage.activated = !storage.activated;
            }, function errorCallback(response) {
                vm.error = true;
                vm.messageError = 'Problems occured during status changing.'
            });
        }

        vm.search = function () {
            console.log(vm.searchQuery);
            $http({
                method: 'GET',
                url: '/api/_search/storages/' + vm.searchQuery
            }).then(
                function () {
                    console.log("Success")
                },
                function () {
                    console.log("Error")
                }
            )
        }

        vm.searchStorage = function (event) {

            if (event.which === 13) {
                if (vm.searchQuery.trim() == "") {
                    vm.isModeSearch = false;
                    Storage.query({
                        page: pagingParams.page - 1,
                        size: vm.itemsPerPage
                    }, onSuccess, onError);
                } else {
                    vm.isModeSearch = true;
                    $http({
                        method: 'GET',
                        url: '/api/_search/storages/' + vm.searchQuery
                    }).then(
                        function (response) {
                            console.log("Success");
                            console.log(response.data);
                            vm.storages = response.data;
                        },
                        function () {
                            console.log("Error")
                        }
                    )
                }
            }


        }

        vm.changeStateCheckbox = function () {
            for (var i in vm.selected) {
                vm.selected[i] = vm.checkedAll;
            }
        }

        vm.showModalDeleteStorage = function (storage) {
            vm.selectedStorage = storage;
            $scope.deleteOneStorage = true;
            vm.modalDelete = $uibModal.open({
                templateUrl: 'app/roles/admincompany/storages/admincompany.modaldeletestorage.html',
                scope: $scope,
                size: 'sm',
            });
        }

        vm.showModalDeleteStorages = function () {
            $scope.deleteOneStorage = false;
            vm.modalDelete = $uibModal.open({
                templateUrl: 'app/roles/admincompany/storages/admincompany.modaldeletestorage.html',
                scope: $scope,
                size: 'sm',
            });
        }

        $scope.closeModalDelete = function () {
            vm.modalDelete.close();
        }

        $scope.deleteStorage = function () {
            Storage.delete({id: vm.selectedStorage.id}, function () {
                Storage.query({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage
                }, onSuccess, onError);
                vm.selected = [];
                vm.checkedAll = false;
            });
            vm.modalDelete.close();
        }


        $scope.deleteStorages = function () {
            console.log("Delete storages");
            var idDelete = [];


            for (var i in vm.selected) {
                if (vm.selected[i] == true) {
                    idDelete.push(vm.storages[i].id);
                }
            }

            if (idDelete.length > 0) {
                $http({
                    method: 'POST',
                    url: '/api/storages/deleteArray',
                    data: idDelete,
                }).then(function successCallback(response) {
                    var page = (idDelete.length == vm.selected.length) && (pagingParams.page == Math.ceil(vm.totalItems / vm.itemsPerPage)) ? pagingParams.page - 2 : pagingParams.page - 1;
                    Storage.query({
                        page: page < 0 ? 0 : page,
                        size: vm.itemsPerPage
                    }, onSuccess, onError);
                    vm.selected = [];
                    vm.checkedAll = false;
                }, function errorCallback(response) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                });
            }
            vm.modalDelete.close();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                size: vm.itemsPerPage,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
            });
        }

        function changeItemsPerPage() {
            $state.transitionTo($state.$current, {
                page: 1,
                size: vm.itemsPerPage,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')
            });
        }

        function sort () {
            var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
            if (vm.predicate !== 'id') {
                result.push('id');
            }
            return result;
        }

    }
})();
