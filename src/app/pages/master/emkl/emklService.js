(function () {
    'use strict';

    angular.module('tpjApp')
            .factory('EmklService', EmklService);

    /** @ngInject */
    EmklService.inject = ['$http', '$resource'];

    function EmklService($http, $resource) {
        var url = 'api/master/emkl';
        return {
            entity: $resource(url + '/:a', {}, {
                queryPage: {method: 'GET', isArray: false}
            }),
            query: function (search, p, callback) {
                return this.entity.queryPage({"a": search, "page": p, "size": 10}, callback)
            },
            entityComposite: $resource(url + '/idkota/cari/:idkota/:a', {}, {
                queryPage: {method: 'GET', isArray: false}
            }),
            queryComposite: function (idkota, search, p, callback) {
                return this.entityComposite.queryPage({"a": search, "idkota": idkota, "page": p, "size": 10}, callback)
            },
            simpan: simpan,
            cariSatu: cariSatu,
            cariSemua: cariSemua,
            hapus: hapus,
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
        function cariSatu(column,id) {
            return $http.get(url + '/'+column+'/' + id);
        }
        function cariSemua() {
            return $http.get(url + '/all');
        }
        function hapus(obj) {
            if (obj.id != null) {
                return $http.delete(url + "/" + obj.id);
            }
        }
    }

})();

