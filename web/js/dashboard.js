
var showRes = () => true;

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
			$(".listsalesreport .month").append("<option value='"+padTwo(i)+"' "+(i==12?"":"")+">"+i+"</option>")
		}
		for(var i=2012;i<=2020;i++) {
			$(".listsalesreport .year").append("<option value='"+i+"' "+(i==2017?"":"")+">"+i+"</option>")
		}
		$(".listsalesreport .date").on("change", function() {
			loadReports();
		});
		function loadReports() {
			var month = $(".listsalesreport .month").val(),
				year = $(".listsalesreport .year").val();
			if (month == -1 || year == -1) return;
			makeCall("getmonthlyreport", {
				data: {
					month: month,
					year : year
				},
				callBack : (r) => {
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

						$(".salesstats").html("Reservations: "+ress.length+"<br>"+
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
						$("select.flights").append(
							"<option value='"+thing+"''>"+thing+"</option");
					});

					if (FLIGHT_CUSTOMERS) {
						$(".listmanifest .flights").val(FLIGHT_CUSTOMERS).trigger("change");
						$(window).scrollTop($(".listmanifest .flights").offset().top);
						FLIGHT_CUSTOMERS = false;
					}
				}
			}
		});
		makeCall("getaccounts", {
			callBack: (r) => {
				if (r && r.accounts) {
					r.accounts.forEach((a) => {
						$("select.customers").append(
							"<option value="+a.person_id+">"+a.first_name+" "+a.last_name+" &lt;"+a.email+"&gt;</option");
					})
				}
			}
		});
		makeCall("getcities", {
			callBack: (r) => {
				if (r && r.cities) {
					r.cities.forEach((a) => {
						$("select.cities").append(
							"<option value=\""+a.city+","+a.country+"\">"+a.city+", "+ a.country+"</option");
					})
				}
			}
		});

		$(".listreservations select").on("change", function() {
			$(".listreservations select").not(this).val(-1);
			var val = $(this).val();
			if (val == -1) return;

			var data = {};
			if ($(this).hasClass("flights")) {
				data.flightNumber = parseInt(val.replace(/[^\d]+/g, ""));
				data.airline_id = (val.replace(/[\d]+/g, ""));
			} else {
				data.account_id = parseInt(val);
			}

			var reservations;
			makeCall("getreservations", {
				data : data,
				callBack : (r) => {
					if (!r || !r.reservations) return;
					reservations = r.reservations;
					for(var i=0;i<reservations.length;i++) {
						reservations[i].date = new Date(reservations[i].ResDate+" 12:00:00");
					}
					reservations = reservations.sort((a, b) => {
						var diff = b.date - a.date;
						if (diff == 0) return b.reservation_id - a.reservation_id;
						return diff;
					});
					showReservations(reservations);
				}
			});
		});
		$(".reservation .edit span").click(function() {
			var res_id = parseInt($(this).closest(".reservation").attr("res-id"));
			showRes(res_id);
		});
		function showReservations(ress) {
			$(".reservations .reservation").not(".top").remove();
			var $open = null;
			ress.forEach((res) => {
				var $res = $(".reservation.top").clone(true, true);
				$res.removeClass("top");
				if (res.reservation_id == DISPLAY_RES) $open = $res;
				$res.attr('res-id', res.reservation_id)
				$res.find(".resno").html(res.reservation_id);
				$res.find(".date").html(res.date.getMonth()+1+"/"+res.date.getDate()+"/"+(res.date.getFullYear()-2000));
				$res.find(".fare").html("$"+res.totalFare.toFixed(2));
				$res.find(".fee").html("$"+res.bookingFee.toFixed(2));
				$(".reservations").append($res);
			});
		}
		showRes = (res_id) => {
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
		}

		$(".mostrevenue select").on("change", function() {
			var val = $(this).val();
			if (val == -1) return;
			makeCall(val, {
				callBack : (r) => {
					if (!r) return;
					var arr, mapFields;
					if (r.customer) {
						arr = r.customer;
						mapFields = ["email"];
						$(".mostrevenues").removeClass("custrep");
					} else if (r.custReps) {
						arr = r.custReps;
						arr.forEach((a) => {
							var time = new Date(a.startDate + " 12:00:00");
							a.start = padTwo(time.getMonth()+1)+"/"+padTwo(time.getDate())+"/"+(time.getYear()-100);
						})
						mapFields = ["hourlyRate", "start"];
						$(".mostrevenues").addClass("custrep");
					}
					arr = arr.sort((a, b) => b.revenue - a.revenue);
					if (!arr || !mapFields) return;
					$(".mostrevenues .mostrevenue").not(".top").remove();
					arr.forEach((a) => {
						var $row = $(".mostrevenue.top").clone(true, true);
						$row.removeClass("top");
						$row.find(".name").html(a.firstName+" "+a.lastName);
						$row.find(".revenue").html("$"+a.revenue.toFixed(2));
						$row.find(".address").html(a.address+", "+a.city+", "+a.state+" "+a.zipCode);
						mapFields.forEach((f) => {
							$row.find("."+f).html(a[f]);
						})

						$(".mostrevenues").append($row);
					});
				}
			});
		});

		$(".listrevenuesummary select").on("change", function() {
			$(".listrevenuesummary select").not(this).val(-1);
			var val = $(this).val();
			if (val == -1) return;

			var data = {};
			if ($(this).hasClass("flights")) {
				data.flightNumber = parseInt(val.replace(/[^\d]+/g, ""));
				data.airlineID = (val.replace(/[\d]+/g, ""));
			} else if ($(this).hasClass("cities")) {
				data.toCity = (val);
			} else {
				data.account_id = parseInt(val);
			}

			makeCall("getrevenue", {
				data : data,
				callBack : (r) => {
					if (r && r.hasOwnProperty("Revenue")) {
						$(".revenuesummary").html("Revenue Generated: $"+r.Revenue.toFixed(2));
					}
				}
			});
		});

		$(".listmanifest select").on("change", function() {
			var val = $(this).val();
			if (val == -1) return;

			var data = {};
			data.flightNumber = parseInt(val.replace(/[^\d]+/g, ""));
			data.airlineId = (val.replace(/[\d]+/g, ""));
			makeCall("getmanifest", {
				data : data,
				callBack : (r) => {
					if (!r || !r.customers) return;
					var cs = [];
					r.customers.forEach((c) => {
						if (!cs.find((a) => a.account_id==c.account_id))
							cs.push(c);
					});

					$(".manifests .manifest").not(".top").remove();
					cs.forEach((a) => {
						var $row = $(".manifest.top").clone(true, true);
						$row.removeClass("top");
						$row.find(".name").html(a.firstName+" "+a.lastName);
						$row.find(".address").html(a.Address+", "+a.city+", "+a.state+" "+a.zipCode);

						$(".manifests").append($row);
					});
				}
			});
		});


		

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

		showRes(res_id);
	})
}








