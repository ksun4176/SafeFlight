

$(function() {

	var reservations;
	makeCall("getreservations", {
		data : {
			account_id : ID
			//reservation_id : 12
		},
		callBack : (r) => {
			if (!r || !r.reservations) return;
			reservations = r.reservations;
			for(var i=0;i<reservations.length;i++) {
				reservations[i].date = new Date(reservations[i].ResDate+" 12:00:00");
			}
			reservations = reservations.sort((a, b) => {
				var diff = b.date - a.date;
				if (diff == 0) return b.reservation_id - a.reservation_id;
				return diff;
			});
			showReservations(reservations);
		}
	});

	$(".resfilter").on('change', function() {
		var val = $(this).val();
		if (val == 'all') showReservations(reservations);
		var today = new Date();
		if (val == 'current') showReservations(reservations.filter( (res) => res.date >= today ));
		if (val == 'past') showReservations(reservations.filter( (res) => res.date < today ));
	});

	$(".reservation .edit span").click(function() {
		var res_id = parseInt($(this).closest(".reservation").attr("res-id"));
		$("#resit").addClass("show loading");

		makeCall("getreservation", {
			data: { reservation_id: res_id },
			callBack : (r) => {
				$("#resit .flight").not(".dummy").remove();
				$("#resit").removeClass("loading delete");
				$("#resit").attr("res-id", res_id);
				if (r) {
					$("#resit h2 .num").html(res_id);
					r.forEach((f) => {
						var $f = $("#resit .flight.dummy").clone(true, true);
						$f.removeClass("dummy");
						$f.find(".num").html(f.airline_id+""+f.flight_num);
						$f.find(".stops").html(buildLegString(f.legs, "DepAirportID", "ArrAirportID"));
						$f.find(".date").html(buildDate(f.DepTime));
						$f.find(".airline").html(getAirlineName(f.airline_id));
						$f.find(".time").html(buildHumanTime(f.DepTime)+" - "+buildHumanTime(f.ArrTime));
						f.legs.forEach((leg) => {
							$f.find(".expando").append(
								"<div><div>"+buildHumanTime(leg.DepTime)+" - "+buildHumanTime(leg.ArrTime)+"</div>"+
								"<span>"+getAirportName(leg.DepAirportID)+" - "+getAirportName(leg.ArrAirportID)+
								"</span></div>");
						});
						$f.insertAfter($("#resit .flight.dummy"));
					});
				}
			}
		})
	})
	getAirlines();
	getAirports();
	$("#resit .flight").click(function() {
		$(this).toggleClass("expand");
	})
	$("#resit .ok").click(function() {
		if ($(this).hasClass("disabled")) return;
		$(this).closest(".modal").removeClass("show");
	})
	$("#resit .delete").click(function() {
		if ($(this).hasClass("disabled")) return;
		var res_id = parseInt($("#resit").attr("res-id"));
		if (!res_id) return;
		$("#resit .button1").addClass("disabled");

		makeCall("deletereservation",{
			data: {reservation_id: res_id},
			callBack : (ok) => {
				if (ok) {
					$("#resit").addClass("delete");
					setTimeout(() => {
						window.location.href = location.href;
					}, 1000);
				}
			}
		});
	})

	function showReservations(ress) {
		$(".reservations .reservation").not(".top").remove();
		var $open = null;
		ress.forEach((res) => {
			var $res = $(".reservation.top").clone(true, true);
			$res.removeClass("top");
			if (res.reservation_id == DISPLAY_RES) $open = $res;
			$res.attr('res-id', res.reservation_id)
			$res.find(".resno").html(res.reservation_id);
			$res.find(".date").html(res.date.getMonth()+1+"/"+res.date.getDate()+"/"+(res.date.getFullYear()-2000));
			$res.find(".fare").html("$"+res.totalFare.toFixed(2));
			$res.find(".fee").html("$"+res.bookingFee.toFixed(2));
			$(".reservations").append($res);
		});

		if (!!$open) {
			DISPLAY_RES = -1;
			$open.find(".edit span").click();
		}
	}

	var account;
	makeCall("getaccounts", {
		data : {
			account_num : ID
		},
		callBack : (r) => {
			if (r && r.accounts && r.accounts.length) {
				account = r.accounts[0];
				if (EDIT_PROFILE)
					$(".editprofile").click();
			}
		}
	});

	$(".editprofile").click(function() {
		if (!account) return;
		editAccount(account, "Edit My Profile");
	});













	

});






