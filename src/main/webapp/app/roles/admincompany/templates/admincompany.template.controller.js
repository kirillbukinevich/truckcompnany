/**
 * Created by Vladimir on 09.11.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('AdmincompanyTemplateController', AdmincompanyTemplateController);

    AdmincompanyTemplateController.$inject = ['$stateParams', '$state', '$scope', 'Upload', '$http', '$location', 'Employee', 'Template'];

    function AdmincompanyTemplateController($stateParams, $state, $scope, Upload, $http, $location, Employee, Template) {
        var vm = this;

        vm.loadPage = loadPage;

        vm.options = {
            language: 'en',
            allowedContent: true,
            entities: false,
            filebrowserUploadUrl: vm.rootUriApp + "api/template/upload",
            toolbarGroups: [
                {name: 'undo'},
                {name: 'insert'},
                {name: 'forms'},
                {name: 'basicstyles', groups: ['basicstyles', 'cleanup']},
                {name: 'paragraph', groups: ['list', 'blocks']},
                {name: 'styles'},
                {name: 'colors'}]
        };

        vm.loadPage($stateParams.id);

        function loadPage(id){
            Template.get({id: id}, onSuccess, onError);
        }
        function onSuccess(data){
            vm.template = data;
            Employee.query({}, onSuccessUploadEmployee, onErrorUploadEmployee);
        }
        function onError(){
            console.log("Error")
        }
        function onSuccessUploadEmployee(data){
            vm.users = data;
        }
        function onErrorUploadEmployee(){

        }
    }
})();
