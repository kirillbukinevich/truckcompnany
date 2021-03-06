(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state'];

    function HomeController($scope, Principal, LoginService, $state) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function () {
            getAccount();
            console.log("authenticationSuccess");
            if (Principal.hasAnyAuthority(["ROLE_SUPERADMIN"])) {
                console.log("Route to initial page for SuperAdmin");
                $state.go('superadmin.companies');
            } else if (Principal.hasAnyAuthority(["ROLE_ADMIN"])) {
                console.log("Route to initial page for Admin");
                $state.go('admincompany.initial');
            } else if (Principal.hasAnyAuthority(["ROLE_DISPATCHER"])) {
                console.log("Route to initial page for Dispatcher");
                $state.go('dispatcher.initial');
            } else if (Principal.hasAnyAuthority(["ROLE_COMPANYOWNER"])) {
                console.log("Route to initial page for CompanyOwner");
                $state.go('companyowner.initial');
            } else if (Principal.hasAnyAuthority(["ROLE_DRIVER"])) {
                console.log("Route to initial page for driver");
                $state.go('driver.routelist');
            } else if (Principal.hasAnyAuthority(["ROLE_MANAGER"])) {
                console.log("Route to initial page for manager");
                $state.go('manager.waybills');
            }
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function (account) {
                console.log('HomeController: getAccount():');
                console.log(account);
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }

        function register() {
            $state.go('register');
        }
    }
})();
