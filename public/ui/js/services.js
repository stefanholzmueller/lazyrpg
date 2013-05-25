'use strict';

var App = App || angular.module('lazyrpg', []);
App.factory('websocket', function($rootScope) {
	var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket
	var socket = new WS("ws://localhost:9000/ws/party?username=test")
	return {
		listen : function(callback) {
			socket.onmessage = function(event) {
				$rootScope.$apply(callback(event.data));
			}
		},
		send : function(msg) {
			socket.send(msg);
		}
	};
});
