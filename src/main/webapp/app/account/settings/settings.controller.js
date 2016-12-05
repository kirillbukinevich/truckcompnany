(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('SettingsController', SettingsController);

    SettingsController.$inject = ['Principal', 'Auth', 'Upload','JhiLanguageService', '$translate', '$http', '$scope', '$uibModal' ];

    function SettingsController (Principal, Auth, Upload, JhiLanguageService, $translate, $http, $scope, $uibModal) {
        var vm = this;

        vm.error = null;
        vm.save = save;
        /*vm.saveLogo = saveLogo;*/
        vm.deleteLogo = deleteLogo;
        vm.settingsAccount = null;
        vm.success = null;




        vm.openModalForUploadPhoto = function(){
            $uibModal.open({
                templateUrl: 'app/account/settings/settings.upload-photo.dialog.html',
                controller: 'SettingsUploadPhotoController',
                controllerAs: 'vm',
                scope : $scope,
                bindToController : true,
                resolve: {
                    account: function(){
                        return vm.settingsAccount;
                    },
                    parent: function(){
                        return vm;
                    }
                }
            });
        }





        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */
        var copyAccount = function (account) {
            return account;
            /*return {
                activated: account.activated,
                email: account.email,
                firstName: account.firstName,
                langKey: account.langKey,
                lastName: account.lastName,
                login: account.login,
                logo: account.logo,
                id : account.id,
            };*/
        };

        Principal.identity().then(function(account) {
            vm.settingsAccount = copyAccount(account);
            console.log(account);
            vm.imageSource = "/content/upload/logouser/" + account.logo;
        });

        function save () {
            vm.settingsAccount.logo = vm.logo;
            console.log(vm.settingsAccount);
            Auth.updateAccount(vm.settingsAccount).then(function() {
                vm.error = null;
                vm.success = 'OK';
                Principal.identity(true).then(function(account) {
                    vm.settingsAccount = copyAccount(account);
                });
                JhiLanguageService.getCurrent().then(function(current) {
                    if (vm.settingsAccount.langKey !== current) {
                        $translate.use(vm.settingsAccount.langKey);
                    }
                });
            }).catch(function() {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }
/*
        function saveLogo(file){
            if (file != null){
                Upload.upload({
                    url: 'api/users/uploadlogo',
                    data: {
                        file: file,
                        file_name: file.name,
                        user_id: vm.settingsAccount.id
                    }
                }).then(function (resp) {
                    console.log("Image was saved success!");
                    vm.uploadError = false;
                    vm.imageSource = "/content/upload/logouser/" +resp.headers('X-truckCompanyApp-params');
                    console.log(vm.imageSource);
                }, function (resp) {
                    console.log("Uploading image wasn't successful!");
                    vm.uploadError = true;

                    switch(resp.headers('X-truckCompanyApp-error')){
                        case 'error.imageinvalidformat' :{
                            vm.messageError = 'You can image upload file with extension: JPG, PNG, BMP, GIF, TIF only.';
                            break;
                        }
                        case 'error.uploadimageproblem':{
                            vm.messageError = 'Downloading file is not successful.';
                            break;
                        }
                        default: {
                            vm.messageError = 'Image size is too large.';
                        }
                    }
                });

            }
        }*/

        function deleteLogo(){
            console.log("DElete logo");
            vm.imageSource = "/content/upload/logouser/";
            vm.logo = null;

            /*$http({
                method: 'GET',
                url: '/api/users/deletelogo/' + vm.settingsAccount.id,
            }).then(function successCallback(response) {
                vm.imageSource = "/content/upload/logouser/";
                vm.error = false;
            }, function errorCallback(response) {

            });*/
        }
    }
})();
