'use strict';

var App = App || angular.module('lazyrpg', []);
App.factory('websocket', function($rootScope) {
	var username = "tester";
	
	var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
	var socket = new WS("ws://" + document.location.host + "/ws/player?username=" + username);
	
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
