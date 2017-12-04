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

        <script type="text/javascript">var TYPE=<?=$TYPE?>;var ID=<?=$ID?>;</script>
		<script src="js/vendor/jmin.js"></script>
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
		<script src="js/script.js"></script>
        <script src="js/ajax.js"></script>
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

    				<div class="labelsmall">Departs after</div>
    				<input class="input dateinput" id="departing" />
                    <div class="labelsmall labelcloser">Arrives before</div>
                    <input class="input dateinput" id="arriving" />

    				<div class="labelsmall">With Seats for</div>
    				<select class="input personinput" id="numberofseats">
    				</select>


    			</div>
    			<div id="right">

                    <div class="flights none pick">
                        <div class="loader">
                            <div class="spinner"><span></span></div>
                        </div>

                        <div class="noflights">
                            <img src="img/papers.png" />
                            <div class="text"></div>
                        </div>
                        
        				<? for($i=0;$i<1;$i++) { ?>
        				<div class="flight dummy">
        					<div class="upperleft">
        						<div class="column1 timerange">11:35am - 5:40pm</div>
        						<div class="column2 time">5h 40m</div>
        						<div class="date"></div>
        					</div>
        					<div class="upperleft upperleft2">
        						<div class="column1 airline">JetBlue Airways</div>
        						<div class="column2 airports">JFK - LAS</div>
        					</div>
        					<div class="bottomleft">
        						Operates on: Monday, Wednesday, Friday
        					</div>
        					<div class="price"><div>$125</div><span>.55</span></div>
                            <div class="stops"></div>
        					<div class="select button1">Select</div>
        				</div>
        				<? } ?>

                        <div class="modal bookFlight">
                            <div>
                                <div class="box">
                                    <h2>Book Flight</h2>
                                    <div class="flightdetail"></div>
                                    <div class="date"></div>
                                    <div class="legs"></div>
                                    <div class="prices">
                                        <div class="choice economy selected" selectprice="economy">
                                            <div class="type">Economy</div>
                                            <div class="price"><div></div><span></span></div>
                                        </div>
                                        <div class="choice firstclass" selectprice="first">
                                            <div class="type">First</div>
                                            <div class="price"><div></div><span></span></div>
                                        </div>
                                    </div>
                                    <? if ($TYPE == 1) {
                                        $ch = curl_init();
                                        curl_setopt($ch, CURLOPT_URL, "http://localhost:8080/Website/account/get?customerrep_id=".$ID); 
                                        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1); 
                                        $output = curl_exec($ch); 
                                        $output = json_decode($output);
                                        $output = $output->accounts;
                                        ?>
                                        <div class="customerlabel">Book under customer:</div>
                                        <select class="customer">
                                            <? for($i=0;$i<count($output);$i++) { ?>
                                                <option value="<?=$output[$i]->person_id?>">
                                                    <?=$output[$i]->first_name?>
                                                    <?=$output[$i]->last_name?>
                                                    &lt;<?=$output[$i]->email?>&gt;
                                                </option>
                                            <? } ?>
                                        </select>
                                    <? } ?>
                                    <div class="customerlabel">Select Reservation:</div>
                                    <select class="reservation">
                                        <option value='-1' selected>New Reservation</option>
                                    </select>
                                    <div class="book button1">Book Now</div>
                                </div>
                            </div>
                        </div>

                    </div>

    			</div>


    		</div>
    	</div>

	</body>
	
</html>




















