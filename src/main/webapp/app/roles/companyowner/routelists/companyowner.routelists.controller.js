(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('CompanyownerRouteListsController', CompanyownerRouteListsController);


    CompanyownerRouteListsController.$inject = ['RouteList', 'pagingParams', '$state', '$uibModal'];

    function CompanyownerRouteListsController (RouteList, pagingParams, $state) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.transition = transition;
        vm.changeItemsPerPage = changeItemsPerPage;

        vm.itemsPerPage = pagingParams.size;
        vm.availableItemsPerPage = [5, 10, 15, 20];
        vm.page = 1;

       vm.datePicker = {
           startDate: null,
           endDate: null
        };

        vm.datePickerOpts = {
            locale : {
                format: "MMMM D, YYYY",
                customRangeLabel: 'Custom range'
            },
            ranges: {
                'Last week' : [moment().subtract(1, "weeks").startOf("week"),
                    moment().subtract(1, "weeks").endOf("week")],
                'Last month' : [moment().subtract(1, "months").startOf("month"),
                    moment().subtract(1, "months").endOf("month")]
            },
            eventHandlers : {
                'apply.daterangepicker' : function (ev, picker) {
                    $('div[name="datepicker"] span').html(vm.datePicker.startDate.format('MMMM D, YYYY') + ' - '
                        + vm.datePicker.endDate.format('MMMM D, YYYY'));
                    vm.loadPage();
                }
            },
            max: moment()
        };

        vm.loadPage();


        function loadPage() {
            RouteList.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                startDate: !!vm.datePicker.startDate? vm.datePicker.startDate.toISOString() : null,
                endDate: !!vm.datePicker.endDate? vm.datePicker.endDate.toISOString() : null
            }, onSuccess, onError);
        }

        function onSuccess(data, headers){
            vm.error = false;
            vm.routeLists = data;
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.page = pagingParams.page;
        }

        function onError(error){
            vm.error = true;
            vm.messageError = 'Problems with connection.'
        }


        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                size:  vm.itemsPerPage,
            });
        }

        function changeItemsPerPage(){
            $state.transitionTo($state.$current, {
                page: 1,
                size:  vm.itemsPerPage,
            });
        }

    }


})();
