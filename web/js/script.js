
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

$(function() {

	$(".modal").each(function() {
		modal.initModal($(this));
	})
	
	

	

});
