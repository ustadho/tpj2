(function () {
    'use strict';

    angular.module('tpjApp')
            .controller('lookupKapalBerangkatCtrl', LookupKapalBerangkatCtrl)
    function LookupKapalBerangkatCtrl($scope, param, KapalBerangkatService, $timeout, $uibModalInstance) {
        $scope.selected = {};
        console.log('LookupMerkCtrl --> param', param);
        $scope.listData = [];
        $scope.reloadData = function () {
            KapalBerangkatService.filterByIdKotaAndAktif(param.kota.id).success(function (d) {
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
        };

        $scope.modalSelected = function () {
            $uibModalInstance.close($scope.selected);
        };

        $scope.tampilkan = function () {
            alert($scope.query);
        };

        $scope.pilih = function (x) {
            $scope.selected = x;
            $scope.modalSelected();
        };
    }
})();