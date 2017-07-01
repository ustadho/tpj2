/**
 * @author v.lugovsky
 * created on 16.12.2015
 */
(function () {
    'use strict';

    angular.module('tpjApp')
            .config(routeConfig);

    /** @ngInject */
    function routeConfig($stateProvider) {
        $stateProvider
                .state('transaksi', {
                    parent: 'pages',
                    url: '/transaksi',
                    template: '<ui-view autoscroll="true" autoscroll-body-top></ui-view>',
                    abstract: true,
                    title: 'Transaksi',
                    sidebarMeta: {
                        icon: 'ion-map',
                        order: 200,
                    },
                })
                .state('transaksi.stuffing', {
                    url: '/stuffing',
                    parent: 'transaksi',
                    views: {
                        'content@': {
                            templateUrl: 'app/pages/transaksi/stuffing/stuffing.html',
                            controller: 'StuffingCtrl',
                            controllerAs: 'vm'
                        }
                    },
                    title: 'Stuffing',
                    sidebarMeta: {
                        order: 0,
                    },
                })
                .state('transaksi.suratjalan', {
                    url: '/suratjalan',
                    parent: 'transaksi',
                    views: {
                        'content@': {
                            templateUrl: 'app/pages/transaksi/suratjalan/suratjalan.html',
                            controller: 'SuratJalanCtrl'
                        }
                    },
                    title: 'Surat Jalan',
                    sidebarMeta: {
                        order: 100,
                    },
                })
                .state('transaksi.suratjalanedit', {
                    url: '/suratjalan/:idSj',
                    parent: 'transaksi',
                    views: {
                        'content@': {
                            url: '/suratjalan/:idSj',
                            templateUrl: 'app/pages/transaksi/suratjalan/suratjalan.html',
                        }
                    },
                    title: 'Surat Jalan [Edit]',
                    sidebarMeta: {
                        order: 100,
                    },
                })
                .state('transaksi.list-suratjalan', {
                    url: '/listsuratjalan',
                    parent: 'transaksi',
                    views: {
                        'content@': {
                            templateUrl: 'app/pages/transaksi/suratjalan/listsuratjalan.html',
                            controller: 'ListSuratJalanCtrl'
                        }
                    },
                    title: 'List Surat Jalan',
                    sidebarMeta: {
                        order: 100,
                    },
                })
    }
})();
