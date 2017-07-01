(function () {
    'use strict';

    angular.module('tpjApp')
            .controller('LaporanPerKapalCtrl', LaporanPackingListCtrl)
    function LaporanPackingListCtrl($scope, KotaService, StuffingService, KapalBerangkatService) {
        $scope.kota = {};
        $scope.kapal = {};
        $scope.open = open;
        $scope.opened = false;
        $scope.format = 'dd-MM-yyyy';
        $scope.tgl1 = new Date();
        $scope.options = {
            showWeeks: false
        };

        function open() {
            $scope.opened = true;
        }
        KotaService.cariSemua().success(function (data) {
            $scope.listKota = data;
        });
        $scope.reloadKapal = function () {
            KapalBerangkatService.filterByIdKotaAndAktif($scope.kota.selected.id).success(function (data) {
                $scope.listKapal = data;
                console.log('listKapal', data);
            });
        };
        $scope.reloadKontainer = function () {
            StuffingService.listStuffingPerKapalBerangkat($scope.kapal.selected.id).success(function (data) {
                $scope.listKontainer = data;
                console.log('listKontainer', data);
            });
        };
        $scope.cetak = function (c, ex, tipe) {
            var link = 'api/report/per-stuffing.' + tipe + '?id=' + c.id + '&ex=' + ex;
            if (tipe == 'pdf') {
//                    window.open(link, '_blank', 'width=screen.width, height=screen.height');
                window.open(link, '_blank', 'width=1024, height=768');
            } else {
                location.href = link;
            }
        }
        $scope.rekapMerk = function (c, tipe) {
            var link = 'api/report/rekap-merk.' + tipe + '?id=' + c.id + '&ex=' + ex;
            if (tipe == 'pdf') {
//                    window.open(link, '_blank', 'width=screen.width, height=screen.height');
                window.open(link, '_blank', 'width=1024, height=768');
            } else {
                location.href = link;
            }
        }
        $scope.$watch('kota.selected', function () {
            $scope.kapal.selected = null;
            $scope.listKapal = [];
            if ($scope.kota.selected != null) {
                $scope.reloadKapal();
            }
        });

        $scope.$watch('kapal.selected', function () {
            $scope.listKontainer = [];
            if ($scope.kapal.selected != null) {
                $scope.reloadKontainer();
            }
        });

    }
})();