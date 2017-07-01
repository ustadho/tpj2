(function () {
    'use strict';

    angular.module('tpjApp')
            .factory('DashboardService', DashboardService);

    /** @ngInject */
    DashboardService.inject = ['$http', '$resource'];

    function DashboardService($http, $resource) {
        var url = 'api/report';
        return {
            rekapColiKubikasi: rekapColiKubikasi,
        }

        ;
        function rekapColiKubikasi(options) {
            return $http.get(url + '/rekap-coli-kubikasi?tgl1=' + options.tgl1 + '&tgl2=' + options.tgl2 + '&grup=' + options.grup + '&limit=' + options.limit + '&order=' + options.order);
        }
    }

})();

