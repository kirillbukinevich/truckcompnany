/**
 * Created by Vladimir on 09.11.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('AdmincompanyTemplatesController', AdmincompanyTemplatesController);

    AdmincompanyTemplatesController.$inject = ['$stateParams', '$state', '$scope', 'Upload', '$http', '$location', 'Employee', 'Template'];

    function AdmincompanyTemplatesController($stateParams, $state, $scope, Upload, $http, $location, Employee, Template) {
        var vm = this;

        vm.loadPage = loadPage;

        vm.templates = [];

        vm.loadPage();

        function loadPage(){
            Template.query({}, onSuccess, onError);
        }

        function onSuccess(data){
            console.log("Success");
            console.log(data);
            vm.templates = data;
        }
        function onError(){
            console.log("Error");
        }

    }
})();
