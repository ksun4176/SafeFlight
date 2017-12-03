
var editAccount = () => {};

$(function() {

	var $modal = $("<div>")
		.addClass("modal");
	$modal.append("<div><div class='box'></div></div>");
	var $box = $modal.find(".box");
	$box.css({
		padding:"20px 25px"
	});
	$("body").append($modal);

	modal.initModal($modal);

	editAccount = (account) => {
		$modal.addClass("show");



		console.log(account);

	};




});

