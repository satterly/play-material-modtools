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
        Comment.get({id: $scope.request.next.commentId}, function (response) {
          $scope.comment = response.comment;
          Content.lookup({path: $scope.comment.discussion.key.replace("/p/","")}, function (response) {
            $scope.content = response.response.content;
          })
        });
      })
    };
    next();

    $scope.$watch('comment', function(current, old){
      if (!angular.isDefined(current)) {
        $scope.showButtons();
      }
      console.log('---start----');
      console.log(current);
      console.log(old);
      console.log('---end----');
    });

    $scope.showButtons = function ($event) {
      $mdBottomSheet.show({
        scope: $scope,
        preserveScope: true,
        targetEvent: $event,
        templateUrl: 'partials/comment-buttons.html',
        controller: function ($scope, $mdBottomSheet) {
          $scope.approve = function () {
            console.log($scope.comment.id);
            Moderation.comment({commentId: $scope.comment.id}, {status: 'approve'}, function (response) {
              console.log(response);
              // Moderation.action({...})
            });
          };
          $scope.pick = function () {

          };
          $scope.block = function () {

          };
          $scope.remove = function () {

          };


          console.log($scope);
          console.log($mdBottomSheet);
        }
      }).then(function (item) {
        console.log('then');
        console.log(item);
      });
    }
  }]);
