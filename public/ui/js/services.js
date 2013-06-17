'use strict';

lazyRPG.factory("wsFactory", function($rootScope) {
	return function(wsUrl) {
		var WS = window["MozWebSocket"] ? MozWebSocket : WebSocket;
		var socket = new WS(wsUrl);

		socket.onclose = function() {
			alert("WebSocket closed! Maybe the server was shut down. Oops.");
		};
		socket.onerror = function(event) {
			alert("WebSocket error: " + event.data);
		};

		return {
			close : function() {
				socket.close();
			},
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
