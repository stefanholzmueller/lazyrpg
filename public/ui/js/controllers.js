'use strict';

var App = App || angular.module('lazyrpg', []);
App.controller('PartyController', function($scope, websocket) {
	$scope.controllerReady = "ready";
	websocket.listen(function(msg) {
		websocket.send(msg);
		alert(msg);
		$scope.controllerReady = msg;
	});
});
