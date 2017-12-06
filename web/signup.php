<?  

    require __DIR__."/common/base.php";
?>

<html>

	<head>

		<title>Sign Up | SafeFlight</title>

		<link rel=icon href=/img/favicon.ico>
		<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
		<link rel="stylesheet" type="text/css" href="css/style.css" />
        <link rel="stylesheet" type="text/css" href="css/login.css" />

		<script src="js/vendor/jmin.js"></script>
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
		<script src="js/script.js"></script>
        <script src="js/ajax.js"></script>
        <script src="js/portals.js"></script>
        <script src="js/login.js"></script>
	</head>

	<body>
		
    	<?php $headerHighlight = "Signup"; include __DIR__."/common/header.php"; ?>


    	<div id="container">
    		<div id="content">

    			<h1>Sign Up</h1>

                <div class="label">First Name</div>
                <input class="loginInput" type="text" id="firstName" placeholder="first name" value="" />
                <div class="label">Last Name</div>
                <input class="loginInput" type="password" id="lastName" placeholder="last name" value="" />
                <div class="label">Username</div>
                <input class="loginInput" type="text" id="username" placeholder="username" value="" />
                <div class="label">Password</div>
                <input class="loginInput" type="password" id="password" placeholder="password" value="" />
                <div class="label">Confirm Password</div>
                <input class="loginInput" type="password" id="password2" placeholder="password" value="" />

                <div class="error">Invalid Login Credentials</div>

                <input type="button" id="gobutton" class="signupbutton" value="Go" />

    		</div>
    	</div>

	</body>
	
</html>




















