<?  
    $REQUIRETYPE = 0;
    require __DIR__."/common/base.php";
?>

<html>

	<head>

		<title>Account | SafeFlight</title>

		<link rel=icon href=/img/favicon.ico>
		<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
		<link rel="stylesheet" type="text/css" href="css/style.css" />

        <script type="text/javascript">var TYPE=<?=$TYPE?>;var ID=<?=$ID?>;</script>
		<script src="js/vendor/jmin.js"></script>
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
		<script src="js/script.js"></script>
        <script src="js/ajax.js"></script>
        <script src="js/profileedit.js"></script>
	</head>

	<body>
		
    	<?php $headerHighlight = "Account"; include __DIR__."/common/header.php"; ?>

    	<div id="container">
    		<div id="content">

                <?php if ($TYPE == 0) { ?>

                    <h1>Account</h1>

                    

                <?php } else if ($TYPE == 2) { ?>

                    <h1>Manager Dashboard</h1>

                <?php } ?>
    			


    		</div>
    	</div>

	</body>
	
</html>




















