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

        vm.rootUriApp = $location.absUrl().replace(new RegExp("#" + $location.url()), "");


        vm.monthes = [
            {name: 'January', value: 1},
            {name: 'February', value: 2},
            {name: 'March', value: 3},
            {name: 'April', value: 4},
            {name: 'May', value: 5},
            {name: 'June', value: 6},
            {name: 'July', value: 7},
            {name: 'August', value: 8},
            {name: 'September', value: 9},
            {name: 'October', value: 10},
            {name: 'November', value: 11},
            {name: 'December', value: 12}];

        vm.isDefaultBirthday = "true";
        vm.isValidDate = true;

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

        vm.changeInputDate= function(){
            vm.isValidDate = true;
            vm.day = '';
            vm.month = '';
        }

        function loadPage() {
            $http({
                method: 'GET',
                url: '/api/template/employee'
            }).then(function successCallback(response) {
                vm.users = response.data;
            }, function errorCallback(response) {

            });
        }

        $scope.$watch('vm.template.recipient', function (val) {
            if (val != undefined && val.birthDate != null) {
                vm.defaultBirthday = new Date(val.birthDate);
            } else {
                vm.defaultBirthday = undefined;
            }
        });

        function onSuccess(data) {
            vm.users = data;
        }

        function onError() {
            console.log("Error upload employee");
        }

        vm.getTitle = function (user) {
            switch (user.authorities[0]) {
                case "ROLE_DISPATCHER" :
                    return "Dispatcher";
                case "ROLE_DRIVER" :
                    return "Driver";
                case "ROLE_MANAGER":
                    return "Manager";
                case "ROLE_COMPANYOWNER":
                    return "Company owner";
                default:
                    return "Anonymous";
            }
        }


        vm.onReady = function () {

        }
        vm.createTemplate = function () {
            if (vm.isValidDate) {
                console.log(vm.template);
                if (vm.isDefaultBirthday =="true") {
                    vm.template.birthday = moment(vm.defaultBirthday).format('YYYY-MM-DD').toString();
                } else{
                    vm.checkDate();
                    vm.template.birthday = moment(vm.newAssignedBirthday).format('YYYY-MM-DD').toString();
                }
                Template.save(vm.template, onSuccessCreateTemplate, onErrorCreateTemplate);
            }
        }

        vm.checkDate = function () {
            if (vm.day == '' || vm.day > 31 || vm.day < 1 || vm.month == undefined) {
                vm.isValidDate = false;
                return;
            }
            var assignYear = vm.defaultBirthday == undefined ? new Date().getFullYear() : vm.defaultBirthday.getFullYear();
            var date = new Date(assignYear, vm.month.value - 1, vm.day);

            vm.isValidDate = (date.getFullYear() == assignYear) && (date.getDate() == vm.day) && (date.getMonth() == vm.month.value - 1);
            if (vm.isValidDate) vm.newAssignedBirthday = date;
        }

        function onSuccessCreateTemplate() {
            $state.go("admincompany.templates");
        }

        function onErrorCreateTemplate() {
            console.log("Error create template");
        }


    }
})();
