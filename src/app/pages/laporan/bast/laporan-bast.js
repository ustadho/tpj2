(function () {
    'use strict';

    angular.module('tpjApp')
            .controller('LaporanBastController', LaporanBastController)
    function LaporanBastController($scope, $http, KotaService, TokoService, KapalBerangkatService) {
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
        $scope.reloadBast = function () {
            $http.get('api/report/list-bast/'+$scope.kapal.selected.id).success(function (data) {
                $scope.listToko = data;
                console.log('listToko', data);
            });
        }
        $scope.cetak = function (c, tipe) {
            var link = 'api/report/bast.' + tipe + '?k=' + $scope.kapal.selected.id + '&m=' + c.id_merk+'&e='+c.id_emkl;
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
                $scope.reloadBast();
            }
        })

    }
})();