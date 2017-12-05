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
        <link rel="stylesheet" type="text/css" href="css/account.css" />

        <script type="text/javascript">var TYPE=<?=$TYPE?>;var ID=<?=$ID?>;</script>
        <script type="text/javascript">var DISPLAY_RES=<?=isset($_GET['res'])?$_GET['res']:-1?>;</script>
		<script src="js/vendor/jmin.js"></script>
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
		<script src="js/script.js"></script>
        <script src="js/ajax.js"></script>
        <script src="js/profileedit.js"></script>
        <script src="js/account.js"></script>
	</head>

	<body>
		
    	<?php $headerHighlight = "Account"; include __DIR__."/common/header.php"; ?>

        <?
            $ch = curl_init();
            curl_setopt($ch, CURLOPT_URL, "http://localhost:8080/Website/account/get?account_num=".$ID); 
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1); 
            $output = curl_exec($ch); 
            $output = json_decode($output);
            $acc = $output->accounts[0];
            $acc->name = $acc->first_name." ".$acc->last_name;
        ?>

    	<div id="container">
    		<div id="content">

                <h1><?=$acc->name?>'s Account</h1>

                <div class="editprofile">Edit Profile Information</div>
    			
                <h2>Reservations</h2>
                <select class="resfilter">
                    <option value='all' selected>All Reservations</option>
                    <option value='current'>Current Reservations</option>
                    <option value='past'>Past Reservations</option>
                </select>

                <div class="reservations">
                    <div class="reservation top" res-id="-1">
                        <div class="column edit"><span>view/edit</span></div>
                        <div class="column resno">Res #</div>
                        <div class="column date">Date</div>
                        <div class="column fare">Total Fare</div>
                        <div class="column fee">Booking Fee</div>
                        <div class="column address"></div>
                    </div>
                </div>

    		</div>
    	</div>

        <div class="modal" id="resit">
            <div>
                <div class="box">
                    <div class="spinner"><span></span></div>
                    <h2>Reservation <span class='num'>1</span> Itinerary</h2>
                    <div class="flight dummy">
                        <div class="row">
                            <div class="num">AA690</div>
                            <div class="stops">AA - BB - CC</div>
                        </div>
                        <div class="airline">American Airlines</div>
                        <div class="row row2">
                            <div class="date">Dec 19</div>
                            <div class="time">4:50pm-8:30pm</div>
                        </div>
                        <div class="expando"></div>
                    </div>
                    <div class="deleted">Cancelled Reservation</div>
                    <div class="button1 ok">Ok</div>
                    <div class="button1 delete">Cancel Reservation</div>
                </div>
            </div>
        </div>

	</body>
	
</html>




















