$(function() {


	function createSubMenu(help, $el) {
		help.forEach((h) => {
			var $row = $("<div></div>");
			$row.addClass("row");
			$row.append(
				$("<div></div>")
					.addClass("title")
					.html(h.name),
				$("<div></div>")
					.addClass("children")
				);
			$el.append($row);

			if (h.children) {
				createSubMenu(h.children, $row.find(".children").first());
			}
		})
	}

	var h = JSON.parse(helps);
	createSubMenu(h, $("#left"));
















});












