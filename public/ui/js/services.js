'use strict';

var App = App || angular.module('lazyrpg', []);
App.factory('wsFactory', function($rootScope) {
	return function(wsUrl) {
		var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
		var socket = new WS(wsUrl);
		
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
