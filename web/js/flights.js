

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

	$("#left .option .title").click(function() {
		$("#left .option.selected").removeClass("selected");
		$(this).closest(".option").addClass("selected");
		getFlights();
	})
	$("#left .personalflights .personalcustomer").on("change", function() {
		getFlights();
	});

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
		makeCall("getcities", { callBack: (r) => {
			if (!r || !r.cities) return;
			airports = [];
			r.cities.forEach((airport) => {
				airports.push([airport.city, airport.country, airport.city+","+airport.country]);
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
				var string = a[0]+", "+a[1];
				if (matchCriteria(a[0].toLowerCase()) || matchCriteria(a[1].toLowerCase()) ||
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
			$this.val(a[0]+", "+a[1]);
			$this.attr("airport_id", a[2]);
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
	});

	$("#numberofseats").on('change', function() {
		getFlights();
	});

	$("#left .return .checkbox").on('change', function() {
		if (this.checked) {
			$("#left .return").addClass("expandreturn");
			$("#returning").val("");
		} else {
			$("#left .return").removeClass("expandreturn");
		}
		getFlights();
	});


	function transformDate(str) {
		var a = str.split("/");
		if (a.length != 3) return "";
		return a[2]+"-"+a[0]+"-"+a[1];
	}
	function getFlights() {
		if ($(".bestflights.option").hasClass("selected")) {
			$(".flights").removeClass("none pick").addClass("loading");
			$(".flights .flight").not(".dummy").remove();
			makeCall("getpopularflights", {
				callBack: returnFlights
			});
		}
		if ($(".personalflights.option").hasClass("selected")) {
			$(".flights").removeClass("none pick").addClass("loading");
			$(".flights .flight").not(".dummy").remove();
			var account_id = ID;
			if (TYPE == 1)
				account_id = parseInt($(".personalcustomer").val());
			makeCall("getpersonalflights", {
				data: {
					account_id: account_id
				},
				callBack: returnFlights
			});
		}
		if ($(".searchflights.option").hasClass("selected")) {
			var fromDate = transformDate($("#departing").val()),
				toDate = transformDate($("#arriving").val()),
				fromAirport = $("#flyingfrom").attr("airport_id"),
				toAirport = $("#flyingto").attr("airport_id"),
				numOfSeats = parseInt($("#numberofseats").val());
			var roundTrip = $("#left .return .checkbox").get(0).checked,
				returnBy = transformDate($("#returning").val());

			if (!fromAirport || !toAirport) {
				$(".flights").addClass("none pick");
				return;
			}

			var data = {
				seats: numOfSeats,
				fromAirport : fromAirport,
				toAirport : toAirport,
				roundtrip : false
			};
			if (fromDate)
				data.fromDate = fromDate;
			if (toDate)
				data.toDate = toDate;
			if (roundTrip) {
				data.roundtrip = true;
				if (returnBy)
					data.backBeforeDate = returnBy;
			}

			$(".flights").removeClass("none pick").addClass("loading");
			$(".flights .flight").not(".dummy").remove();
			makeCall("getflights", {
				data: data,
				callBack: returnFlights
			});
		}
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
		if (!r || !r.flights || !r.flights.length) {
			$(".flights").addClass("none").removeClass("pick");
			return;
		}

		r.flights.forEach((flight, index) => {
			var $flight = $(".flight.dummy").clone(true, true);
			$flight.removeClass("dummy");
			$flight.attr("flight", index);
			
			flight.legs = transformFlightLeg(flight.legs);
			flight.prices = flight.prices["one-way"];
			flight.totalPrices = {};
			for(var i in flight.prices)
				flight.totalPrices[i] = flight.prices[i];
			if (!!flight.prices2) {
				flight.legs2 = transformFlightLeg(flight.legs2);
				flight.prices2 = flight.prices2["one-way"];
				for(var i in flight.prices2)
					flight.totalPrices[i] += flight.prices2[i];
				flight.return = {
					airline_id: flight.airline_id2,
					flightNumber: flight.flightNumber2,
					daysOfWeek: flight.daysOfWeek2,
					legs: flight.legs2,
					prices: flight.prices2
				}
				delete flight.airline_id2;
				delete flight.flightNumber2;
				delete flight.daysOfWeek2;
				delete flight.legs2;
				delete flight.prices2;
			}

			var proc = [[$flight.find(".block1"), flight]];
			if (flight.return) {
				$flight.addClass("roundtrip");
				proc.push([$flight.find(".block2"), flight.return]);
			}
			
			proc.forEach((pro) => {
				var $block = pro[0],
					flight = pro[1];

				$block.find(".airline").html(getAirlineName(flight.airline_id));
				$block.find(".airports").html(buildLegString(flight.legs));
				if (flight.legs.length == 1)
					$block.find(".stops").html("Direct");
				else if (flight.legs.length == 2)
					$block.find(".stops").html("1 Stop");
				else
					$block.find(".stops").html(flight.legs.length-1 + " Stops");
				$block.find(".bottomleft").html(buildOperates(flight.daysOfWeek));
				$block.find(".date").html(buildDepDate(flight.legs));
				$block.find(".time").html(buildTime(flight.legs));
				$block.find(".timerange").html(
					buildHumanTime(flight.legs[0].depTime) + " - " +
					buildHumanTime(flight.legs[flight.legs.length-1].arrTime));
			});
			
			updatePrice($flight.find(".price"), flight.totalPrices.economy);

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
		if ($(this).hasClass("disabled")) return;
		var index = parseInt($(this).closest(".flight").attr("flight"));
		var flight = flights[index],
			flightr = flight.return;
		activeFlight = flight;
		if (!!flightr)
			$(".bookFlight").addClass("showreturn");
		else
			$(".bookFlight").removeClass("showreturn");
		
		$(".bookFlight").addClass("show");
		$(".bookFlight .flightdetail.fa").html(getAirlineName(flight.airline_id)+" "+flight.flightNumber);
		if (flightr) {
			$(".bookFlight .flightdetail.fb").html(getAirlineName(flightr.airline_id)+" "+flightr.flightNumber);
		}
		$(".bookFlight .date.fa").html(buildDepDate(flight.legs)+"<span>"+
			buildHumanTime(flight.legs[0].depTime) + " - " +
			buildHumanTime(flight.legs[flight.legs.length-1].arrTime)+"</span>");
		if (flightr) {
			$(".bookFlight .date.fb").html(buildDepDate(flightr.legs)+"<span>"+
				buildHumanTime(flightr.legs[0].depTime) + " - " +
				buildHumanTime(flightr.legs[flightr.legs.length-1].arrTime)+"</span>");
		}
		$(".bookFlight .legs.fa").html(buildLegString(flight.legs));
		if (flightr) {
			$(".bookFlight .legs.fb").html(buildLegString(flightr.legs));
		}
		for (var i in flight.prices) {
			if (i == "economy")
				updatePrice($(".bookFlight .economy .price"), flight.totalPrices[i])
			else if (i == "first")
				updatePrice($(".bookFlight .firstclass .price"), flight.totalPrices[i])
		}
		if (TYPE == 1)
			$(".bookFlight .customer").trigger("change");
		else if (TYPE == 0) {
			$(".bookFlight .reservation option").not("[value=-1]").remove();
			makeCall("getreservations", {
				data: {account_id: ID},
				callBack : populateReservations
			});
		}
	});

	function populateReservations(r) {
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
		$(".bookFlight .reservation option").not("[value=-1]").remove();
		
		makeCall("getreservations", {
			data: {account_id: account_id},
			callBack : populateReservations
		});
	})

	var account_id;

	$(".bookFlight .book").click(function() {
		var flight = activeFlight;
		if (!flight) return;
		
		seats = parseInt($("#numberofseats").val());
		account_id = (TYPE == 1) ? parseInt($(".bookFlight .customer").val()) : ID;
		$(".bookFlight").removeClass("show");
		updatePassengersModal(true);
	});

	var passengers = [], seats = 0, userDetails = false,
		onPPage = 0;
	function getUserDetails() {
		makeCall("getaccounts", {
			data:{account_num:account_id},
			callBack : (r) => {
				if (r && r.accounts && r.accounts[0]) {
					userDetails = r.accounts[0];
					fillInUserDetails();
				}
			}
		})
	}
	function fillInUserDetails() {
		$(".passengers .firstName .value").val(userDetails.first_name);
		$(".passengers .lastName .value").val(userDetails.last_name);
		$(".passengers .address .value").val(userDetails.address);
		$(".passengers .state .value").val(userDetails.state);
		$(".passengers .city .value").val(userDetails.city);
		$(".passengers .zip .value").val(userDetails.zipcode);
	}
	var mapPassenger = ([
			["first_name", "firstName"],
			["last_name", "lastName"],
			["address", "address"],
			["city", "city"],
			["state", "state"],
			["zip", "zip"],
		]);
	function updatePassengersModal(justOpened=false) {
		if (justOpened) {
			passengers = [true];
			onPPage = 0;
			userDetails = false;
		}
		$(".passengers").addClass("show");
		$(".passengers .total").html(seats);
		$(".passengers .on").html(onPPage+1);
		$(".passengers .arrow").removeClass("hidden");
		if (onPPage == 0) $(".passengers .arrow.left").addClass("hidden");
		if (onPPage == seats - 1) $(".passengers .arrow.right").addClass("hidden");
		$(".passengers .accountToggle").attr("disabled", false);
		if (passengers[onPPage] === true) {
			$(".passengers .accountToggle").val("myaccount");
			$(".passengers .field .value").attr("disabled", true);
			if (!!userDetails)
				fillInUserDetails();
			else
				getUserDetails();
		} else {
			$(".passengers .accountToggle").val("fillin");
			$(".passengers .field .value").attr("disabled", false);
			if (!!passengers[onPPage]) {
				mapPassenger.forEach((a) => {
					$(".passengers ."+a[1]+" .value").val(passengers[onPPage][a[0]])
				});
			} else {
				$(".passengers .field .value").val("");
			}
		}
	}
	function savePassengerPage() {
		if (passengers[onPPage] !== true) {
			passengers[onPPage] = {};
			mapPassenger.forEach((a) => {
				passengers[onPPage][a[0]] = $(".passengers ."+a[1]+" .value").val();
			});
		}
	}
	$(".passengers .accountToggle").on("change", function() {
		if ($(this).val() == "myaccount") {
			passengers[onPPage] = true;
			updatePassengersModal();
		} else if ($(this).val() == "fillin") {
			passengers[onPPage] = false;
			updatePassengersModal();
		}
	});
	$(".passengers .arrow").click(function() {
		if ($(".passengers .book").hasClass("disabled")) return;
		savePassengerPage();
		onPPage += $(this).hasClass("left") ? -1 : 1;
		onPPage = Math.max(Math.min(onPPage, seats-1), 0);
		updatePassengersModal();
	});
	$(".passengers .book").click(function() {
		if ($(this).hasClass("disabled")) return;
		savePassengerPage();
		$(".passengers .message").removeClass("show ok people error");

		var error = false;
		for(var i=0;i<seats;i++) {
			if (!passengers[i]) {
				error = true;
				break;
			}
			if (passengers[i] !== true) {
				for(var j in passengers[i]) {
					if (passengers[i].hasOwnProperty(j))
						if (passengers[i][j].trim() == "") {
							error = true;
							break;
						}
				}
				if (!passengers[i].zip.match(/^[\d]{5}$/))
					error = true;
			}
			if (error) break;
		}
		if (error) {
			$(".passengers .message").addClass("show people");
			return;
		}
		
		var flight = activeFlight;
		if (!flight) return;

		var $this = $(this).addClass("disabled");
		$(".passengers .accountToggle, .passengers .value").attr("disabled", true);

		bookFlight(flight, !!flight.return);
	});

	function bookFlight(flight, bookReturn=false, reservation_id=-1) {
		var legs = [];
		for(var i=0;i<flight.legs.length;i++) legs.push(flight.legs[i].leg);
		legs = legs.join(" ");
		var date = flight.legs[0].depTime;
		date = date.getFullYear()+""+padTwo(1+date.getMonth())+""+padTwo(date.getDate());
		var priceClass = $(".bookFlight .prices .choice.selected").attr("selectprice"),
			price = flight.prices[priceClass];

		var data = {
			account_id : account_id,
			airline_id : flight.airline_id,
			flightNumber : flight.flightNumber,
			legNumber : legs,
			flightFare : price * seats,
			flightClass : priceClass,
			date : date,
		};
		if (TYPE == 1) {
			data.customer_rep_id = ID;
		}
		var res_id = parseInt($(".bookFlight .reservation").val())
		if (res_id > 0)
			data.reservation_id = res_id;
		if (reservation_id > -1)
			data.reservation_id = reservation_id;
		var persons = [];
		for(var i=0;i<seats;i++) {
			if (passengers[i] === true)
				persons.push({
					account_id: account_id
				});
			else
				persons.push(passengers[i]);
		}
		data.persons = JSON.stringify({
			persons : persons
		});
		

		makeCall("createreservation", {
			data: data,
			callBack : (r) => {
				if (r && r.reservation_id > -1) {
					if (bookReturn && flight.return) {
						bookFlight(flight.return, false, r.reservation_id);
					} else {
						$(".passengers .message").addClass("show ok");
						setTimeout(function() {
							if (TYPE == 0) {
								window.location.href = "/account.php?res="+r.reservation_id;
							} else if (TYPE == 1) {
								window.location.href = "/dashboard.php?res="+r.reservation_id;
							}
						},1000);
					}
				} else {
					$(".passengers .message").addClass("show error");
					$this.removeClass("disabled");
					updatePassengersModal();
				}
			}
		});
	}

























	getAirlines();

	/*
	$("#flyingfrom").attr("airport_id", "New York City,United States")
	$("#flyingto").attr("airport_id", "Los Angeles,United States")
	$(".return input").prop("checked", true);
	//$("#numberofseats").val(2);
	getFlights();
	//*/






	

});









