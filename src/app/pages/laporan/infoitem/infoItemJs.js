(function () {
    'use strict';

    angular.module('tpjApp')
            .controller('InfoItemCtrl', InfoItemCtrl);

    function InfoItemCtrl($scope, $window, $uibModal, $filter, SuratJalanService, TokoService, toastr) {
        $scope.paging = {
            currentPage: 1,
            totalItems: 0
        };
        $scope.options = {format: 'DD/MM/YYYY', showClear: false};
        $scope.vm = {tglAwal: new Date(), tglAkhir: new Date(), toko: null, cari: ""};
        TokoService.cariSemua().success(function (data) {
            $scope.listToko = data;
        });
        $scope.reloadData = function () {
            console.log('vm', $scope.vm);
            console.log('tglAwal', $filter('date')(new Date($scope.vm.tglAwal), 'yyyy-MM-dd'));
            $scope.dataPage = SuratJalanService.queryInfoItemComposite($filter('date')(new Date($scope.vm.tglAwal), 'yyyy-MM-dd'), $filter('date')(new Date($scope.vm.tglAkhir), 'yyyy-MM-dd'), ($scope.vm.toko == null || $scope.vm.toko.id == undefined || $scope.vm.toko.id == null ? 0 : $scope.vm.toko.id), ($scope.vm.cari == '' ? 'null' : $scope.vm.cari), $scope.paging.currentPage - 1, function () {
                $scope.paging.maxSize = ($scope.dataPage.size);
                $scope.paging.totalItems = $scope.dataPage.totalElements;
                $scope.paging.currentPage = parseInt($scope.dataPage.number) + 1;
                $scope.paging.maxPage = $scope.dataPage.totalPages;
            });
        };
        $scope.reloadData();

        $scope.edit = function (x) {
            $window.open('#/transaksi/suratjalan/' + x.id, '_blank');
        };

        $scope.hapus = function (x) {
            console.log('hapus', x);
            SuratJalanService.hapus(x.id).success(function (d) {
                toastr.success('Hapus data sukses!');
            });
        };
        
        $scope.cetak = function (c, ex, tipe) {
            var link = 'api/report/per-stuffing.' + tipe + '?id=' + c.id_stuffing + '&ex=' + ex;
            if (tipe == 'pdf') {
//                    window.open(link, '_blank', 'width=screen.width, height=screen.height');
                window.open(link, '_blank', 'width=1024, height=768');
            } else {
                location.href = link;
            }
        };

    }
}
)();