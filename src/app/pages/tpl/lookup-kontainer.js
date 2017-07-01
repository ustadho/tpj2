(function () {
    'use strict';

    angular.module('tpjApp')
            .controller('LookupKontainerController', LookupKontainerController)
    function LookupKontainerController($scope, $timeout, $uibModalInstance, KontainerService) {
        $scope.selected = {};
        $scope.oldSearch = "";
        $scope.paging = {
            currentPage: 1,
            totalItems: 0
        };
        $scope.reloadData = function () {
            $scope.dataPage = KontainerService.query($scope.cari, $scope.paging.currentPage - 1, function () {
                $scope.paging.maxSize = ($scope.dataPage.size);
                $scope.paging.totalItems = $scope.dataPage.totalElements;
                $scope.paging.currentPage = parseInt($scope.dataPage.number) + 1;
                $scope.paging.maxPage = $scope.dataPage.totalPages;
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