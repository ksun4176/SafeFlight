
if (TYPE === 1) {
	$(function() {

		$("#miniheader span").click(function() {
			$("#mailinglist").addClass("show loading");
			$("#mailinglist textarea").val("");

			makeCall(
				"getmailinglist",
				{
					callBack : (r) => {
						$("#mailinglist").removeClass("loading");
						var emails = [];
						if (r.emails)
							r.emails.forEach((email) => {
								emails.push(email.Email);
							});
						$("#mailinglist textarea").val(emails.join(", "));
					}
				})
		});
		
		$(".accounts1 .account.top .column.edit span").click(function() {
			var account_id = parseInt($(this).closest(".account").attr("account-id")) || 0;
			if (account_id <= 0) return;

			var account = null;
			accounts.forEach((acc) => (account_id == acc.person_id) ? account=acc : null);
			editAccount(account, "Edit Account", true);
		})

		var accounts = [];
		makeCall(
			"getaccounts",
			{
				data : {
					customer_rep_id : ID
				},
				callBack : (r) => {
					accounts = r ? (r.accounts || []) : [];
					accounts.forEach((account) => {
						var $acc = $(".accounts1 .account.top").clone(true, true);
						$acc.removeClass("top");
						$acc.attr("account-id", account.person_id);

						$acc.find(".username").html(account.username);
						$acc.find(".email").html(account.email);
						$acc.find(".name").html(account.first_name+" "+account.last_name);
						$acc.find(".address").html(account.address+", "+account.city+", "+account.state+" "+account.zipcode)

						$('.accounts1').append($acc);
					});
				}
			}
			);

		

	});
}

else if (TYPE === 2) {
	$(function() {
		
		$(".accountse .account.top .column.edit span").click(function() {
			var employee_id = parseInt($(this).closest(".account").attr("employee-id")) || 0;
			if (employee_id <= 0) return;

			var employee = null;
			employees.forEach((acc) => (employee_id == acc.id) ? employee=acc : null);
			editAccount(employee, "Edit Employee", true, true);
		})

		

	});
}

	var employees = [];
	$(function() {

		
		makeCall(
			"getemployees",
			{
				data : {},
				callBack : (r) => {
					employees = r ? (r.employees || []) : [];
					employees.forEach((employee) => {
						var $acc = $(".accountse .account.top").clone(true, true);
						$acc.removeClass("top");
						$acc.attr("employee-id", employee.id);

						$acc.find(".username").html(employee.username);
						$acc.find(".email").html(employee.ssn);
						$acc.find(".name").html(employee.first_name+" "+employee.last_name);
						$acc.find(".address").html(employee.address+", "+employee.city+", "+employee.state+" "+employee.zipcode)

						$('.accountse').append($acc);
					});
				}
			}
			);
	});


if (DISPLAY_RES > -1) {
	$(function() {
		var res_id = DISPLAY_RES;
		
		$("#resit").addClass("show loading");
		makeCall("getreservation", {
			data: { reservation_id: res_id },
			callBack : (r) => {
				$("#resit .flight").not(".dummy").remove();
				$("#resit").removeClass("loading delete");
				$("#resit").attr("res-id", res_id);
				if (r) {
					$("#resit h2 .num").html(res_id);
					r.forEach((f) => {
						var $f = $("#resit .flight.dummy").clone(true, true);
						$f.removeClass("dummy");
						$f.find(".num").html(f.airline_id+""+f.flight_num);
						$f.find(".stops").html(buildLegString(f.legs, "DepAirportID", "ArrAirportID"));
						$f.find(".date").html(buildDate(f.DepTime));
						$f.find(".airline").html(getAirlineName(f.airline_id));
						$f.find(".time").html(buildHumanTime(f.DepTime)+" - "+buildHumanTime(f.ArrTime));
						f.legs.forEach((leg) => {
							$f.find(".expando").append(
								"<div><div>"+buildHumanTime(leg.DepTime)+" - "+buildHumanTime(leg.ArrTime)+"</div>"+
								"<span>"+getAirportName(leg.DepAirportID)+" - "+getAirportName(leg.ArrAirportID)+
								"</span></div>");
						});
						$f.insertAfter($("#resit .flight.dummy"));
					});
				}
			}
		})
	})
}








