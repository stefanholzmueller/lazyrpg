'use strict';

lazyRPG.controller("LoginController", function($scope, $window) {
	$scope.login = function() {
		var username = $scope.username;
		var errors = validateUsername(username);
		
		if (errors.length === 0) {
			redirectToPlayPage(username);
		} else {
			$scope.errors = errors;
		}
	}
	
	$scope.enter = function($event) {
		if ($event.keyCode == 13) {
			$scope.login();
		}
	}

	function validateUsername(username) {
		var errors = [];
		
		if (username) {
			if (username.length < 3 || username.length > 30) {
				errors.push("Usernames must have between 3 and 30 characters");
			}
		} else {
			errors.push("Username must not be empty");
		}
		
		return errors;
	}

	function redirectToPlayPage(username) {
		$window.location.href = "http://" + document.location.host + "/play/"
				+ encodeURIComponent(username);
	}
});

lazyRPG.controller("PlayerController", function($scope, $window, wsFactory) {
	var escapedUsername = $window.location.pathname.split("/")[2]
	var username = decodeURIComponent(escapedUsername);

	var host = document.location.host;
	var wsUrl = "ws://" + host + "/ws/player?username=" + escapedUsername;
	var ws = wsFactory(wsUrl);

	$scope.connectionStatus = "Connected as " + username;
	$scope.sheet = {
			lvl : CHARACTER_INITIAL_LEVEL,
			xp : CHARACTER_INITIAL_XP,
			xpNext : CHARACTER_XP_PER_LEVEL_BASE
	};
	$scope.log = [];

	ws.listen(function(data) {
		var msg = JSON.parse(data);
		if (msg.log) {
			$scope.log.push(msg.log);
		} else if (msg.sheet) {
			$scope.sheet = msg.sheet;
		} else {
			alert("Unknown data: " + data);
			ws.close();
		}
	});
});
