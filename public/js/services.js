'use strict';

/* Services */

angular.module('modtools.services', ['config', 'ngResource'])

  .factory('Moderation', ['$resource', 'config', function ($resource, config) {
    return $resource(config.moderationApiUrl + '/comment', {}, {
      'comment': {method: 'GET', url: config.moderationApiUrl + '/queue/:queueId/comment'},
      'moderate': {method: 'POST', url: config.moderationApiUrl + '/comment/:commentId/status'}
    })
  }]);