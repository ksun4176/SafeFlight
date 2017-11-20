
onLoaded(function() {

	$(window).scroll(function() {
		var top = $(document).scrollTop();

		if (top > 150)
			$("header").removeClass("home")
		else
			$("header").addClass("home")
	}).trigger("scroll")

	$("header .logo").append(
		$("<div>").addClass("trail t2")
		);




});
