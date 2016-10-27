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
        vm.logo = null;
        vm.uploadLogoError = false;
        vm.messageLogoError = '';

        vm.load($stateParams.id);

        function deleteLogo(){
            console.log('deleting')
            $http({
                method: 'GET',
                url: '/api/deletelogo/' + vm.company.id,
            }).then(function successCallback(response) {
                console.log('Logo delete.')
            }, function errorCallback(response) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
            });
        }

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
                    console.log("Image was saved success!")
                    vm.imageSource = "/api/showlogo/" + vm.company.id + "?cash=" + Math.random();
                }, function (resp) {
                    console.log('Error status: ' + resp.status);
                    vm.uploadLogoError = true;

                    switch(resp.headers('X-truckCompanyApp-error')){
                        case 'error.imageinvalidformat' :{
                            vm.messageLogoError = 'You can image upload file with extension: JPG, PNG, BMP, GIF, TIF only.';
                            break;
                        }
                        case 'error.uploadimageproblem':{
                            console.log("uploadimageproblem");
                            break;
                        }
                    }
                });


            }
        }

        function deleteLogo(id){

        }

        function load (id) {
            Company.get({id: id}, function(result) {
                vm.company = result;
                console.log(vm.company);
                vm.imageSource = "/api/showlogo/" + vm.company.id + "?cash=" + Math.random();
            });
        }


        function update(){
            console.log("Update Company: " + vm.company.id);
            Company.update(
                {
                    id:  vm.company.id,
                    name: vm.company.name
                },
                function(result){
                    console.log("Update company with id = " + vm.company.id);
                    $state.go('superadmin.companies');
                }
            )
        }

    }
})();
