(function () {
    'use strict';

    angular.module('tpjApp')
            .controller('LookupTokoCtrl', LookupTokoCtrl)
    function LookupTokoCtrl($scope, $uibModalInstance, param, TokoService, $timeout) {
        $scope.selected = {};
        console.log('LookupTokoCtrl --> param', param)
        $scope.listData = [];
        $scope.cari = param.cari;
        $scope.reloadData = function () {
            TokoService.lookupToko($scope.cari).success(function (d) {
                console.log('data', d);
                $scope.listData = d;
                $timeout(function () {
                    $('#cari').focus();
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