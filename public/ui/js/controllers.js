'use strict';

var App = App || angular.module('autorpg', []);
App.controller('PartyController', function($scope, websocket) {
	$scope.controllerReady = "ready";
	websocket.onmessage = function(evt) {
		alert(evt.data);
		$scope.controllerReady = evt.data;
	};
});