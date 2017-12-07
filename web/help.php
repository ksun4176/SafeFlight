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
                        (object)array(
                            "name" => "Editing Your Profile"
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
                            "name" => "Booking a Flight",
                            "children" => array(
                                (object)array(
                                    "name" => "Reverse Auction"
                                ),
                                (object)array(
                                    "name" => "Round Trip Flights"
                                ),
                            )
                        ),
                    )
                ),
                (object)array(
                    "name" => "Reservations",
                ),
                (object)array(
                    "name" => "Customer Representatives",
                ),
                (object)array(
                    "name" => "Managers",
                    "children" => array(
                        (object)array(
                            "name" => "Flights by Airport"
                        ),
                        (object)array(
                            "name" => "Employee Management"
                        ),
                        (object)array(
                            "name" => "View Reservations"
                        ),
                        (object)array(
                            "name" => "Most Revenue Generated"
                        ),
                        (object)array(
                            "name" => "Flight Customers"
                        ),
                        (object)array(
                            "name" => "Sales Report"
                        ),
                    )
                ),
            );
            echo "<script type='text/javascript'>var helps = '".json_encode($help)."';</script>";
        ?>

    	<div id="container">
    		<div id="content">
                <div id="left">

                </div>
                <div id="right">
                    <div id="getting_started">
                        <h2>Getting Started</h2>
                        <p>
                            Click on the help links to the left to find more information about how to use SafeFlight.
                        </p>
                    </div>
                    <div id="creating_an_account">
                        <h2>Creating an account</h2>
                        <p>
                            Click the "Signup" tab in the header on the upper right.
                        </p>
                        <p>
                            Enter your first name, and last name. Choose a unique username, and then a password and submit. You will be taken to your account to edit more information.
                        </p>
                    </div>
                    <div id="logging_in">
                        <h2>Logging in</h2>
                        <p>
                            Click the "Login" tab in the header on the upper right.
                        </p>
                        <p>
                            Enter your username and password. You will be taken to your account page
                        </p>
                    </div>
                    <div id="editing_your_profile">
                        <h2>Editing Your Profile</h2>
                        <p>
                            Once you're signed in, click the "Account" tab in the header.<br />
                            Then, click "Edit Profile Information"
                        </p>
                        <img src="/img/help/editprofilebutton.png" class="img" />
                        <p>
                            In the modal that pops up, modify/fill in your information and click "Update Information" to save
                        </p>
                        <img src="/img/help/editprofilemodal.png" class="img" />
                    </div>
                    <div id="browsing_flights">
                        <h2>Browsing Flights</h2>
                        <p>
                            Click on the "flights" tab to browse flights
                        </p>
                        <p>
                            Use the left hand side interface to select how you'd like to browse flights.
                        </p>
                    </div>
                    <div id="searching_flights">
                        <h2>Searching Flights</h2>
                        <p>
                            On the left side, specify the city you are flying from, and the city you are flying to, to begin searching for flights. Specify your seats if you would like to book for more than one person.<br /> Optionally, you can also specify the time frame you would like to depart or arrive.
                        </p>
                        <img src="/img/help/searchflights.png" class="img" />
                        <p>
                            If you would like a round trip flight, press the return flight checkbox, and optionally, specify a return date.
                        </p>
                        <img src="/img/help/searchflightsreturn.png" class="img" />
                    </div>
                    <div id="personal_suggestions">
                        <h2>Personal Suggestions</h2>
                        <p>This will give you a list of flights that are catered specifically to you, given your browsing history and reservation history.</p>
                        <p>As a customer representative, you can choose to produce a list of flights catered to any of your customers here.</p>
                        <img src="/img/help/personalflightscr.png" class="img" />
                    </div>
                    <div id="best_sellers">
                        <h2>Best Sellers</h2>
                        <p>This will give you a list of flights that are currently being booked the most times.</p>
                    </div>
                    <div id="booking_a_flight">
                        <h2>Booking a Flight</h2>
                        <img src="/img/help/bookselect.png" class="img" />
                        <p>
                            When you find a flight you'd like to book, select it.
                        </p>
                        <p>
                            Choose the pricing you want. For reverse auctions, see the reverse auction section on the left. Choose if you want to book the flight under a new reservation or an existing reservation, and if you are a customer representative, pick the customer you'd like to book the flight under.
                        </p>
                        <img src="/img/help/bookmodal1.png" class="img" />
                        <p>
                            In the following passenger details modal, fill in the information for the passengers you want to book for. You can fill in the field with your own account information, or specify a new passenger.
                        </p>
                    </div>
                    <div id="reverse_auction">
                        <h2>Reverse Auction</h2>
                        <img src="/img/help/hasreverse.png" class="img" />
                        <p>
                            If a flight has a reverse auction option, you can choose to place a bid on it.<br />Choose the auction option when selecting pricing.
                        </p>
                        <img src="/img/help/chooseauction.png" class="img" />
                        <p>
                            In the following modal, specify a bid you'd like to place. If it is high enough, you will be given the option of booking the flight.
                        </p>
                        <img src="/img/help/auctionmodal.png" class="img" />
                    </div>
                    <div id="round_trip_flights">
                        <h2>Round Trip Flights</h2>
                        <p>
                            If you choose to search for round trip flights, each flight will have a flight going to your destination and a return flight.
                        </p>
                        <p>
                            The specified price will include both flights, and when you book, both flights will automatically be added to your reservation.
                        </p>
                    </div>
                    <div id="reservations">
                        <h2>Reservations</h2>
                        <img src="/img/help/accres.png" class="img" />
                        <p>
                            On your account page, you can see your reservations.
                        </p>
                        <p>You can use the dropdown to filter your reservations.</p>
                        <p>You can also view/edit a reservation by clicking on the button next to the row.</p>
                        <img src="/img/help/resit.png" class="img" />
                        <p>In this modal, you will see your reservation itinerary. You can also delete the reservation.</p>
                    </div>
                    <div id="customer_representatives">
                        <h2>Customer Representatives</h2>
                        <p>
                            As a customer representative, in your dashboard, you can make reservations for other customers. In this dashboard you can also view the other employees working for SafeFlight.
                        </p>
                        <img src="/img/help/getmailinglist.png" class="img" />
                        <p>
                            You can also view a mailing list of customers, if you need to send important information or a newsletter.
                        </p>
                    </div>
                    <div id="managers">
                        <h2>Managers</h2>
                        <p>As a manager, you have a comprehensive dashboard with many tools,</p>
                        <p>See the submenu on the left to see more of what managers can do.</p>
                    </div>
                    <div id="flights_by_airport">
                        <h2>Flights By Airport</h2>
                        <p>
                            By visiting the flights page, you can also see flights listed by airport.
                        </p>
                        <img src="/img/help/flightsairport.png" class="img" />
                    </div>
                    <div id="employee_management">
                        <h2>Employee Management</h2>
                        <p>
                            In your manager dashboard, you can see a listing of all employees.
                        </p>
                        <img src="/img/help/employee.png" class="img" />
                        <p>
                            By clicking edit, you can open up an employee, and edit their information or delete the employee.
                        </p>
                        <img src="/img/help/emp2.png" class="img" />
                        <p>
                            You can also add new employees.    
                        </p>
                        <img src="/img/help/empcreate.png" class="img" />
                    </div>
                    <div id="view_reservations">
                        <h2>View Reservations</h2>
                        <img src="/img/help/ress.png" class="img" />
                        <p>
                            In your dashboard, you can view a list of reservations by flight or customer.
                        </p>
                        <p>Clicking "view" will also allow you to view the reservation's details/itinerary.</p>
                    </div>
                    <div id="most_revenue_generated">
                        <h2>Most Revenue Generated</h2>
                        <img src="/img/help/mostrev.png" class="img" />
                        <p>
                            In your dashboard, you can create a list of customers or customer representatives to see who has generated the most revenue.
                        </p>
                    </div>
                    <div id="flight_customers">
                        <h2>Customers of a Flight</h2>
                        <img src="/img/help/fcust.png" class="img" />
                        <p>
                            In your dashboard, you can view the customers of any specified flight.
                        </p>
                    </div>
                    <div id="sales_report">
                        <h2>Sales Report</h2>
                        <img src="/img/help/sreport.png" class="img" />
                        <p>
                            In your dashboard at the bottom, you can view a monthly sales report of any month of your choice.
                        </p>
                    </div>
                    <div id="">
                        <h2></h2>
                    </div>
                    <div id="">
                        <h2></h2>
                        <p>
                            
                        </p>
                    </div>
                </div>

    		</div>
    	</div>

	</body>
	
</html>




















