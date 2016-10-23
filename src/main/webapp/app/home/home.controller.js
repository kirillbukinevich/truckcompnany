(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state'];

    function HomeController ($scope, Principal, LoginService, $state) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();

            if (Principal.hasAnyAuthority(["ROLE_ADMIN"])){
                console.log("Route to initial page for Admin");
                $state.go('superadmin.companies');
            }


        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                console.log('HomeController: getAccount():')
                console.log(account);
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });


        }
        function register () {
            $state.go('register');
        }
    }
})();
