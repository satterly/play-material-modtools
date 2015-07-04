'use strict';

/* Controllers */

angular.module('modtools.controllers', ['ngMaterial'])

  .controller('SidenavController', ['$scope', '$mdSidenav', function ($scope, $mdSidenav) {

    $scope.queues = [
      'Central',
      'Central (watched)',
      'Comment is Free',
      'CiF (watched)'
    ];

    $scope.menu = function () {
      $mdSidenav('left').toggle();
    };
  }])

  .controller('HomeController', ['$scope', function ($scope) {

  }]);