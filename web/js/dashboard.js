
if (TYPE === 1) {
	$(function() {

		$(".getmailinglist").click(function() {
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

					if (EDIT_ACCOUNT > -1) {
						$(".accounts1 .account[account-id="+EDIT_ACCOUNT+"] .edit span").click();
						EDIT_ACCOUNT = -1;
					}
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


		for(var i=1;i<=12;i++) {
			$("#salesreport .month").append("<option value='"+i+"' "+(i==12?"selected":"")+">"+i+"</option>")
		}
		for(var i=2010;i<=2017;i++) {
			$("#salesreport .year").append("<option value='"+i+"' "+(i==2017?"selected":"")+">"+i+"</option>")
		}
		$("#salesreport .date").on("change", function() {
			loadReports();
		});
		$(".salesreport").click(function() {
			$("#salesreport").addClass("show");
			loadReports();
		})
		function loadReports() {
			var month = $("#salesreport .month").val(),
				year = $("#salesreport .year").val();
			$("#salesreport").addClass("loadingsales");
			makeCall("getmonthlyreport", {
				data: {
					month: month,
					year : year
				},
				callBack : (r) => {
					$("#salesreport").removeClass("loadingsales");
					if (r && r.reservations) {
						var ress = r.reservations;
						var totalFare = 0, totalFee = 0;
						var accs = [], reps = [];
						ress.forEach((res) => {
							totalFare += res.totalFare;
							totalFee += res.bookingFee;
							if (accs.indexOf(res.account_id) == -1) accs.push(res.account_id);
							if (reps.indexOf(res.customer_rep_id) == -1) reps.push(res.customer_rep_id);
						});

						$("#salesreport .stats").html("Reservations: "+ress.length+"<br>"+
							"Total Fare: $"+totalFare.toFixed(2)+"<br>"+
							"Total Booking Fees: $"+totalFee.toFixed(2)+"<br>"+
							"Active Customers: "+accs.length+"<br>"+
							"Active Customer Representatives: "+reps.length+"<br>"
							);
					}
				}
			})
		}

		makeCall("getflights", {
			data: {
				all : true
			},
			callBack: (r) => {
				if (r && r.flights) {
					r.flights.forEach((f) => {
						var thing = f.airline_id + "" + f.flightNumber;
						$(".listreservations .flights").append(
							"<option value='"+thing+"''>"+thing+"</option");
					})
				}
			}
		});
		makeCall("getaccounts", {
			callBack: (r) => {
				if (r && r.accounts) {
					r.accounts.forEach((a) => {
						$(".listreservations .customers").append(
							"<option value="+a.person_id+">"+a.first_name+" "+a.last_name+" &lt;"+a.email+"&gt;</option");
					})
				}
			}
		});
		$(".listreservations select").on("change", function() {
			$(".listreservations select").not(this).val(-1);
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

				if (TYPE == 2 && EDIT_ACCOUNT > -1) {
					$(".accountse .account[employee-id="+EDIT_ACCOUNT+"] .edit span").click();
					EDIT_ACCOUNT = -1;
				}
			}
		}
		);



	$(".createaccountclick").click(function() {
		$("#createaccount").addClass("show");
		$("#createaccount .input").val("");
		$("#createaccount .message").removeClass("show ok error empty exist");
	});

	$("#createaccount .button").click(function() {
		var data = {};
		$("#createaccount .message").removeClass("show ok error empty exist");

		var readFields = [],
			call;
		if (TYPE == 1) {
			call = "createaccount";
			data.customer_rep_id = ID;
			readFields = ["username", "password", "firstName", "lastName"];
			data.address = data.city = data.state = data.email = data.zip = data.creditCardNo = "";
		} else if (TYPE == 2) {
			call = "createemployee";
			readFields = ["username", "password", "firstName", "lastName", "ssn"];
			data.address = data.city = data.state = data.zip = "";
			data.hourlyRate = 0;
		}
		for(var i=0;i<readFields.length;i++) {
			var f = readFields[i];
			data[f] = $("#createaccount .input."+f).val().trim();
			if (data[f] == "") {
				$("#createaccount .message").addClass("show error empty");
				return;
			}
		}

		var $this = $(this).addClass("disabled");
		$("#createaccount .input").attr("disabled", true);
		makeCall(call, {
			data: data,
			callBack : (r) => {
				if (r) {
					var field = (TYPE == 2) ? "employee_id" : "account_id";
					if (r[field] > 0) {
						$("#createaccount .message").addClass("show ok");
						setTimeout(function() {
							window.location.href = "/dashboard.php?acc="+r[field];
						}, 1000);
					} else {
						$this.removeClass("disabled");
						$("#createaccount .message").addClass("show error exist");
						$("#createaccount .input").attr("disabled", false);
					}
				} else {
					$this.removeClass("disabled");
					$("#createaccount .message").addClass("show error");
					$("#createaccount .input").attr("disabled", false);
				}
			}
		})
	});

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








