(function () {
    'use strict';

    angular.module('tpjApp')
            .factory('SuratJalanService', SuratJalanService);

    /** @ngInject */
    SuratJalanService.inject = ['$http', '$resource'];

    function SuratJalanService($http, $resource) {
        var url = 'api/transaksi/sj';
        return {
            entity: $resource(url + '/:a', {}, {
                queryPage: {method: 'GET', isArray: false}
            }),
            query: function (search, p, callback) {
                return this.entity.queryPage({"a": search, "page": p, "size": 10}, callback)
            },
            entityComposite: $resource(url + '/tglawal/tglakhir/idkota/cari/:tglawal/:tglakhir/:idkota/:a', {}, {
                queryPage: {method: 'GET', isArray: false}
            }),
            queryComposite: function (tglawal, tglakhir, idkota, search, p, callback) {
                return this.entityComposite.queryPage({"a": search, "tglawal": tglawal, "tglakhir": tglakhir, "idkota": idkota, "page": p, "size": 10}, callback)
            },
            infoItemComposite: $resource(url + '/infoitem/tglawal/tglakhir/idtoko/cari/:tglawal/:tglakhir/:idtoko/:a', {}, {
                queryPage: {method: 'GET', isArray: false}
            }),
            queryInfoItemComposite: function (tglawal, tglakhir, idtoko, search, p, callback) {
                return this.infoItemComposite.queryPage({"a": search, "tglawal": tglawal, "tglakhir": tglakhir, "idtoko": idtoko, "page": p, "size": 10}, callback)
            },
            hapus: hapus,
            simpan: simpan,
            cariSatu: cariSatu,
            lookupItem: lookupItem,
            getSjStuffing: getSjStuffing,
            postSjStuffings: postSjStuffings
        }

//        return service;

        ;

        function simpan(data, ori) {
            if (ori.id == null || ori.id == undefined) {
                return $http.post(url, data)
            } else {
                return $http.put(url + '/' + ori.id, data)
            }
        }
        function cariSatu(column, id) {
            console.log('get '+url + '/' + column + '/' + id);
            return $http.get(url + '/' + column + '/' + id);
        }
        function getSjStuffing(id) {
            return $http.get(url + '/get-sj-stuffing/' + id);
        }
        function lookupItem(s) {
            return $http.get(url + '/item/lookup/' + s);
        }
        function postSjStuffings(listData) {
            return $http.post(url + '/post-sj-stuffings', listData);
        }
        function hapus(id) {
            return $http.delete(url + '/' + id);
        }
    }

})();

