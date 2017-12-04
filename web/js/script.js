
var modal = {
	initModal : ($modal) => {
		if ($modal.attr("init")) return;
		$modal.attr("init", true);
		$modal.click(function(e) {
			if ($(this).find(".box").parent().find(e.target).length == 0) {
				$(this).removeClass("show");
			}
		})
	}
}

function toHumanReadableTime(time) {
	var t = time;
	if (typeof time == "string")
		t = new Date(time);
}

function transformFlightLeg(leg) {
	var l = [];
	for(var i in leg) {
		if (isNaN(i)) continue;
		l[i] = leg[i];
		l[i].arrTime = new Date(l[i].arrTime);
		l[i].depTime = new Date(l[i].depTime);
	}
	l.shift();
	return l;
}


$(function() {

	$(".modal").each(function() {
		modal.initModal($(this));
	})
	

	

});

var getAirlines = () => true;
var getAirlineName = () => "";
$(function() {
	var madeCall = false;
	var airlines = {};
	getAirlines = () => {
		if (madeCall) return;
		madeCall = true;
		makeCall("getairlines", {callBack:(r) => {
			if (r && r.airlines) {
				for(var i=0;i<r.airlines.length;i++) {
					airlines[r.airlines[i].airline_id] = r.airlines[i].name;
				}
			}
		}})
	}
	getAirlineName = (id) => {
		if (airlines.hasOwnProperty(id)) return airlines[id];
		return "";
	}

})







