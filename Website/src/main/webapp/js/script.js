
window.loaded = false;
window.loadedCb = [];
window.onLoaded = function(cb) {
	if (window.loaded) {
		cb()
	} else {
		loadedCb.push(cb)
	}
}
function loadAll() {
	window.loaded = true;
	for(var i=0;i<loadedCb.length;i++) {
		loadedCb[i]();
	}
}

$(function() {

	var loadNum = 0;
	
	$("[loadcontent]").each(function() {
		var $this = $(this);

		loadNum++;

		$this.load($this.attr("file"), function() {
			loadNum--;
			if (loadNum == 0) {
				loadAll();
			}
		});
	})

	if (loadNum == 0) {
		loadAll();
	}


	

});
