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
        vm.rootUriApp = $location.absUrl().replace(new RegExp("#" + $location.url()), "");
        vm.loadPage = loadPage;

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

        vm.loadPage($stateParams.id);

        function loadPage(id) {
            Template.get({id: id}, onSuccessLoadTemplate, onError);
        }

        function getMonth(number){

            for (var i in vm.monthes){
                if (vm.monthes[i].value == number) return vm.monthes[i];
            }
        }

        function onSuccessLoadTemplate(data) {
            vm.template = data;
            console.log(vm.template)


            vm.defaultBirthday = new Date(vm.template.recipient.birthDate);
            var assignedDateTemplate = new Date(vm.template.birthday);
            vm.isDefaultBirthday = (vm.defaultBirthday.getDate() == assignedDateTemplate.getDate() && vm.defaultBirthday.getMonth() == assignedDateTemplate.getMonth())? "true" : "false";
            if (vm.isDefaultBirthday == "false"){
                vm.day = assignedDateTemplate.getDate();
                vm.month = getMonth(assignedDateTemplate.getMonth() + 1);
            }

            $http({
                method: 'GET',
                url: '/api/template/employee/' + vm.template.recipient.id
            }).then(function successCallback(response) {
                vm.users = response.data;
            }, function errorCallback(response) {

            });




        }

        function onError() {
            console.log("Error")
        }

        function onSuccessUploadEmployee(data) {
            vm.users = data;
        }

        function onErrorUploadEmployee() {

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

        vm.uploadTemplate = function(){
            if (vm.isValidDate) {
                if (vm.isDefaultBirthday =="true") {
                    vm.template.birthday = moment(vm.defaultBirthday).format('YYYY-MM-DD').toString();
                } else{
                    vm.checkDate();
                    vm.template.birthday = moment(vm.newAssignedBirthday).format('YYYY-MM-DD').toString();
                }
                Template.update(vm.template, onSuccessUploadTemplate)
            }
        }

        function onSuccessUploadTemplate(data, headers){
            console.log("Update success");
            console.log(data);
            $state.go("admincompany.templates");
        }

        vm.changeInputDate= function(){
            vm.isValidDate = true;
            vm.day = '';
            vm.month = '';
        }
    }
})();
