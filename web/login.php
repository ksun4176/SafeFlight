<?  

    require __DIR__."/common/base.php";
?>

<html>

	<head>

		<title>Login | SafeFlight</title>

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
		
    	<?php $headerHighlight = "Login"; include __DIR__."/common/header.php"; ?>

        <?php /*
username=kevsun,password=kevsun
username=timsitorus,password=timsitorus
username=wawong,password=wawong
username=rogli,password=rogli
username=alibanana,password=alibanana
username=cardoom,password=cardoom
username=eufap,password=eufap
username=grahugme,password=grahugme
1: manager
2-4: cust rep
5-8: cust
        */ ?>

    	<div id="container">
    		<div id="content">

    			<h1>Login</h1>

                <div class="label">Username</div>
                <input class="loginInput" type="text" id="username" placeholder="username" value="kevsun" />
                <div class="label">Password</div>
                <input class="loginInput" type="password" id="password" placeholder="password" value="kevsun" />

                <div class="error">Invalid Login Credentials</div>

                <input type="button" id="gobutton" value="Go" />

    		</div>
    	</div>

	</body>
	
</html>




















