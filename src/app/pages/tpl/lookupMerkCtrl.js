(function () {
    'use strict';

    angular.module('tpjApp')
            .controller('LookupMerkCtrl', LookupMerkCtrl)
    function LookupMerkCtrl($scope, $uibModalInstance, param, TokoService, $timeout) {
        $scope.selected = {};
        console.log('LookupMerkCtrl --> param', param)
        $scope.listData = [];
        $scope.cari = param.merk;
        $scope.reloadData = function () {
            TokoService.lookupMerk(param.kota.id, $scope.cari).success(function (d) {
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