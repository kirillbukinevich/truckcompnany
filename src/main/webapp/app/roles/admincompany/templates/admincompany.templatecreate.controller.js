/**
 * Created by Vladimir on 30.10.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('AdmincompanyTemplateCreateController', AdmincompanyTemplateCreateController);

    AdmincompanyTemplateCreateController.$inject = ['$stateParams', '$state', '$scope', 'Upload', '$http', '$location', 'Employee', 'Template'];

    function AdmincompanyTemplateCreateController($stateParams, $state, $scope, Upload, $http, $location, Employee, Template) {
        var vm = this;

        vm.template = {};
        vm.loadPage = loadPage;

        vm.rootUriApp = $location.absUrl().replace(new RegExp("#" +$location.url()), "");

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

        vm.loadPage();

       function loadPage(){
            Employee.query({}, onSuccess, onError);
        }

        function onSuccess(data){
            vm.users = data;
        }
        function onError(){
            console.log("Error upload employee");
        }

        vm.getTitle = function(user){
            switch(user.authorities[0]){
                case "ROLE_DISPATCHER" : return "Dispatcher";
                case "ROLE_DRIVER" : return "Driver";
                case "ROLE_MANAGER": return "Manager";
                case "ROLE_COMPANYOWNER": return "Company owner";
                default: return "Anonymous";
            }
        }


        vm.onReady = function () {

        }
        vm.createTemplate = function () {
            console.log(vm.template);
            Template.save(vm.template, onSuccessCreateTemplate, onErrorCreateTemplate);
        }

        function onSuccessCreateTemplate(){
            $state.go("admincompany.templates");
        }
        function onErrorCreateTemplate(){
            console.log("Error create template");
        }


    }
})();
