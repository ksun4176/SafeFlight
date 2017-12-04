

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
	})

	function showReservations(ress) {
		$(".reservations .reservation").not(".top").remove();
		ress.forEach((res) => {
			console.log(res);
			var $res = $(".reservation.top").clone(true, true);
			$res.removeClass("top");
			$res.attr('res-id', res.reservation_id)
			$res.find(".resno").html(res.reservation_id);
			$res.find(".date").html(res.date.getMonth()+1+"/"+res.date.getDate()+"/"+(res.date.getFullYear()-2000));
			$res.find(".fare").html("$"+res.totalFare.toFixed(2));
			$res.find(".fee").html("$"+res.bookingFee.toFixed(2));
			$(".reservations").append($res);
		})
	}

	var account;
	makeCall("getaccounts", {
		data : {
			account_num : ID
		},
		callBack : (r) => {
			if (r && r.accounts && r.accounts.length)
				account = r.accounts[0];
		}
	});

	$(".editprofile").click(function() {
		if (!account) return;
		editAccount(account, "Edit My Profile");
	});













	

});






