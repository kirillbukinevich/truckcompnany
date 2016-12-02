(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$scope', '$state', 'Auth', 'Principal', 'ProfileService', 'LoginService'];

    function NavbarController ($scope, $state, Auth, Principal, ProfileService, LoginService) {
        var vm = this;

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.isAuthenticatedAndNotHasRole = Principal.isWithoutAuthority;
        vm.forwardToControlPanel = forwardToControlPanel;



        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;

        getAccount();


        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                console.log("Navbar")
                console.log(account);
            });
        }

        function login() {
            collapseNavbar();
            LoginService.open();
        }

        function logout() {
            collapseNavbar();
            Auth.logout();
            $state.go('home');
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }


        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        function forwardToControlPanel(){
            console.log("forward")
            console.log(vm.account.authorities[0])
            switch (vm.account.authorities[0]){
                case "ROLE_ADMIN": $state.go('admincompany.initial'); break;
                case "ROLE_SUPERADMIN":$state.go('superadmin.companies'); break;
                case "ROLE_DRIVER":$state.go('driver.routelist'); break;
                case "ROLE_MANAGER":$state.go('manager.initial'); break;
                case "ROLE_COMPANYOWNER":$state.go('companyowner.initial'); break;
                case "ROLE_DISPATCHER":$state.go('dispatcher.initial'); break;
            }
        }
    }
})();
