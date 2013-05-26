'use strict';

var App = App || angular.module('lazyrpg', []);
App.controller('PlayerController', function($scope, wsFactory) {
	var escapedUsername = "tester";
	var wsUrl = "ws://" + document.location.host + "/ws/player?username=" + escapedUsername;
	var ws = wsFactory(wsUrl);
	
	$scope.controllerReady = "ready";
	ws.listen(function(msg) {
		ws.send(msg);
//		alert(msg);
		$scope.controllerReady = msg;
	});
});
