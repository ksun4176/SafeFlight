<?  

    require __DIR__."/common/base.php";
?>

<html>

	<head>

		<title>Help | SafeFlight</title>

		<link rel=icon href=/img/favicon.ico>
		<link rel="stylesheet" type="text/css" href="css/style.css" />
        <link rel="stylesheet" type="text/css" href="css/help.css" />

		<script src="js/vendor/jmin.js"></script>
		<script src="js/script.js"></script>
        <script src="js/help.js"></script>
	</head>

	<body>
		
    	<?php $headerHighlight = "Help"; include __DIR__."/common/header.php"; ?>

        <?php
            global $help;
            $outputcontainers = array();
            $help = array(
                (object)array(
                    "name" => "Getting Started",
                    "children" => array(
                        (object)array(
                            "name" => "Creating an Account"
                        ),
                        (object)array(
                            "name" => "Logging In"
                        ),
                    )
                ),
                (object)array(
                    "name" => "Flights",
                    "children" => array(
                        (object)array(
                            "name" => "Browsing Flights",
                            "children" => array(
                                (object)array(
                                    "name" => "Searching Flights"
                                ),
                                (object)array(
                                    "name" => "Personal Suggestions"
                                ),
                                (object)array(
                                    "name" => "Best Sellers"
                                ),
                            )
                        ),
                        (object)array(
                            "name" => "Booking a Flight"
                        ),
                    )
                ),
                (object)array(
                    "name" => "Reservations",
                    "children" => array(
                        (object)array(
                            "name" => "See Your Reservations"
                        ),
                    )
                )
            );
            echo "<script type='text/javascript'>var helps = '".json_encode($help)."';</script>";
        ?>

    	<div id="container">
    		<div id="content">
                <div id="left">

                </div>
                <div id="right">
                    <div id="getting_started">
                        How to get started 
                    </div>
                    <div id="creating_an_account">
                        Creating an account
                    </div>
                    <div id="logging_in">
                        Logging in
                    </div>
                </div>

    		</div>
    	</div>

	</body>
	
</html>




















