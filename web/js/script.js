

$(function() {

	$(".modal").click(function(e) {
		if ($(this).find(".box").parent().find(e.target).length == 0) {
			$(this).removeClass("show");
		}
	})
	
	

	

});
