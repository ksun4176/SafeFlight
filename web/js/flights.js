

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
		setTimeout(function() {
			airports = [
				["Berlin Tegel","Berlin","Germany"],
				["Chicago O'Hare International","Chicago","Illinois"],
				["Hartsfield-Jackson Atlanta Int","Atlanta","United States of America"],
				["Ivato International","Antananarivo","Madagascar"],
				["John F. Kennedy International","New York","United States of America"],
				["LaGuardia","New York","United States of America"],
				["Logan International","Boston","United States of America"],
				["London Heathrow","London","United Kingdom"],
				["Los Angeles International","Los Angeles","United States of America"],
				["San Francisco International","San Francisco","United States of America"],
				["Tokyo International","Tokyo","Japan"]
			];
			for(var i=0;i<airportCb.length;i++) {
				airportCb[i](airports);
			}
			airportCb = [];
		}, 600);
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
				if (matchCriteria(a[0].toLowerCase()) || matchCriteria(a[1].toLowerCase()) || matchCriteria(a[2].toLowerCase()) ||
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

				return false;
			}

			if (key == 13) {
				if (selected < 0 || selected >= listed.length) return false;
				selectAirport(listed[selected]);
				return false;
			}

			getAirports((airports) => {
				populateDropdown($this.val());
			});
		});
		$this.on("change", function(e) {
			getAirports((airports) => {
				populateDropdown($this.val());
			});
		})
	});

	$("#departing").each(function() {
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


	var returnFlights = (r) => {
		$(".flights").removeClass("loading")
		if (!r.flights || !r.flights.length) {
			$(".flights").classClass("none");
			return;
		}

		r.flights.forEach((flight) => {console.log(flight);
			var $flight = $(".flight.dummy").clone(true, true);
			$flight.removeClass("dummy");

			$flight.find(".airline").html(flight.airline_id);
			$flight.find(".airports").html(flight.depAirportID+" - "+flight.arrAirportID);

			$(".flights").append($flight);
		})
	}
	function getFlights() {
		$(".flights").removeClass("none").addClass("loading");
		$(".flights .flight").not(".dummy").remove();

		var numOfSeats = parseInt($("#numberofseats").val());

		makeCall("getflights", {
			data: {
				seats: numOfSeats
			},
			callBack: returnFlights
		});
	}
	getFlights();












	

});
