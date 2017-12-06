
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
		createaccount: {
			url: "/account/signup",
			method: "POST"
		},
		editaccount: {
			url: "/account/edit",
			method: "POST"
		},
		deleteaccount: {
			url: "/account/delete",
			method: "POST",
			adummy: {ok:true}
		},
		
		createemployee: {
			url: "/account/signup/employee",
			method: "POST"
		},
		getemployees: {
			url: "/account/get/employee",
			method: "GET"
		},
		editemployee: {
			url: "/account/edit/employee",
			method: "POST"
		},

		getreservation: {
			url: "/reservations/itinerary",
			method: "GET",
			transform : function(r) {
				if (!r || !r.Flights || !r.Flights.length) return [];
				var flights = {};
				r.Flights.forEach((leg) => {
					var air = leg.AirlineID+""+leg.FlightNo;
					if (!flights.hasOwnProperty(air)) {
						flights[air] = {
							airline_id : leg.AirlineID,
							flight_num : leg.FlightNo,
							legs : [0]
						}
					}
					var flegs = flights[air].legs;
					flegs[leg.LegNo] = leg;
					leg.ArrTime = new Date(leg.ArrTime)
					leg.DepTime = new Date(leg.DepTime)
				});
				var f = [];
				for(var i in flights) {
					if (flights.hasOwnProperty(i)) {
						for(var j=0;j<flights[i].legs.length;j++)
							if (!flights[i].legs[j]) {
								flights[i].legs.splice(j, 1);
								j--;
							}
						flights[i].DepTime = flights[i].legs[0].DepTime;
						flights[i].ArrTime = flights[i].legs[flights[i].legs.length-1].ArrTime;
						f.push(flights[i]);
					}
				}
				return f.sort((a,b) => b.DepTime - a.DepTime);
			}
		},
		getreservations: {
			url: "/reservations/get",
			method: "POST",
			_dummy : () => {
				var arr = [], ind = 0;;
				for(var i=0;i<5;i++) arr.push({
					reservation_id:(ind += Math.floor(Math.random()*10)+2),
					totalFare: Math.floor(Math.random()*500) + 200+"DUMMYDATADONTUSE"
				})
				//arr.push({reservation_id:15, totalFare:300})
				return {reservations:arr}
			}
		},
		createreservation: {
			url: "/reservations/create",
			method: "POST",
			_dummy : {
				reservation_id:26
			}
		},
		deletereservation: {
			url: "/reservations/delete",
			method: "POST",
			transform : function(r) {
				if (!r || !r.ok) return false;
				return true;
			}
		},

		getflights: {
			url: "/flights/get",
			method: "GET"
		},
		getairportflights: {
			url: "/reports/airportflights",
			method: "GET"
		},
		getpopularflights: {
			url: "/flights/getpopular",
			method: "GET"
		},
		getpersonalflights: {
			url: "/flights/getpersonallist",
			method: "GET",
			adummy: {
				flights:[]
			}
		},

		getcities: {
			url: "/flights/cities",
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
		},

		getmonthlyreport: {
			url: "/reports/month",
			method: "GET"
		},
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

		var transform = ajaxCall.transform || ((a) => a);

		$.ajax(
			ENDPOINT + ajaxCall.url,
			{
				method: ajaxCall.method || "GET",
				dataType: "json",
				data: data,
				success: (response) => callback(transform(response)),
				error: () => callback(transform(null))
			}
			);
		return true;
	}

});


