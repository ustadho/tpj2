(function () {
    'use strict';

    angular
            .module('tpjApp', [
                'ngAnimate',
                'ui.bootstrap',
                'ui.sortable',
                'ui.router',
                'ui.select',
                'ngSanitize',
                'ngTouch',
                'toastr',
                'smart-table',
                'xeditable',
                'ui.slimscroll',
                'ngJsTree',
                'angular-progress-button-styles',
                'ngResource',
                'ngBootbox',
                'ae-datetimepicker',
                'ngStorage',
                'ngCookies',
                'ngCacheBuster',
            ])
            .run(run)
            .directive('ngConfirm', ['$uibModal', function ($uibModal) {
                    return {
                        restrict: 'A',
                        scope: {
                            ngConfirmMessage: '@',
                            ngConfirm: '&',
                            ngNotConfirm: '&'
                        },
                        link: function (scope, element) {
                            element.bind('click', function () {
                                var modalInstance = $uibModal.open({
                                    template: '<div class="modal-header"><h4 class="modal-title">{{confirmMessage}}</h3></div><div class="modal-footer"><button class="btn btn-primary" type="button" ng-click="ok()">Ya</button><button class="btn btn-warning" type="button" ng-click="cancel()">Tidak</button></div>',
                                    controller: 'ModalConfirmCtrl',
                                    size: 'md',
                                    windowClass: 'confirm-window',
                                    resolve: {
                                        confirmClick: function () {
                                            return scope.ngConfirm;
                                        },
                                        notConfirmClick: function () {
                                            return scope.ngNotConfirm;
                                        },
                                        confirmMessge: function () {
                                            return scope.ngConfirmMessage;
                                        }
                                    }
                                });
                            });
                        }
                    }
                }])
            .controller('ModalConfirmCtrl', ['$scope', '$uibModalInstance', 'notConfirmClick', 'confirmClick', 'confirmMessge',
                function ($scope, $uibModalInstance, notConfirmClick, confirmClick, confirmMessge) {
                    $scope.confirmMessage = confirmMessge;
                    function closeModal() {
                        $uibModalInstance.dismiss('cancel');
                    }

                    $scope.ok = function () {
                        confirmClick();
                        closeModal();
                    }

                    $scope.cancel = function () {
                        notConfirmClick();
                        closeModal();
                    }
                }]);
    ;

    run.$inject = ['stateHandler'];

    function run(stateHandler) {
        stateHandler.initialize();
    }
})();
