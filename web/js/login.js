

$(function() {

	$(".loginInput").on("keydown", function(e) {
		var key = e.keyCode || e.which;
		if (key == 13) {
			e.preventDefault();
			$(".loginbutton").click();
			return false;
		}
	});

	$(".loginbutton").click(function() {

		$("input").attr("disabled", true);
		$(".error").removeClass("show");

		makeCall("login", {
			data: {
				username: $("#username").val(),
				password: $("#password").val()
			},
			callBack : (r) => {
				if (!r || r.account_id == -1) {
					$("input").attr("disabled", false);
					$(".error").addClass("show");
					return;
				}
				$(".error").html("Successfully signed in!").addClass("ok show");

				setTimeout(() => {
					portal("account",
						{
							account_id: r.account_id,
							account_type: r.accountType
						});
				}, 1000);
			}
		})
	})

	$(".signupbutton").click(function() {
		var data = {};

		var readFields = ["username", "password", "firstName", "lastName"];
		data.address = data.city = data.state = data.email = data.zip = data.creditCardNo = "";
		for(var i=0;i<readFields.length;i++) {
			var f = readFields[i];
			data[f] = $("#"+f).val().trim();
			if (data[f] == "") {
				$(".error").addClass("show").html("Please fill everything out");
				return;
			}
		}
		if ($("#password").val() != $("#password2").val()) {
			$(".error").addClass("show").html("Your passwords don't match");
			return;
		}

		$("input").attr("disabled", true);
		$(".error").removeClass("show");

		makeCall("createaccount", {
			data: data,
			callBack : (r) => {
				if (r) {
					if (r.account_id > 0) {
						$(".error").addClass("show ok").html("Successfully signed up!");

						setTimeout(() => {
							portal("account",
								{
									account_id: r.account_id,
									account_type: 0,
									editprof : true
								});
						}, 1000);
					} else {
						$(".error").addClass("show").html("Username taken");
						$("input").attr("disabled", false);
					}
				} else {
					$(".error").addClass("show").html("Unable to create account");
					$("input").attr("disabled", false);
				}
			}
		})
	})

	

});
