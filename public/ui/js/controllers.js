'use strict';

var App = App || angular.module('autorpg', []);
App.controller('PartyController', function($scope, websocket) {
	$scope.controllerReady = "ready";
	websocket.listen(function(data) {
		websocket.send(data);
		alert(data);
		$scope.controllerReady = data;
	});
});
