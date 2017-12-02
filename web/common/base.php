<?php
	
	$ID = -1;
	$TYPE = -1;

	if (isset($_COOKIE["account_id"])) {
		$ID = (int)$_COOKIE["account_id"];
		if (isset($_COOKIE["account_type"])) {
			$TYPE = (int)$_COOKIE["account_type"];
		}
	}

	if (isset($REQUIRETYPE)) {
		if ($TYPE != $REQUIRETYPE) {
			die("Unauthorized");
		}
	}
	if (isset($DENYTYPE)) {
		if ($TYPE == $DENYTYPE) {
			die("Unauthorized");
		}
	}
	