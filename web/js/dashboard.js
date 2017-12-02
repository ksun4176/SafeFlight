
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
		})
		
		

	});
}

