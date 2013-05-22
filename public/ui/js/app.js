'use strict';

var App = App || angular.module('autorpg', []);
App.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/party.html', {
		templateUrl : 'party.html',
		controller : 'PartyController'
	});
} ]);
