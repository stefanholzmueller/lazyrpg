'use strict';

var App = App || angular.module('autorpg', []);
App.factory('websocket', function($rootScope) {
	var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket
	var socket = new WS("ws://localhost:9000/ws/party")
	return {
		on : function(eventName, callback) {
			socket.on(eventName, function() {
				var args = arguments;
				$rootScope.$apply(function() {
					callback.apply(socket, args);
				});
			});
		},
		emit : function(eventName, data, callback) {
			socket.emit(eventName, data, function() {
				var args = arguments;
				$rootScope.$apply(function() {
					if (callback) {
						callback.apply(socket, args);
					}
				});
			})
		}
	};
});