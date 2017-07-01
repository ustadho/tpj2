(function () {
    'use strict';

    angular.module('tpjApp')
            .controller('LaporanPerMerkController', PerMerkContoller)
    function PerMerkContoller($scope, KotaService, TokoService, KapalBerangkatService) {
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
        }
        $scope.reloadToko = function () {
            TokoService.listMerkKapalBerangkat($scope.kapal.selected.id).success(function (data) {
                $scope.listToko = data;
                console.log('listToko', data);
            });
        }
        $scope.cetak = function (tipe) {
            var idMerks = "";
            for (var i = 0; i < $scope.listToko.length; i++) {
                if ($scope.listToko[i].terpilih == true) {
                    if (idMerks == "") {
                        idMerks += $scope.listToko[i].id_merk;
                    } else {
                        idMerks += "," + $scope.listToko[i].id_merk;
                    }
                }
            }
            console.log('idMerks', idMerks);
            var link = 'api/report/per-merk-toko.' + tipe + '?id=' + $scope.kapal.selected.id + '&it=' + idMerks;
            if (tipe == 'pdf') {
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
        })

        $scope.$watch('kapal.selected', function () {
            $scope.listToko = [];
            if ($scope.kapal.selected != null) {
                $scope.reloadToko();
            }
        })

    }
})();