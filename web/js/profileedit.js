
var editAccount = () => {};

$(function() {

	var emp = false;

	var $modal = $("<div>")
		.addClass("modal editAccount");
	$modal.append("<div><div class='box'></div></div>");
	var $box = $modal.find(".box");
	function buildThing(className, label, placeholder="") {
		return $("<div>")
			.addClass("field "+className, label, placeholder)
			.append(
				$("<div class='label'>").html(label),
				$("<input class='value' />").attr("placeholder", placeholder)
				);
	}
	$box.append(
		$("<h2></h2>"),
		$("<div>").addClass("name").append(
			buildThing("firstName", "First Name"),
			buildThing("lastName", "Last Name")
			),
		buildThing("email", "Email"),
		buildThing("address", "Address"),
		$("<div>").addClass("address2").append(
			buildThing("city", "City"),
			buildThing("state", "State"),
			buildThing("zip", "Zip Code")
			),
		buildThing("creditcard", "Credit Card"),
		$("<div class='error'></div>"),
		$("<div class='submit button'>Update Information</div>"),
		$("<div class='delete button'>Delete Account</div>"),
		);
	$("body").append($modal);

	modal.initModal($modal);
	$modal.find(".creditcard .value").on("focus", function() {
		$(this).attr("type", "text");
	}).on("blur", function() {
		$(this).attr("type", "password");
	});

	var acc;
	$modal.find(".submit").click(function() {
		if ($(this).hasClass("disabled")) return;
		$modal.find(".value").attr("disabled", true);
		var $this = $modal.find(".button").addClass("disabled");

		var call = emp ? "editemployee" : "editaccount";
		var data = {
			account_id: acc.person_id,
			firstName: $(".modal").find(".firstName .value").val(),
			lastName: $(".modal").find(".lastName .value").val(),
			address: $(".modal").find(".address .value").val(),
			city: $(".modal").find(".city .value").val(),
			state: $(".modal").find(".state .value").val(),
			zip: $(".modal").find(".zip .value").val(),
			email: $(".modal").find(".email .value").val(),
			creditCardNo: $(".modal").find(".creditcard .value").val(),
		}
		if (emp) {
			data.hourlyRate = data.email;
			data.account_id = acc.id;
		}

		makeCall(
			call,
			{
				data: data,
				callBack : (r) => {
					if (r && r.ok) {
						$modal.find(".error").addClass("show ok");
						setTimeout(() => {
							window.location.href = window.location.href;
						}, 1000);
					} else {
						$modal.find(".value").attr("disabled", false);
						$this.removeClass("disabled");
						$modal.find(".error").addClass("show");
					}
				}
			});
	});

	editAccount = (account, title="Edit Account", showdelete=false, employee=true) => {
		emp = employee;
		acc = account;
		$modal.addClass("show");
		$modal.find(".error").removeClass("show ok");
		$modal.find("h2").html(title);
		$modal.find(".creditcard .value").attr("type", "password");
		$modal.find(".delete").css("display", showdelete?"inline-block":"none");
		if (!employee) {
			$modal.find(".email .label").html("Email");
			$modal.find(".creditcard").css("display", "block");
			([
				["firstName", "first_name"],
				["lastName", "last_name"],
				["email", "email"],
				["address", "address"],
				["city", "city"],
				["state", "state"],
				["zip", "zipcode"],
				["creditcard", "credit_card_num"],
			]).forEach((a) => {
				$modal.find("."+a[0]+" .value").val(account[a[1]]);
			});
		} else {
			$modal.find(".email .label").html("Hourly Rate");
			$modal.find(".creditcard").css("display", "none");
			([
				["firstName", "first_name"],
				["lastName", "last_name"],
				["email", "hourly_rate"],
				["address", "address"],
				["city", "city"],
				["state", "state"],
				["zip", "zipcode"],
			]).forEach((a) => {
				$modal.find("."+a[0]+" .value").val(account[a[1]]);
			});
		}
	};




});

