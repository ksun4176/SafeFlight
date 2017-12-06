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

        <?php
            if ($TYPE == 1) {
                $ch = curl_init();
                curl_setopt($ch, CURLOPT_URL, "http://localhost:8080/Website/account/get?customer_rep_id=".$ID);
                curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);

                global $output;
                $output = curl_exec($ch); 
                $output = json_decode($output);
                $output = $output->accounts;
            }
        ?>

    	<div id="container">
    		<div id="content">

    			<h1>Flights</h1>

    			<div id="left">

                    <? if ($TYPE == 2) { ?>
                        <div class="option allflights">
                            <div class="title">All Flights</div>
                        </div>
                        <div class="option airportflights">
                            <div class="title">Flights by Airport</div>
                            <div class="expand t3">
                                <div class="label">Airport:</div>
                                <select class="airports"></select>
                            </div>
                        </div>
                    <? } ?>
                    <div class="option bestflights">
                        <div class="title"><?=($TYPE == 2)?"Most Active Flights":"Best Seller Flights"?></div>
                    </div>
                    <? if ($TYPE == 0 || $TYPE == 1) { ?>
                        <div class="option personalflights">
                            <div class="title">Personal Suggestions</div>
                            <? if ($TYPE == 1) { ?>
                                <div class="expand t3">
                                    <div class="label">For customer:</div>
                                    <select class="personalcustomer">
                                        <? global $output; for($i=0;$i<count($output);$i++) { ?>
                                            <option value="<?=$output[$i]->person_id?>">
                                                <?=$output[$i]->first_name?>
                                                <?=$output[$i]->last_name?>
                                                &lt;<?=$output[$i]->email?>&gt;
                                            </option>
                                        <? } ?>
                                    </select>
                                </div>
                            <? } ?>
                        </div>
                    <? } ?>
                    <div class="option searchflights selected">
                        <div class="title">Search Flights</div>
                        <div class="expand t3">
            				<div class="label">I am looking for a flight:</div>

            				<div class="labelsmall">Flying from</div>
            				<input class="input locationinput" id="flyingfrom" />
            				<div class="labelsmall labelcloser">Flying to</div>
            				<input class="input locationinput" id="flyingto" />

            				<div class="labelsmall">Departs after</div>
            				<input class="input dateinput" id="departing" />
                            <div class="labelsmall labelcloser">Arrives before</div>
                            <input class="input dateinput" id="arriving" />

                            <div class="return t3">
                                <input class="checkbox" type="checkbox" />
                                <div class="labelsmall labelreturn">With a return flight</div>
                                <div class="labelsmall labelcloser">That returns by</div>
                                <input class="input dateinput" id="returning" />
                            </div>

            				<div class="labelsmall">With Seats for</div>
            				<select class="input personinput" id="numberofseats">
            				</select>
                        </div>
                    </div>

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
                        
        				<div class="flight dummy">
                            <div class="block1">
            					<div class="upperleft">
            						<div class="column1 timerange">11:35am - 5:40pm</div>
            						<div class="column2 time">5h 40m</div>
            						<div class="date"></div>
            					</div>
            					<div class="upperleft upperleft2">
            						<div class="column1 airline">JetBlue Airways</div>
            						<div class="column2 airports">JFK - LAS</div>
            					</div>
                                <div class="stops"></div>
                            </div>
                            <div class="block2">
                                <div class="upperleft">
                                    <div class="column1 timerange">11:35am - 5:40pm</div>
                                    <div class="column2 time">5h 40m</div>
                                    <div class="date"></div>
                                </div>
                                <div class="upperleft upperleft2">
                                    <div class="column1 airline">JetBlue Airways</div>
                                    <div class="column2 airports">JFK - LAS</div>
                                </div>
                                <div class="stops"></div>
                            </div>
                            <div class="bottomleft">
                                Operates on: Monday, Wednesday, Friday
                            </div>
        					<div class="price"><div>$125</div><span>.55</span></div>
        					<div class="reverseauction">Has Reverse Auction</div>
                            <?php if ($TYPE ==  0 || $TYPE == 1) { ?>
                                <div class="select button1">Select</div>
                            <? } else if ($TYPE == -1) { ?>
                                <div class="select button1 disabled">Login to Book</div>
                            <? } else if ($TYPE == 2) { ?>
                                <div class="select button1">View Customers</div>
                            <? } ?>
        				</div>

                        <div class="modal bookFlight">
                            <div>
                                <div class="box">
                                    <h2>Book Flight</h2>
                                    <div class="flightdetail fa"></div>
                                    <div class="date fa"></div>
                                    <div class="legs fa"></div>
                                    <div class="flightdetail fb"></div>
                                    <div class="date fb"></div>
                                    <div class="legs fb"></div>
                                    <div class="prices">
                                        <div class="choice economy selected" selectprice="economy">
                                            <div class="type">Economy</div>
                                            <div class="price"><div></div><span></span></div>
                                        </div>
                                        <div class="choice firstclass" selectprice="first">
                                            <div class="type">First</div>
                                            <div class="price"><div></div><span></span></div>
                                        </div>
                                        <div class="choice auction" selectprice="auction">
                                            <div class="type">Auction</div>
                                            <div class="price"><div>$---</div><span>.--</span></div>
                                        </div>
                                    </div>
                                    <? if ($TYPE == 1) { ?>
                                        <div class="customerlabel">Book under customer:</div>
                                        <select class="customer">
                                            <? global $output; for($i=0;$i<count($output);$i++) { ?>
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
                                    <div class="book button1">Select Flight</div>
                                </div>
                            </div>
                        </div>

                        <div class="modal revauction">
                            <div>
                                <div class="box">
                                    <h2>Reverse Auction</h2>
                                    <div class="labelbid">Enter your bid:</div>
                                    <input type="number" min="10" step="10" />
                                    <div class="message1"></div>
                                    <div class="placebid button1">Place Bid</div>
                                    <div class="labelbid labelbid2">Bid History</div>
                                    <div class="bids datasets">
                                        <div class="bid dataset top">
                                            <div class="column id">ID</div>
                                            <div class="column amount">Bid Amount</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="modal passengers">
                            <div>
                                <div class="box">
                                    <h2>Passenger Details</h2>
                                    <div class="pages">
                                        <div class="arrow left"></div>
                                        <div class="count">
                                            <span class="on">1</span>
                                            <span>/</span>
                                            <span class="total">4</span>
                                        </div>
                                        <div class="arrow right"></div>
                                    </div>
                                    <select class="accountToggle">
                                        <option value="myaccount">Use Account Details</option>
                                        <option value="fillin">Fill In Passenger Info</option>
                                    </select>
                                    <div class="editAccountForm">
                                        <div class="name">
                                            <div class="field firstName">
                                                <div class="label">First Name</div>
                                                <input class="value" />
                                            </div>
                                            <div class="field lastName">
                                                <div class="label">Last Name</div>
                                                <input class="value" />
                                            </div>
                                        </div>
                                        <div class="field address">
                                            <div class="label">Address</div>
                                            <input class="value" />
                                        </div>
                                        <div class="address2">
                                            <div class="field city">
                                                <div class="label">City</div>
                                                <input class="value" />
                                            </div>
                                            <div class="field state">
                                                <div class="label">State</div>
                                                <input class="value" />
                                            </div>
                                            <div class="field zip">
                                                <div class="label">Zip Code</div>
                                                <input class="value" />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="message"></div>
                                    <div class="book button1">Book Now</div>
                                </div>
                            </div>
                        </div>

                    </div>

                    </div>

    			</div>


    		</div>
    	</div>

	</body>
	
</html>




















