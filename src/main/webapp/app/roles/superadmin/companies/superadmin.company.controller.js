/**
 * Created by Vladimir on 23.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('SuperadminCompanyController', SuperadminCompanyController);

    SuperadminCompanyController.$inject = ['$stateParams', '$state','Company', 'Upload','$http'];

    function SuperadminCompanyController ($stateParams, $state, Company, Upload, $http) {
        var vm = this;

        vm.load = load;
        vm.saveLogo = saveLogo;
        vm.deleteLogo = deleteLogo;
        vm.update = update;
        vm.company = {};
        vm.admin = {};
        vm.logo = null;
        vm.error = false;
        vm.messageError = '';

        vm.load($stateParams.id);


        function saveLogo(file){
            if (file != null){
                Upload.upload({
                    url: 'api/uploadlogo',
                    data: {
                        file: file,
                        file_name: file.name,
                        company_id: vm.company.id
                    }
                }).then(function (resp) {
                    console.log("Image was saved success!");
                    vm.uploadError = false;
                    vm.imageSource = "/content/upload/logocompany/" +resp.headers('X-truckCompanyApp-params');
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
        }

        function deleteLogo(id){
            console.log("DELETE LOGO FOR COMPANY WITH ID=" + id);
            $http({
                method: 'GET',
                url: '/api/deletelogo/' + id,
            }).then(function successCallback(response) {
                vm.imageSource = "/content/upload/logocompany/";
                vm.error = false;
            }, function errorCallback(response) {

            });
        }

        function load (id) {
            Company.get({id: id}, function(result) {
                vm.company = result;
                console.log(vm.company);
                vm.imageSource = "/content/upload/logocompany/" + vm.company.logo;
            });
        }


        function update(){
            console.log("Update Company: " + vm.company.id);
            console.log(vm.company);
            Company.update(
                {
                    id:  vm.company.id,
                    name: vm.company.name,
                    users: vm.company.users,
                },
                function(result){
                    console.log("Update company with id = " + vm.company.id);
                    $state.go('superadmin.companies');
                },
                function(resp){
                    vm.error = true;
                    switch(resp.headers('X-truckCompanyApp-error')){
                        case 'error.userexists': {
                            vm.messageError = 'Login already in use';
                            break;
                        }
                        case 'error.emailexists':{
                            vm.messageError = 'Email already in use.';
                            break;
                        }
                    }
                }
            )
        }

    }
})();
