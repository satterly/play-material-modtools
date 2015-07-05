'use strict';

angular.module('config', [])

  .constant('config', {
    'moderationApiUrl': ""  // served from same domain (no CORS)
  });