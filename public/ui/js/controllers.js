'use strict';

var App = App || angular.module('lazyrpg', []);
App.controller('PlayerController', function($scope, wsFactory) {
	var username = "tester";
	var escapedUsername = encodeURIComponent(username);

	var host = document.location.host;
	var wsUrl = "ws://" + host + "/ws/player?username=" + escapedUsername;
	var ws = wsFactory(wsUrl);

	$scope.connectionStatus = "Connected as " + username;
	$scope.log = [];
	$scope.sheet = {};

	ws.listen(function(data) {
		var msg = JSON.parse(data);
		if (msg.log) {
			$scope.log.push(msg.log);
		} else if (msg.sheet) {
			$scope.sheet = msg.sheet;
		} else {
			ws.send(data);
			alert("Unknown data: " + data);
		}
	});
});
