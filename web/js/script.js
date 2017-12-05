
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
		l[i].leg = i;
	}
	l.shift();
	return l;
}

var months = "Jan,Feb,Mar,Apr,May,Jun,Jul,Aug,Sep,Oct,Nov,Dec".split(",");
function buildDate(time) {
	return months[(time.getMonth())]+" "+time.getDate();
}
function padTwo(a) {return (a < 10) ? ("0"+a) : (""+a);}
function buildHumanTime(date) {
	var hour = date.getHours(), ampm = "am";
	if (hour >= 12) { hour -= 12; ampm = "pm"; }
	if (hour == 0) hour = 12;
	return (hour)+":"+padTwo(date.getMinutes())+ampm;
}
function buildLegString(l, a="depAirportID", b="arrAirportID") {
	var str = "";
	for(var i=0;i<l.length;i++) str += l[i][a]+" - ";
	str += l[l.length-1][b];
	return str;
}


$(function() {

	$(".modal").each(function() {
		modal.initModal($(this));
	})
	

	

});

var getAirlines = () => true;
var getAirlineName = () => "";
var getAirports = () => true;
var getAirportName = () => "";
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
$(function() {
	var madeCall = false;
	var airports = {};
	getAirports = () => {
		if (madeCall) return;
		madeCall = true;
		makeCall("getairports", {callBack:(r) => {
			if (r && r.airports) {
				for(var i=0;i<r.airports.length;i++) {
					airports[r.airports[i].airport_id] = r.airports[i].name;
				}
			}
		}})
	}
	getAirportName = (id) => {
		if (airports.hasOwnProperty(id)) return airports[id];
		return "";
	}
})







