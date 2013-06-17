'use strict';

lazyRPG.controller("PlayerController", function($scope, wsFactory) {
	var username = window.location.pathname.split("/")[2]
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
			ws.close();
		}
	});
});
