(function () {
    'use strict';

    angular.module('tpjApp')
            .controller('KapalCtrl', KapalCtrl)
            .controller('KapalModalController', KapalModalController);

    /** @ngInject */
    function KapalCtrl($scope, $uibModal, $log, toastr, KapalService) {
        $scope.search = "";
        $scope.oldSearch = "";
        $scope.paging = {
            currentPage: 1,
            totalItems: 0
        };

        $scope.reloadData = function () {
            $scope.dataPage = KapalService.query($scope.search, $scope.paging.currentPage-1, function () {
                $scope.paging.maxSize = ($scope.dataPage.size);
                $scope.paging.totalItems = $scope.dataPage.totalElements;
                $scope.paging.currentPage = parseInt($scope.dataPage.number) + 1;
                $scope.paging.maxPage = $scope.dataPage.totalPages;
            });
        };

        $scope.reloadData();

        $scope.baru = function () {
            console.log('Baru');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/master/kapal/kapal-modal.html',
                controller: 'KapalModalController',
                size: 'md',
                resolve: {
                    data: function () {
                        return {};
                    }
                }
            });
            modalInstance.result.then(function (selectedItem) {
                $scope.reloadData();
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

        $scope.edit = function (x, page, size) {
            console.log('Open modal');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: page,
                controller: 'KapalModalController',
                size: size,
                resolve: {
                    data: function () {
                        return x;
                    }
                }
            });
            modalInstance.result.then(function (selectedItem) {
                $scope.reloadData();
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

        $scope.hapus = function (x) {
            KapalService.hapus(x).success(function () {
                toastr.success('Hapus data sukses!');
                $scope.reloadData();
            });
        };
    }

    function KapalModalController($uibModalInstance, toastr, $scope, KapalService, data) {
        $scope.ori = angular.copy(data);
        $scope.modalTitle = "Edit Kapal";
        console.log('edit', data);
        $scope.vm = angular.copy(data);


        $scope.simpan = function () {
            KapalService.simpan($scope.vm, $scope.ori).success(function (d) {
                $uibModalInstance.close($scope.vm);
                toastr.success('Simpan data sukses!');
            })
        }
    }
})();

