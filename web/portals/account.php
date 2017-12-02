<?php
	
	if (isset($_REQUEST["logout"])) {
		setcookie("account_id", "", time() - 60, "/", "", 0);
		setcookie("account_type", "", time() - 60, "/", "", 0);

		header( 'Location: /login.php' );
		exit;
	}

	$account_id = isset($_POST["account_id"]) ? (int)$_POST["account_id"] : -1;
	if ($account_id < 0) exit;

	$account_type = isset($_POST["account_type"]) ? (int)$_POST["account_type"] : -1;
	if ($account_type < 0 || $account_type > 2) exit;

	$monthsec = 60 * 60 * 24 * 30;

	setcookie("account_id", $account_id, time()+$monthsec, "/", "", 0);
	setcookie("account_type", $account_type, time()+$monthsec, "/", "", 0);

	header( 'Location: /login.php' );