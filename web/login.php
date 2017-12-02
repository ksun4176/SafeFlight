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
        <script src="js/login.js"></script>
	</head>

	<body>
		
    	<?php $headerHighlight = "Login"; include __DIR__."/common/header.php"; ?>

        <?php /*
        $.ajax("http://localhost:8080/Website/account/login", {method:"POST",dataType: 'json',data:{username:"rogli", password:"rogli"}})
        */ ?>

    	<div id="container">
    		<div id="content">

    			<h1>Login</h1>

                <div class="label">Username</div>
                <input class="loginInput" type="text" id="username" placeholder="username" />
                <div class="label">Password</div>
                <input class="loginInput" type="password" id="password" placeholder="password" />

                <input type="button" id="gobutton" value="Go" />

    		</div>
    	</div>

	</body>
	
</html>




















