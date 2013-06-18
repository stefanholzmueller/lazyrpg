'use strict';

lazyRPG.controller("PlayerController", function($scope, $window, wsFactory) {
	var escapedUsername = $window.location.pathname.split("/")[2]
	var username = decodeURIComponent(escapedUsername);

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
