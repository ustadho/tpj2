(function () {
    'use strict';

    angular.module('tpjApp')
            .factory('KapalService', KapalService);

    /** @ngInject */
    KapalService.inject = ['$http', '$resource'];

    function KapalService($http, $resource) {
        var url = 'api/master/kapal';
        return {
            entity: $resource(url + '/:a', {}, {
                queryPage: {method: 'GET', isArray: false}
            }),
            query: function (search, p, callback) {
                return this.entity.queryPage({"a": search, "page": p, "size": 10}, callback)
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
        function hapus(obj) {
            if (obj.id != null) {
                return $http.delete(url + "/" + obj.id);
            }
        }
        function cariSemua() {
            return $http.get(url + '/all');
        }
    }

})();

