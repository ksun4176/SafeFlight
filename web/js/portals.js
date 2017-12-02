
var portal = () => {};

$(function() {

	portal = function(portalName, data, method="POST") {
		var form = $("<form>");
		form.css("display", "none");

		form.attr({
			method: method,
			action: "/portals/"+portalName+".php"
		});

		for(var key in data) {
			if (!data.hasOwnProperty(key)) continue;
			var inp = $("<input>");
			inp.attr("name", key);
			inp.attr("value", data[key]);
			form.append(inp);
		}

		$("body").append(form);
		form.submit();
	}

});


