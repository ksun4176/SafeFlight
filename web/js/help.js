$(function() {

	function clickThing() {
		$('.row.expand').removeClass('expand');
		$('.title.active').removeClass('active');

		var th = $(this).closest(".row");
		while(th.length) {
			th.addClass("expand");
			th = th.parent().closest(".row");
		}

		$(this).addClass("active");
		$("#right .show").removeClass("show");
		var sel = $(this).attr("sel");
		$("#"+sel).addClass("show");
	}

	function createSubMenu(help, $el) {
		help.forEach((h) => {
			var $row = $("<div></div>");
			$row.addClass("row");
			var id = h.name.toLowerCase().replace(/\s/g, "_");
			$row.append(
				$("<div></div>")
					.attr("sel", id)
					.addClass("title")
					.html(h.name)
					.click(clickThing),
				$("<div></div>")
					.addClass("children")
				);
			$el.append($row);

			if (h.children) {
				$row.find(".title").first().addClass("hasChildren");
				createSubMenu(h.children, $row.find(".children").first());
			}
		})
	}

	var h = JSON.parse(helps);
	createSubMenu(h, $("#left"));

	$("[sel=getting_started]").click();
	//var sel = $("#right > div").not($("#right [id='']")).last().attr("id")
	//$("[sel="+sel+"]").click();














});












