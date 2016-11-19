/**
 * Created by Vladimir on 30.10.2016.
 */

(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('AdmincompanyStoragesController', AdmincompanyStoragesController);

    AdmincompanyStoragesController.$inject = ['$stateParams', '$state','Company', 'Upload','$http','pagingParams'];

    function AdmincompanyStoragesController ($stateParams, $state, Company, Upload, $http, pagingParams) {
        var vm = this;

        vm.load = load;
        vm.toggleStatus = toggleStatus;
        vm.storages = [];
        vm.error = false;
        vm.messageError = '';
        vm.load();

        vm.availableItemsPerPage = [2, 10, 20];
        vm.page = 1;
        vm.itemsPerPage = pagingParams.size;


        function load () {
            console.log('AdmincompanyStoragesController');
            $http({
                method: 'GET',
                url: '/api/storages'
            }).then(function successCallback(response) {
                vm.error = false;
                vm.storages = response.data;
                console.log(vm.storages);
            }, function errorCallback(response) {
                vm.error = true;
                vm.messageError = 'Problems with connection.'
            });
        }

        function toggleStatus(storage){
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

        vm.search = function(){
            console.log(vm.searchQuery);
            $http({
                method : 'GET',
                url: '/api/_search/storages/' + vm.searchQuery
            }).then(
                function (){
                    console.log("Success")
                },
                function(){
                    console.log("Error")
                }
            )
        }

        vm.searchStorage = function(event){

            if (event.which === 13){
                $http({
                    method : 'GET',
                    url: '/api/_search/storages/' + vm.searchQuery
                }).then(
                    function (response){
                        console.log("Success");
                        console.log(response.data);
                        vm.storages = response.data;
                    },
                    function(){
                        console.log("Error")
                    }
                )
            }


        }

    }
})();
