'use strict';

var App = App || angular.module('lazyrpg', []);
App.factory('wsFactory', function($rootScope) {
	return function(wsUrl) {
		var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
		var socket = new WS(wsUrl);
		
		socket.onopen = function() {
//			alert("ws opened!");
		};
		socket.onclose = function() {
//			alert("ws closed!");
		};
		socket.onerror = function(event) {
			alert("ws error: " + event.data);
		};
		
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
	};
});
