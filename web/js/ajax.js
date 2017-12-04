
var makeCall = () => {};

$(function() {

	var ENDPOINT = "http://localhost:8080/Website";

	var ajaxCalls = {
		login: {
			url: "/account/login",
			method: "POST"
		},
		getaccounts: {
			url: "/account/get",
			method: "GET"
		},
		editaccount: {
			url: "/account/edit",
			method: "POST"
		},

		getreservations: {
			url: "/reservations/get",
			method: "POST",
			dummy : () => {
				var arr = [], ind = 0;;
				for(var i=0;i<5;i++) arr.push({
					reservation_id:(ind += Math.floor(Math.random()*10)+2),
					totalFare: Math.floor(Math.random()*500) + 200+"DUMMYDATADONTUSE"
				})
				return {reservations:arr}
			}
		},
		createreservation: {
			url: "/reservations/create",
			method: "POST"
		},

		getflights: {
			url: "/flights/get",
			method: "GET"
		},
		getairports: {
			url: "/flights/airports",
			method: "GET"
		},
		getairlines: {
			url: "/flights/airlines",
			method: "GET"
		},
		getmailinglist: {
			url: "/account/mailinglist",
			method: "GET"
		}
	}

	makeCall = function(call, options) {
		if (!ajaxCalls.hasOwnProperty(call))
			return false;
		var ajaxCall = ajaxCalls[call];

		var opts = options || {};
		var data = opts.data || {};
		var callback = opts.callBack || (() => {});

		if (ajaxCall.hasOwnProperty("dummy")) {
			var response = null;

			if (typeof ajaxCall.dummy == "object") {
				response = ajaxCall.dummy;
			} else if (typeof ajaxCall.dummy == "function") {
				response = ajaxCall.dummy(data);
			}

			callback(response);
			return true;
		}

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


