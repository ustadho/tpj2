(function () {
    'use strict';

    angular.module('tpjApp')
            .controller('DashboardCtrl', DashboardCtrl)
    function DashboardCtrl($scope, $filter, DashboardService) {
        $scope.d = new Date();
        $scope.d.setMonth($scope.d.getMonth() - 1);
        $scope.options = {
            tgl1: $scope.d,
            tgl2: new Date(),
            grup: {name: 'Kota Tujuan', value: 'kota_tujuan'},
            order: {name: 'Kubikasi Terbesar', value: 'kubikasi_desc'},
            limit: 5,
        };
        $scope.optionsTgl = {format: 'DD/MM/YYYY', showClear: false};
        $scope.listGrup = [
            {name: 'Kota Tujuan', value: 'kota_tujuan'},
            {name: 'Kondisi', value: 'kondisi'},
            {name: 'Customer', value: 'customer'},
            {name: 'Kapal', value: 'kapal'},
            {name: 'Kontainer', value: 'nomor_kontainer'},
            {name: 'Emkl', value: 'emkl'},
            {name: 'Pengirim', value: 'pengirim'}
        ];
        $scope.listOrder = [
            {name: 'Nama Asc', value: 'grup_asc'},
            {name: 'Nama Desc', value: 'grup_desc'},
            {name: 'Coli Terbesar', value: 'coli_desc'},
            {name: 'Coli Terkecil', value: 'coli_asc'},
            {name: 'Kubikasi Terbesar', value: 'kubikasi_desc'},
            {name: 'Kubikasi Terkecil', value: 'kubikasi_asc'},
        ];

        $scope.initGrupColiKubikasiChart = function (series, categories, grup, idContainer) {
            $scope.grupColiKubikasiChart = Highcharts.chart(idContainer, {
                chart: {
                    type: 'column'
                },
                title: {
                    text: 'Rekap Coli & Kubikasi berdasarkan ' + grup
                },
                subtitle: {
                    text: 'Sumber : Data Transaksi'
                },
                xAxis: {
                    categories: categories,
                    crosshair: true
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: 'Jumlah'
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                    pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                            '<td style="padding:0"><b>{point.y:.1f}</b></td></tr>',
                    footerFormat: '</table>',
                    shared: true,
                    useHTML: true
                },
                plotOptions: {
                    column: {
                        pointPadding: 0.2,
                        borderWidth: 0
                    }
                },
                series: series
            });
        };

        $scope.reloadData = function () {
            $scope.param = {
                tgl1: $filter('date')(new Date($scope.options.tgl1), 'yyyy-MM-dd'),
                tgl2: $filter('date')(new Date($scope.options.tgl2), 'yyyy-MM-dd'),
                grup: $scope.options.grup.value,
                order: $scope.options.order.value,
                limit: $scope.options.limit,
            };
            console.log('$scope.param', $scope.param);
            DashboardService.rekapColiKubikasi($scope.param).success(function (data) {
                $scope.categories = [];
                $scope.seriesColi = {
                    name: 'Coli',
                    data: []
                };
                $scope.seriesKubikasi = {
                    name: 'Kubikasi (metrik kubik)',
                    data: []
                };
                for (var i = 0; i < data.length; i++) {
                    $scope.categories.push(data[i].grup);
                    $scope.seriesColi.data.push(data[i].coli);
                    $scope.seriesKubikasi.data.push(data[i].kubikasi);
                }
                $scope.listSeries = [];
                $scope.listSeries.push($scope.seriesColi);
                $scope.listSeries.push($scope.seriesKubikasi);
                console.log('$scope.categories', $scope.categories);
                console.log('$scope.listSeries', $scope.listSeries);
                $scope.initGrupColiKubikasiChart($scope.listSeries, $scope.categories, $scope.options.grup.name, 'grupColiKubikasiChart');
            });
        };

        $scope.$watchGroup(['options.grup', 'options.order', 'options.tgl1', 'options.tgl2', 'options.limit'], function (newValues, oldValues, scope) {
            $scope.reloadData();
        });

    }
})();