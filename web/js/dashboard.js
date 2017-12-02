
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
		
		$(".account.top .column.edit span").click(function() {
			var account_id = parseInt($(this).closest(".account").attr("account-id")) || 0;
			if (account_id <= 0) return;

			var account = null;
			accounts.forEach((acc) => (account_id == acc.person_id) ? account=acc : null);
			console.log(account);
		})

		var accounts = [];
		makeCall(
			"getaccounts",
			{
				callBack : (r) => {
					accounts = r ? (r.accounts || []) : [];
					accounts.forEach((account) => {
						var $acc = $(".accounts .account.top").clone(true, true);
						$acc.removeClass("top");
						$acc.attr("account-id", account.person_id);

						$acc.find(".username").html(account.username);
						$acc.find(".email").html(account.email);
						$acc.find(".name").html(account.first_name+" "+account.last_name);
						$acc.find(".address").html(account.address+", "+account.city+", "+account.state+" "+account.zipcode)

						$('.accounts').append($acc);
					});
				}
			}
			);

		

	});
}

