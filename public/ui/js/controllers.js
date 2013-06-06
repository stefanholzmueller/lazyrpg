'use strict';

var App = App || angular.module('lazyrpg', []);
App.controller('PlayerController', function($scope, wsFactory) {
	var escapedUsername = "tester";
	
	var host = document.location.host;
	var wsUrl = "ws://" + host + "/ws/player?username=" + escapedUsername;
	var ws = wsFactory(wsUrl);

	$scope.connectionStatus = "Connected";
	$scope.log = [];

	ws.listen(function(data) {
		var msg = JSON.parse(data);
		if (msg.log) {
			$scope.log.push(msg.log);
		} else {
			ws.send(msg);
			alert("Unknown message: " + msg);
		}
	});
});
