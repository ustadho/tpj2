(function () {
    'use strict';

    angular.module('tpjApp')
            .controller('JmlContainerBulananCtrl', JmlContainerBulananCtrl);

    function JmlContainerBulananCtrl($scope, $window, $uibModal, $filter, toastr) {

        $scope.vm = {tahun: null, bulan: null};
        $scope.listBulan = [
            {value: 1, name: 'Januari'},
            {value: 2, name: 'Februari'},
            {value: 3, name: 'Maret'},
            {value: 4, name: 'April'},
            {value: 5, name: 'Mei'},
            {value: 6, name: 'Juni'},
            {value: 7, name: 'Juli'},
            {value: 8, name: 'Agustus'},
            {value: 9, name: 'September'},
            {value: 10, name: 'Oktober'},
            {value: 11, name: 'November'},
            {value: 12, name: 'Desember'}
        ];

        $scope.initParam = function () {
            var tgl = new Date();
            $scope.vm.tahun = tgl.getFullYear();
            for (var i = 0; i < $scope.listBulan.length; i++) {
                if ($scope.listBulan[i].value === (tgl.getMonth() + 1)) {
                    $scope.vm.bulan = $scope.listBulan[i];
                }
            }
        };

        $scope.initParam();

        $scope.cetak = function (tipe) {
            var link = 'api/report/jml-container-pertujuan.' + tipe + '?tahun=' + $scope.vm.tahun + '&bulan=' + $scope.vm.bulan.value;
            if (tipe == 'pdf') {
                window.open(link, '_blank', 'width=1024, height=768');
            } else {
                location.href = link;
            }
        }
    }
}
)();