'use strict';

/* Controllers */

angular.module('modtools.controllers', ['ngMaterial', 'ngSanitize'])

  .controller('SidenavController', ['$scope', '$location', '$mdSidenav', function ($scope, $location, $mdSidenav) {

    $scope.queues = [
      'Central',
      'Central (watched)',
      'Comment is Free',
      'CiF (watched)'
    ];

    $scope.menu = function () {
      $mdSidenav('left').toggle();
    };
    $scope.goto = function(queue, event) {
      console.log(queue);
      // console.log(event);
      $location.path('/queue/' + queue)
    };
  }])

  .controller('HomeController', ['$scope', function ($scope) {


  }])

  .controller('QueueController', ['$scope', '$routeParams', '$mdBottomSheet', 'Moderation', 'Comment', 'Content',
    function ($scope, $routeParams, $mdBottomSheet, Moderation, Comment, Content) {

    $scope.queue = $routeParams.queue;
    $scope.comment = undefined;

    // get next comment and display it for moderation

    var next = function () {
      Moderation.next({queue: $scope.queue}, function (response) {
        $scope.request = response.data;
        Comment.get({id: $scope.request.moderation.commentId}, function (response) {
          $scope.comment = response.comment;
          Content.lookup({path: $scope.comment.discussion.key.replace("/p/","")}, function (response) {
            $scope.content = response.response.content;
          })
        });
      })
    };
    next();

    $scope.$watch('comment', function(current, old){
      if (current) {
        showButtons();
      }
      console.log('---start----');
      console.log(current);
      console.log(old);
      console.log('---end----');
    });

    var showButtons = function () {
      $mdBottomSheet.show({
        templateUrl: 'partials/comment-buttons.html',
        controller: 'CommentButtonController'
      }).then(function (clickedItem) {
        $scope.alert = clickedItem.name + ' clicked!';
      });
    }
  }])

  .controller('CommentButtonController', ['$scope', '$mdBottomSheet', function ($scope, $mdBottomSheet) {

  }]);