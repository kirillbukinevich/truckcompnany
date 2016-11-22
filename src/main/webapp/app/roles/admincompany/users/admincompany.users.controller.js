(function() {
    'use strict';
    angular
        .module('truckCompanyApp')
        .controller('AdmincompanyUsersController', AdmincompanyUsersController);

    AdmincompanyUsersController.$inject = ['$state','$http','pagingParams', 'UserUtilService', 'Employee'];

    function AdmincompanyUsersController ( $state, $http, pagingParams, UserUtilService, Employee) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.transition = transition;
        vm.changeItemsPerPage = changeItemsPerPage;
        vm.toggleStatus = toggleStatus;
        vm.changeStateCheckbox = changeStateCheckbox;
        vm.getRoleForUI =UserUtilService.getRoleForUI;

        vm.users = [];
        vm.checkedAll = false;

        vm.availableItemsPerPage = [2, 10, 20];
        vm.page = 1;
        vm.itemsPerPage = pagingParams.size;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;


        vm.loadPage();


        function loadPage () {
            Employee.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
        }

        function onSuccess(data, headers){
            vm.error = false;
            vm.users = data;
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.page = pagingParams.page;
        }

        function onError(){

        }

        function toggleStatus(user){
            $http({
                method: 'GET',
                url: '/api/user/change_status/' + user.id
            }).then(function() {
                user.activated = !user.activated;
            });
        }


        function changeStateCheckbox() {
            for (var i in vm.selected) {
                vm.selected[i] = vm.checkedAll;
            }
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                size:  vm.itemsPerPage,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
            });
        }

        function changeItemsPerPage(){
            $state.transitionTo($state.$current, {
                page: 1,
                size:  vm.itemsPerPage
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
