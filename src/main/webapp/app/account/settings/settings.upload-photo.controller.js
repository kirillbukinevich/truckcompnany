/**
 * Created by Vladimir on 05.11.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('SettingsUploadPhotoController', SettingsUploadPhotoController);

    SettingsUploadPhotoController.$inject = ['Upload', '$translate', '$http', '$scope', '$uibModal', '$uibModalInstance', 'account', 'parent' ];

    function SettingsUploadPhotoController (Upload,  $translate, $http, $scope, $uibModal, $uibModalInstance, account, parent) {
        var vm = this;
        vm.account = account;
        vm.parent = parent;
        vm.uploadImage = '';
        vm.croppedImage = '';
        vm.isError = false;
        vm.messageError = '';

        vm.back = function(){
            vm.uploadImage = '';
        }

        vm.saveLogo = function(){
            console.log("SAVE LOGO")
            if (vm.croppedImage != ''){
                Upload.upload({
                    url: 'api/users/uploadlogo',
                    data: {
                        file: vm.croppedImage,
                        file_name: 'test.jpg',
                        user_id: vm.account.id
                    }
                }).then(function (resp) {
                    console.log("Image was saved success!");
                    vm.parent.logo = resp.headers('X-truckCompanyApp-params');
                    vm.parent.imageSource = "/content/upload/logouser/" +vm.parent.logo;
                    $uibModalInstance.close();
                }, function (resp) {
                    console.log("Uploading image wasn't successful!");
                });

            }
        }

        vm.addEvent = function(){
            angular.element(document.querySelector('#fileInput')).on('change',handleFileSelect);
        }

        var availableExt = ['JPG', 'PNG', 'BMP'];
        var isAvailableExt = function(nameFile){
            var ext = nameFile.split(".")[1] != undefined ? nameFile.split(".")[1].toUpperCase(): '';
            for (var i in availableExt){
                if (availableExt[i] == ext) {
                    return true;
                }
            }
            return false;
        }

        var handleFileSelect = function(evt) {
            var file = evt.currentTarget.files[0];
            if (isAvailableExt(file.name)) {
                vm.isError = false;
                var reader = new FileReader();
                reader.onload = function (evt) {
                    $scope.$apply(function () {
                        vm.uploadImage = evt.target.result;
                        console.log(vm.uploadImage)
                    });
                };
                reader.readAsDataURL(file);
            } else {
                $scope.$apply(function () {
                    vm.isError = true;
                    vm.messageError = 'Invalid extension for image. Available extension JPG, PNG, BMP';
                });
            }
        }



        vm.close = function(){
            $uibModalInstance.close();
        }


    }
})();
