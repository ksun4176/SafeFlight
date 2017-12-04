

$(function() {

	makeCall("getreservations", {
		data : {
			account_id : ID
			//reservation_id : 12
		},
		callBack : (r) => {
			console.log(r);
		}
	});













	

});






