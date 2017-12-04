<?  
    $DENYTYPE = 0;
    require __DIR__."/common/base.php";
?>

<html>

	<head>

		<title>Dashboard | SafeFlight</title>

		<link rel=icon href=/img/favicon.ico>
		<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
		<link rel="stylesheet" type="text/css" href="css/style.css" />
        <link rel="stylesheet" type="text/css" href="css/dashboard.css" />

        <script type="text/javascript">var TYPE=<?=$TYPE?>;var ID=<?=$ID?>;</script>
		<script src="js/vendor/jmin.js"></script>
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
		<script src="js/script.js"></script>
        <script src="js/ajax.js"></script>
        <script src="js/profileedit.js"></script>
        <script src="js/dashboard.js"></script>
	</head>

	<body>
		
    	<?php $headerHighlight = "Dashboard"; include __DIR__."/common/header.php"; ?>

    	<div id="container">
    		<div id="content">

                <?php if ($TYPE == 1) { ?>

                    <h1>Customer Representative Dashboard</h1>

                    <div id="miniheader">
                        Customers
                        <span>Get mailing list</span>
                    </div>

                    <div class="modal" id="mailinglist">
                        <div>
                            <div class="box">
                                <div class="spinner"><span></span></div>
                                <h2>Mailing List</h2>
                                <textarea></textarea>
                            </div>
                        </div>
                    </div>

                    <div class="accounts">
                        <div class="account top" account-id="-1">
                            <div class="column edit"><span>edit</span></div>
                            <div class="column username">Username</div>
                            <div class="column name">Name</div>
                            <div class="column email">Email</div>
                            <div class="column address">Address</div>
                        </div>
                    </div>

                <?php } else if ($TYPE == 2) { ?>

                    <h1>Manager Dashboard</h1>

                <?php } ?>
    			


    		</div>
    	</div>

	</body>
	
</html>



















