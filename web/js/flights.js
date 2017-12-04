

$(function() {

	if ($("#numberofseats").length) {
		for(var i=1;i<=10;i++) {
			$("#numberofseats").append(
				$("<option>")
					.attr("value", i)
					.html(i)
					.attr("selected", i == 1)
				);
		}
	}

	var airports = null;
	var airportCb = [], airportRequestSent = false;
	function getAirports(callBack) {
		if (airports != null) {
			callBack(airports);
			return;
		}
		airportCb.push(callBack);
		if (airportRequestSent) return;
		airportRequestSent = true;
		makeCall("getairports", { callBack: (r) => {
			if (!r || !r.airports) return;
			airports = [];
			r.airports.forEach((airport) => {
				airports.push([airport.name, airport.city, airport.country, airport.airport_id]);
			});
			for(var i=0;i<airportCb.length;i++) {
				airportCb[i](airports);
			}
			airportCb = [];
		}});
	}

	$(".locationinput").each(function() {
		var $this = $(this);

		var $wrapper = $("<div>").addClass("inputwrapper locationwrapper");
		$this.replaceWith($wrapper);
		$wrapper.append($this);

		var $dropdown = $("<div>").addClass("dropdown");
		$wrapper.append($dropdown);
		$dropdown.append(
			$("<div>").addClass("loading").html("Loading...")
			);

		var selected = -2;
		var listed = [];
		function populateDropdown(criteria) {
			$dropdown.empty();
			selected = -1;

			var matchCriteria = () => true;
			if (!!criteria) {
				criteria = criteria.toLowerCase();
				matchCriteria = (value) => (value.indexOf(criteria) > -1);
			}

			listed = [];
			for(var i=0;i<airports.length;i++) {
				var a = airports[i];
				var string = a[1]+", "+a[2]+" ["+a[0]+"]";
				if (matchCriteria(a[0].toLowerCase()) || matchCriteria(a[1].toLowerCase()) || matchCriteria(a[2].toLowerCase()) || matchCriteria(a[3].toLowerCase()) ||
					string.toLowerCase() == criteria) {
					$dropdown.append(
						$("<div>")
							.addClass("airport")
							.attr("index", i)
							.append("<span>"+string+"</span>")
							.on("mousedown", selectAirport)
						);
					listed.push(i);
					if (listed.length >= 8)
						break;
				}
			}
		}
		function selectAirport(ind) {
			if (isNaN(ind)) {
				ind = parseInt($(this).attr("index"));
			}
			var a = airports[ind];
			$this.val(a[1]+", "+a[2]+" ["+a[0]+"]");
			$this.attr("airport_id", a[3]);
			if (getFlights)
				getFlights();
		}

		$this.on("focus", function() {
			$dropdown.addClass("visible");
			getAirports((airports) => {
				populateDropdown($this.val());
			});
		}).on("blur", function() {
			selected = -2;
			$dropdown.removeClass("visible");
		});
		$this.on("keyup", function(e) {
			var key = e.keyCode;

			if (key == 38 || key == 40) {
				if (selected == -2) return false;
				if (key == 40) selected++;
				else selected--;
				if (selected < 0) selected = listed.length - 1;
				if (selected >= listed.length) selected = 0;
				$dropdown.find(".airport.active").removeClass("active");
				$dropdown.find(".airport[index="+listed[selected]+"]").addClass("active");
				e.preventDefault();
				return false;
			}

			if (key == 13) {
				if (selected < 0 || selected >= listed.length) return false;
				selectAirport(listed[selected]);
				$(this).blur();
				return false;
			}

			getAirports((airports) => {
				populateDropdown($this.val());
			});
		});
	});

	$(".dateinput").each(function() {
		var $this = $(this);

		var today = new Date(); today.setDate(today.getDate() + 5);
		var dd = today.getDate(), mm = today.getMonth()+1, yyyy = today.getFullYear();
		if (dd<10) dd='0'+dd;
		if (mm<10) mm='0'+mm;
		var today = mm+'/'+dd+'/'+yyyy;
		$this
			//.val(today)
			.datepicker({ dateFormat: 'mm/dd/yy' })
	})
	$(".dateinput").on('change', function() {
		getFlights();
	})


	function transformDate(str) {
		var a = str.split("/");
		if (a.length != 3) return "";
		return a[2]+"-"+a[0]+"-"+a[1];
	}
	function getFlights() {
		var fromDate = transformDate($("#departing").val()),
			toDate = transformDate($("#arriving").val()),
			fromAirport = $("#flyingfrom").attr("airport_id"),
			toAirport = $("#flyingto").attr("airport_id"),
			numOfSeats = parseInt($("#numberofseats").val());

		if (!fromAirport || !toAirport) {
			$(".flights").addClass("none pick");
			return;
		}

		var data = {
			seats: numOfSeats,
			fromAirportID : fromAirport,
			toAirportID : toAirport
		};
		if (fromDate)
			data.fromDate = fromDate;
		if (toDate)
			data.toDate = toDate;

		$(".flights").removeClass("none").addClass("loading");
		$(".flights .flight").not(".dummy").remove();

		makeCall("getflights", {
			data: data,
			callBack: returnFlights
		});
	}
	
	function buildLegString(l) {
		var str = "";
		for(var i=0;i<l.length;i++) str += l[i].depAirportID+" - ";
		str += l[l.length-1].arrAirportID;
		return str;
	}
	function buildOperates(dow) {
		var str = "Operates on: ";
		if (dow == "1111111") str += "All Days";
		else if (dow == "1111100") str += "Weekdays";
		else if (dow == "0000011") str += "Weekends";
		else {
			var days = [];
			dow.split("").forEach((el, i) => {
				if (el == "1")
					days.push("Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday".split(",")[i]);
			})
			str += days.join(", ");
		}
		return str;
	}
	function buildTime(legs) {
		var time = legs[legs.length - 1].arrTime - legs[0].depTime;
		time /= 1000 * 60;
		time = Math.round(time);
		return Math.floor(time / 60) + "h "+(time % 60)+"m";
	}
	var months = "Jan,Feb,Mar,Apr,May,Jun,Jul,Aug,Sep,Oct,Nov,Dec".split(",");
	function buildDepDate(legs) {
		var time = legs[0].depTime;
		return months[(time.getMonth())]+" "+time.getDate();
	}
	function padTwo(a) {return (a < 10) ? ("0"+a) : (""+a);}
	function buildHumanTime(date) {
		var hour = date.getHours(), ampm = "am";
		if (hour >= 12) { hour -= 12; ampm = "pm"; }
		if (hour == 0) hour = 12;
		return (hour)+":"+padTwo(date.getMinutes())+ampm;
	}
	function updatePrice($el, price) {
		$el.find("div").html("$"+Math.floor(price));		
		$el.find("span").html("."+padTwo((price*100) % 100));
		$el.attr("price", price);
	}


	var flights = [];
	var returnFlights = (r) => {
		$(".flights").removeClass("loading pick")
		if (!r.flights || !r.flights.length) {
			$(".flights").addClass("none").removeClass("pick");
			return;
		}

		r.flights.forEach((flight, index) => {
			flight.legs = transformFlightLeg(flight.legs);
			
			var $flight = $(".flight.dummy").clone(true, true);
			$flight.removeClass("dummy");
			$flight.attr("flight", index);

			$flight.find(".airline").html(getAirlineName(flight.airline_id));
			$flight.find(".airports").html(buildLegString(flight.legs));
			if (flight.legs.length == 1)
				$flight.find(".stops").html("Direct");
			else if (flight.legs.length == 2)
				$flight.find(".stops").html("1 Stop");
			else
				$flight.find(".stops").html(flight.legs.length-1 + " Stops");
			$flight.find(".bottomleft").html(buildOperates(flight.daysOfWeek));
			$flight.find(".date").html(buildDepDate(flight.legs));
			$flight.find(".time").html(buildTime(flight.legs));
			$flight.find(".timerange").html(
				buildHumanTime(flight.legs[0].depTime) + " - " +
				buildHumanTime(flight.legs[flight.legs.length-1].arrTime));
			updatePrice($flight.find(".price"), flight.prices["one-way"].economy);
			

			$(".flights").append($flight);
		});
		flights = r.flights;
	}

	$(".bookFlight .prices .choice").click(function() {
		$(".bookFlight .prices .choice").removeClass("selected");
		$(this).addClass("selected");
	})

	var activeFlight = null;
	$(".flight.dummy .select").click(function(e) {
		var index = parseInt($(this).closest(".flight").attr("flight"));
		var flight = flights[index];
		activeFlight = flight;
		
		$(".bookFlight").addClass("show");
		$(".bookFlight .flightdetail").html(getAirlineName(flight.airline_id)+" "+flight.flightNumber);
		$(".bookFlight .date").html(buildDepDate(flight.legs)+"<span>"+
			buildHumanTime(flight.legs[0].depTime) + " - " +
			buildHumanTime(flight.legs[flight.legs.length-1].arrTime)+"</span>");
		$(".bookFlight .legs").html(buildLegString(flight.legs));
		for (var i in flight.prices["one-way"]) {
			if (i == "economy")
				updatePrice($(".bookFlight .economy .price"), flight.prices["one-way"][i])
			else if (i == "first")
				updatePrice($(".bookFlight .firstclass .price"), flight.prices["one-way"][i])
		}
		if (TYPE == 1)
			$(".bookFlight .customer").trigger("change");
		else if (TYPE == 0) {
			makeCall("getreservations", {
				data: {account_id: ID},
				callBack : populateReservations
			});
		}
	});

	function populateReservations(r) {
		$(".bookFlight .reservation option").not("[value=-1]").remove();
		if (r && r.reservations) {
			r.reservations.forEach((res) => {
				$(".bookFlight .reservation").append(
					$("<option></option>").attr("value", res.reservation_id)
						.html("Reservation "+res.reservation_id+" - Total Fare: $"+res.totalFare)
					);
			});
		}
	}
	$(".bookFlight .customer").on("change", function() {
		var account_id = $(this).val();
		
		makeCall("getreservations", {
			data: {account_id: account_id},
			callBack : populateReservations
		});
	})

	$(".bookFlight .book").click(function() {
		var flight = activeFlight;
		if (!flight) return;
		console.log(flight);
		
		var selchoice = $(".bookFlight .prices .choice.selected").attr("selectprice");
		var price = flight.prices["one-way"][selchoice];
		var legs = [];
		for(var i=0;i<flight.legs.length;i++) legs.push(flight.legs[i].leg);
		legs = legs.join(" ");
		var date = flight.legs[0].depTime;
		date = date.getFullYear()+""+(1+date.getMonth())+""+date.getDate();

		var data = {
			account_id : ID, // CUSTOMER ID NOT LOGGED IN ID
			airline_id : flight.airline_id,
			flightNumber : flight.flightNumber,
			legNumber : legs,
			flightFare : price,
			date : date
		};
		if (TYPE == 1) {
			data.account_id = parseInt($(".bookFlight .customer").val());
			data.customer_rep_id = ID;
		}
		var res_id = parseInt($(".bookFlight .reservation").val())
		if (res_id > 0)
			data.reservation_id = res_id;

		makeCall("createreservation", {
			data: data,
			callBack : (r) => {
				console.log(r)
			}
		})
	});

























	getAirlines();

	/*
	$("#flyingfrom").attr("airport_id", "JFK")
	$("#flyingto").attr("airport_id", "LAX")
	getFlights();
	//*/






	

});









