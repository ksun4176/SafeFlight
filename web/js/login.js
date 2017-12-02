

$(function() {

	
	$("#gobutton").click(function() {

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

	

});
