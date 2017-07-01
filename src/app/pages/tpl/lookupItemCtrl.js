(function () {
    'use strict';

    angular.module('tpjApp')
            .controller('LookupItemCtrl', LookupItemCtrl)
    function LookupItemCtrl($scope, $timeout, $uibModalInstance, cari, SuratJalanService) {
        $scope.selected = {};
            $scope.listData = [];
            $scope.cari=cari;
            $scope.reloadData = function () {
                SuratJalanService.lookupItem($scope.cari).success(function (data) {
                    $scope.listData = data;
                    $timeout(function () {
                        $('#search').focus();
                    }, 500);
                });
            };
            $scope.reloadData();

            $scope.totalDisplayed = 12;

            $scope.loadMore = function () {
                $scope.totalDisplayed += 10;
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            }

            $scope.modalSelected = function () {
                $uibModalInstance.close($scope.selected);
            };

            $scope.tampilkan = function () {
                alert($scope.query);
            }

            $scope.pilih = function (x) {
                $scope.selected = x;
                $scope.modalSelected();
            }
    }
})();