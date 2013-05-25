'use strict';

var App = App || angular.module('lazyrpg', []);
App.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/party', {
		templateUrl : 'party.html',
		controller : 'PartyController'
	});
} ]);
