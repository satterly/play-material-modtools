'use strict';

/* Services */

angular.module('modtools.services', ['config', 'ngResource'])

  .factory('Moderation', ['$resource', 'config', function ($resource, config) {
    return $resource(config.moderationApiUrl + '/comment', {}, {
      'next':     {method: 'GET', url: config.moderationApiUrl + '/queues/:queue/next'},
      'moderate': {method: 'POST', url: config.moderationApiUrl + '/comment/:commentId/status'}
    })
  }])

  .factory('Content', ['$resource', 'config', function ($resource, config) {
    return $resource(config.contentApiUrl, {'orderBy': 'newest', 'page-size': 100, 'api-key': config.contentApiKey}, {
      'query':    {method: 'GET', url: config.contentApiUrl + '/search'},
      'shortUrl': {method: 'GET', params: {'show-fields': 'shortUrl'}, url: config.contentApiUrl + '/:section/:year/:month/:day/:slug'
      }
    });
  }])

  .factory('Discussion', ['$resource', 'config', function ($resource, config) {
    return $resource(config.discussionApiUrl, {'orderBy': 'newest', 'pageSize': 50}, {
      'counts':      {method: 'GET', url: config.discussionApiUrl + '/getCommentCounts'},
      'most':        {method: 'GET', url: config.discussionApiUrl + '/most/comments'},
      'moststaff':   {method: 'GET', url: config.discussionApiUrl + '/discussion/most/staff'},
      'popular':     {method: 'GET', url: config.discussionApiUrl + '/popular'},
      'get':         {method: 'GET', url: config.discussionApiUrl + '/discussion//p/:key'},
      'featured':    {method: 'GET', url: config.discussionApiUrl + '/discussion//p/:key/featuredcomments'},
      'highlighted': {method: 'GET', url: config.discussionApiUrl + '/discussion//p/:key/highlights'},
      'recommended': {method: 'GET', url: config.discussionApiUrl + '/discussion//p/:key/recommended'},
      'staff':       {method: 'GET', url: config.discussionApiUrl + '/discussion//p/:key/staff-comments'},
      'top':         {method: 'GET', url: config.discussionApiUrl + '/discussion//p/:key/topcomments'}
    });
  }])

  .factory('Comment', ['$resource', 'config', function ($resource, config) {
    return $resource(config.discussionApiUrl, {}, {
      'query':       {method: 'GET', url: config.discussionApiUrl + '/search'},
      'highlighted': {method: 'GET', url: config.discussionApiUrl + '/recent/highlighted'},
      'recommended': {method: 'GET', url: config.discussionApiUrl + '/recent/recommended'},
      'get':         {method: 'GET', url: config.discussionApiUrl + '/comment/:id'},
      'context':     {method: 'GET', url: config.discussionApiUrl + '/comment/:id/context'}
    });
  }])

  .factory('Profile', ['$resource', 'config', function ($resource, config) {
    return $resource(config.discussionApiUrl, {}, {
      'get':         {method: 'GET', url: config.discussionApiUrl + '/profile/:userid'},
      'stats':       {method: 'GET', url: config.discussionApiUrl + '/profile/:userid/stats'},
      'comments':    {method: 'GET', url: config.discussionApiUrl + '/profile/:userid/comments'},
      'picked':      {method: 'GET', url: config.discussionApiUrl + '/profile/:userid/comments?displayHighlighted'},
      'replies':     {method: 'GET', url: config.discussionApiUrl + '/profile/:userid/replies'},
      'discussions': {method: 'GET', url: config.discussionApiUrl + '/profile/:userid/discussions'}
    });
  }])

  .factory('Identity', ['$resource', 'config', function ($resource, config) {
    return $resource(config.identityApiUrl, {}, {
      'get': {method: 'JSONP', params: {callback: 'JSON_CALLBACK'}, url: config.identityApiUrl + '/user/:userid'}
    });
  }]);
