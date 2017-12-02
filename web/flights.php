<?  
    
    require __DIR__."/common/base.php";
?>

<html>

	<head>

		<title>Flights | SafeFlight</title>

		<link rel=icon href=/img/favicon.ico>
		<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
		<link rel="stylesheet" type="text/css" href="css/style.css" />
		<link rel="stylesheet" type="text/css" href="css/flights.css" />

		<script src="js/vendor/jmin.js"></script>
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
		<script src="js/script.js"></script>
		<script src="js/flights.js"></script>
	</head>

	<body>
		
    	<?php $headerHighlight = "Flights"; include __DIR__."/common/header.php"; ?>

    	<div id="container">
    		<div id="content">

    			<h1>Flights</h1>

    			<div id="left">

    				<div class="label">I am looking for a flight:</div>

    				<div class="labelsmall">Flying from</div>
    				<input class="input locationinput" id="flyingfrom" />
    				<div class="labelsmall labelcloser">Flying to</div>
    				<input class="input locationinput" id="flyingto" />

    				<div class="labelsmall">Departing on</div>
    				<input class="input dateinput" id="departing" />

    				<div class="labelsmall">With Seats for</div>
    				<select class="input personinput" id="numberofseats">
    				</select>


    			</div>
    			<div id="right">
    				<div class="noflights">
    					<img src="img/papers.png" />
    					<div class="text">No flights were found</div>
    				</div>

    				<? for($i=0;$i<3;$i++) { ?>
    				<div class="flight">
    					<div class="upperleft">
    						<div class="column1 timerange">11:35am - 5:40pm</div>
    						<div class="column2 time">5h 40m</div>
    						<div class="stops">2 stops</div>
    					</div>
    					<div class="upperleft upperleft2">
    						<div class="column1 airline">JetBlue Airways</div>
    						<div class="column2 airports">JFK - LAS</div>
    					</div>
    					<div class="bottomleft">
    						Operates on: Monday, Wednesday, Friday
    					</div>
    					<div class="price">$125<span>.55</span></div>
    					<div class="select">Select</div>
    				</div>
    				<? } ?>

    			</div>


    		</div>
    	</div>

	</body>
	
</html>




















