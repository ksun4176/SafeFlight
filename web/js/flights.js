

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


	var flights = [];
	var returnFlights = (r) => {
		$(".flights").removeClass("loading pick")
		if (!r.flights || !r.flights.length) {
			$(".flights").addClass("none").removeClass("pick");
			return;
		}

		r.flights.forEach((flight, index) => {
			flight.legs = transformFlightLeg(flight.legs);

			console.log(flight);
			
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

			$(".flights").append($flight);
		});
		flights = r.flights;
	}

	getAirlines();

	//$("#flyingfrom").attr("airport_id", "JFK")
	//$("#flyingto").attr("airport_id", "LAX")
	//getFlights();












	

});






