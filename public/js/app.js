'use strict';

angular.module('modtools', [
  'config',
  'ngRoute',
  'ngCookies',
  'modtools.services',
  'modtools.controllers',
  'modtools.filters',
  'modtools.directives'
])

  .config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/', {templateUrl: 'partials/home.html', controller: 'HomeController'});
    $routeProvider.otherwise({redirectTo: '/'});
  }]);