
var makeCall = () => {};

$(function() {

	var ENDPOINT = "http://localhost:8080/Website";

	var ajaxCalls = {
		login: {
			url: "/account/login",
			method: "POST"
		}
	}

	makeCall = function(call, options) {
		if (!ajaxCalls.hasOwnProperty(call))
			return false;
		var ajaxCall = ajaxCalls[call];

		var opts = options || {};
		var data = opts.data || {};
		var callback = opts.callBack || (() => {});

		$.ajax(
			ENDPOINT + ajaxCall.url,
			{
				method: ajaxCall.method || "GET",
				dataType: "json",
				data: data,
				success: callback,
				error: () => callback(null)
			}
			);
		return true;
	}

});


