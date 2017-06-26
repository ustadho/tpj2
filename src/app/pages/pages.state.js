(function () {
    'use strict';

    angular
            .module('tpjApp')
            .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
                .state('pages', {
                    abstract: true,
                    parent: 'app'
                })
                .state('login', {
                    url: '/login',
                    title: 'Login',
                    views: {
                        'content@': {
                            templateUrl: 'app/components/login/login.html',
                            controller: 'LoginController as vm',
                        }
                    },
                    data: {
                        loginRequired: false,
                        pageTitle: 'Login'
                    },
                    sidebarMeta: {
                        order: 0,
                    },
                });
    }
})();
